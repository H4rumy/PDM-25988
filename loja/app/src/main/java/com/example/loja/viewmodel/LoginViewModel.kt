package com.example.loja.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

class LoginViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Estados para email, senha, erro e carregamento
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isLoginSuccessful = MutableStateFlow(false)
    val isLoginSuccessful: StateFlow<Boolean> = _isLoginSuccessful

    // Guarda o usuário autenticado
    private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun setErrorMessage(message: String) {
        _errorMessage.value = message
    }

    fun clearError() {
        _errorMessage.value = ""
    }

    // Método para autenticação
    suspend fun signIn(email: String, password: String) {
        _isLoading.value = true
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            _currentUser.value = result.user  // Guarda o utilizador autenticado
            _isLoginSuccessful.value = true
        } catch (e: Exception) {
            _errorMessage.value = e.message ?: "Erro ao fazer login."
            _isLoginSuccessful.value = false
        } finally {
            _isLoading.value = false
        }
    }


    fun checkUserSession() {
        _currentUser.value = auth.currentUser
    }

    fun signOut() {
        auth.signOut()
        _currentUser.value = null
    }
}