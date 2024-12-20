package com.example.loja.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loja.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Definindo diferentes estados para o processo de registro
sealed class RegistoState {
    object Loading : RegistoState()
    data class Success(val user: FirebaseUser) : RegistoState()
    data class Error(val message: String) : RegistoState()
}

class RegistoViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _registoState = MutableStateFlow<RegistoState>(RegistoState.Loading)
    val registoState: StateFlow<RegistoState> = _registoState

    // Função para registrar um novo usuário
    fun registar(email: String, senha: String) {
        viewModelScope.launch {
            _registoState.value = RegistoState.Loading
            val result = authRepository.registar(email, senha)
            if (result != null) {
                _registoState.value = RegistoState.Success(result)
            } else {
                _registoState.value = RegistoState.Error("Erro no registo.")
            }
        }
    }
}
