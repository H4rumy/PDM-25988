package com.example.loja.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loja.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _loginState = MutableStateFlow<FirebaseUser?>(null)
    val loginState: StateFlow<FirebaseUser?> = _loginState

    // Função para logar o usuário
    fun login(email: String, senha: String) {
        viewModelScope.launch {
            val result = authRepository.login(email, senha)
            _loginState.value = result
        }
    }
}
