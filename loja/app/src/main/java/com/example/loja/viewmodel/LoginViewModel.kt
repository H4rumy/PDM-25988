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
    private val auth = FirebaseAuth.getInstance()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    suspend fun signIn(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            _errorMessage.value = "Por favor, preencha todos os campos"
            return false
        }

        _isLoading.value = true
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            _errorMessage.value = ""
            true
        } catch (e: Exception) {
            _errorMessage.value = when {
                e.message?.contains("password") == true -> "Senha incorreta"
                e.message?.contains("user") == true -> "Utilizador não encontrado"
                else -> "Erro ao fazer login: ${e.message}"
            }
            false
        } finally {
            _isLoading.value = false
        }
    }

    fun signOut() {
        auth.signOut()
    }
}