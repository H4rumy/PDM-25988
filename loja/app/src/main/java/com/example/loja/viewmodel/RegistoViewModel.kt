package com.example.loja.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loja.classes.User
import com.example.loja.repository.AuthRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RegistoViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

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
        _isLoading.value = true
        _errorMessage.value = ""

        if (password != confirmPassword) {
            _errorMessage.value = "As palavras-passe não coincidem."
            _isLoading.value = false
            return
        }

        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            _isRegistrationSuccessful.value = true
        } catch (e: Exception) {
            _errorMessage.value = e.message ?: "Erro ao registar."
            _isRegistrationSuccessful.value = false
        } finally {
            _isLoading.value = false
        }
    }
}
