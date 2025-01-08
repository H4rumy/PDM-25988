package com.example.loja.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loja.classes.User
import com.example.loja.repository.AuthRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RegistoViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore = Firebase.firestore

    // Estados para email, palavra-passe, confirmação, erro e carregamento
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isRegistrationSuccessful = MutableStateFlow(false)
    val isRegistrationSuccessful: StateFlow<Boolean> = _isRegistrationSuccessful

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _confirmPassword.value = newConfirmPassword
    }

    suspend fun signUp(email: String, password: String, confirmPassword: String) {
        try {
            _isLoading.value = true
            _errorMessage.value = ""

            // Validações
            if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                _errorMessage.value = "Preencha todos os campos"
                return
            }

            if (password != confirmPassword) {
                _errorMessage.value = "As senhas não coincidem"
                return
            }

            if (password.length < 6) {
                _errorMessage.value = "A senha deve ter pelo menos 6 caracteres"
                return
            }

            // Criar usuário no Firebase
            val result = auth.createUserWithEmailAndPassword(email.trim(), password).await()

            // Se chegou aqui, o registro foi bem sucedido
            val user = result.user
            if (user != null) {
                // Salvar informações adicionais do usuário no Firestore
                val userDoc = hashMapOf(
                    "email" to email.trim(),
                    "uid" to user.uid
                )

                firestore.collection("user")
                    .document(user.uid)
                    .set(userDoc)
                    .await()

                _isRegistrationSuccessful.value = true
            }

        } catch (e: FirebaseAuthWeakPasswordException) {
            _errorMessage.value = "A senha é muito fraca"
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            _errorMessage.value = "Email inválido"
        } catch (e: FirebaseAuthUserCollisionException) {
            _errorMessage.value = "Este email já está em uso"
        } catch (e: Exception) {
            _errorMessage.value = "Erro ao criar conta: ${e.message}"
            Log.e("RegistoViewModel", "Erro no registro", e)
        } finally {
            _isLoading.value = false
        }
    }
}
