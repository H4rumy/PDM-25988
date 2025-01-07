package com.example.loja.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ReceberViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _carrinhosRecebidos = MutableStateFlow<List<CarrinhoRecebido>>(emptyList())
    val carrinhosRecebidos: StateFlow<List<CarrinhoRecebido>> = _carrinhosRecebidos

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        carregarCarrinhosRecebidos()
    }

    private fun carregarCarrinhosRecebidos() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Log.e("ReceberViewModel", "Usuário não está autenticado")
            return
        }

        Log.d("ReceberViewModel", "Iniciando busca para usuário: $userId")

        firestore.collection("compartilhamentos")
            .whereEqualTo("sharedWithUserId", userId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("ReceberViewModel", "Erro ao carregar carrinhos", e)
                    _error.value = "Erro ao carregar carrinhos: ${e.message}"
                    return@addSnapshotListener
                }

                Log.d("ReceberViewModel", "Documentos encontrados em compartilhamentos: ${snapshot?.documents?.size}")

                viewModelScope.launch {
                    try {
                        val carrinhos = snapshot?.documents?.mapNotNull { doc ->
                            val ownerUserId = doc.getString("ownerUserId")
                            Log.d("ReceberViewModel", "Documento de compartilhamento: ${doc.data}")

                            try {
                                val userDoc = firestore.collection("user")
                                    .document(ownerUserId!!)
                                    .get()
                                    .await()

                                if (!userDoc.exists()) {
                                    // Se não encontrar direto, tenta pela query
                                    val userQuery = firestore.collection("user")
                                        .whereEqualTo("uid", ownerUserId)
                                        .get()
                                        .await()

                                    if (userQuery.documents.isEmpty()) {
                                        Log.e("ReceberViewModel", "Usuário não encontrado: $ownerUserId")
                                        return@mapNotNull null
                                    }

                                    val document = userQuery.documents.first()
                                    Log.d("ReceberViewModel", "Usuário encontrado por query: ${document.data}")

                                    val ownerEmail = document.getString("email")
                                    if (ownerEmail == null) {
                                        Log.e("ReceberViewModel", "Email não encontrado nos campos: ${document.data?.keys}")
                                        return@mapNotNull null
                                    }

                                    CarrinhoRecebido(
                                        ownerUserId = ownerUserId,
                                        ownerEmail = ownerEmail
                                    )
                                } else {
                                    Log.d("ReceberViewModel", "Usuário encontrado direto: ${userDoc.data}")

                                    val ownerEmail = userDoc.getString("email")
                                    if (ownerEmail == null) {
                                        Log.e("ReceberViewModel", "Email não encontrado nos campos: ${userDoc.data?.keys}")
                                        return@mapNotNull null
                                    }

                                    CarrinhoRecebido(
                                        ownerUserId = ownerUserId,
                                        ownerEmail = ownerEmail
                                    )
                                }
                            } catch (e: Exception) {
                                Log.e("ReceberViewModel", "Erro ao buscar usuário: $ownerUserId", e)
                                null
                            }
                        } ?: emptyList()

                        _carrinhosRecebidos.value = carrinhos
                        Log.d("ReceberViewModel", "Total de carrinhos processados: ${carrinhos.size}")
                    } catch (e: Exception) {
                        Log.e("ReceberViewModel", "Erro ao processar carrinhos", e)
                        _error.value = "Erro ao processar carrinhos: ${e.message}"
                    }
                }
            }
    }
}

data class CarrinhoRecebido(
    val ownerUserId: String = "",
    val ownerEmail: String = ""
)