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

    init {
        carregarCarrinhosRecebidos()
    }

    private fun carregarCarrinhosRecebidos() {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("compartilhamentos")
            .whereEqualTo("sharedWithUserId", userId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("ReceberViewModel", "Erro ao carregar carrinhos", e)
                    return@addSnapshotListener
                }

                viewModelScope.launch {
                    try {
                        val carrinhos = snapshot?.documents?.mapNotNull { doc ->
                            val ownerUserId = doc.getString("ownerUserId") ?: return@mapNotNull null

                            val ownerDoc = firestore.collection("users")
                                .document(ownerUserId)
                                .get()
                                .await()

                            val ownerEmail = ownerDoc.getString("email") ?: return@mapNotNull null

                            CarrinhoRecebido(
                                ownerUserId = ownerUserId,
                                ownerEmail = ownerEmail
                            )
                        } ?: emptyList()

                        _carrinhosRecebidos.value = carrinhos
                    } catch (e: Exception) {
                        Log.e("ReceberViewModel", "Erro ao processar carrinhos", e)
                    }
                }
            }
    }
}

data class CarrinhoRecebido(
    val ownerUserId: String = "",
    val ownerEmail: String = ""
)