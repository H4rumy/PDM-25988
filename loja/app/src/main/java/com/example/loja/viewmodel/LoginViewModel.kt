package com.example.loja.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

class LoginViewModel(application: Application) : AndroidViewModel(application) {

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

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    init {
        initializeFirebase()
        checkUserSession()
    }

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
        if (!::auth.isInitialized) {
            _errorMessage.value = "Firebase Auth não está inicializado"
            return
        }

        _isLoading.value = true
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            _currentUser.value = result.user
            _isLoginSuccessful.value = true
        } catch (e: Exception) {
            _errorMessage.value = e.message ?: "Erro ao fazer login."
            _isLoginSuccessful.value = false
        } finally {
            _isLoading.value = false
        }
    }

    companion object {
        fun provideFactory(
            application: Application
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LoginViewModel(application) as T
            }
        }
    }

    private fun initializeFirebase() {
        try {
            auth = FirebaseAuth.getInstance()
            firestore = FirebaseFirestore.getInstance()
            Log.d("LoginViewModel", "Firebase inicializado com sucesso. Usuário atual: ${auth.currentUser?.email}")
        } catch (e: Exception) {
            Log.e("LoginViewModel", "Erro ao inicializar Firebase: ${e.message}")
            _errorMessage.value = "Erro ao inicializar Firebase: ${e.message}"
        }
    }


    fun checkUserSession() {
        try {
            if (::auth.isInitialized) {
                _currentUser.value = auth.currentUser
                _isLoginSuccessful.value = auth.currentUser != null
            }
        } catch (e: Exception) {
            _errorMessage.value = "Erro ao verificar sessão: ${e.message}"
        }
    }

    fun signOut() {
        if (::auth.isInitialized) {
            auth.signOut()
            _currentUser.value = null
            _isLoginSuccessful.value = false
        }
    }
}