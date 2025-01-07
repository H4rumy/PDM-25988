package com.example.loja.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loja.classes.CarrinhoCompartilhado
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserInfo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import java.util.UUID

class PartilharViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _todosUsers = MutableStateFlow<List<AppUser>>(emptyList())
    val todosUsers: StateFlow<List<AppUser>> = _todosUsers

    private val _usersSelecionados = MutableStateFlow<Set<String>>(emptySet())
    val usersSelecionados: StateFlow<Set<String>> = _usersSelecionados

    private val _usersCompartilhados = MutableStateFlow<List<CarrinhoCompartilhado>>(emptyList())
    val usersCompartilhados: StateFlow<List<CarrinhoCompartilhado>> = _usersCompartilhados

    init {
        carregarUsers()
        carregarCompartilhamentos()
    }

    private fun carregarUsers() {
        val currentUserId = auth.currentUser?.uid ?: return

        firestore.collection("user")
            .whereNotEqualTo("uid", currentUserId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) return@addSnapshotListener

                val users = snapshot?.documents?.mapNotNull { doc ->
                    doc.getString("email")?.let { email ->
                        AppUser(
                            uid = doc.id,
                            email = email
                        )
                    }
                } ?: emptyList()

                _todosUsers.value = users
            }
    }

    fun compartilharComSelecionados(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val currentUser = auth.currentUser ?: return

        viewModelScope.launch {
            try {
                _usersSelecionados.value.forEach { userId ->
                    val usuario = _todosUsers.value.find { it.uid == userId }
                    usuario?.let {
                        val compartilhamento = CarrinhoCompartilhado(
                            id = UUID.randomUUID().toString(),
                            ownerUserId = currentUser.uid,
                            sharedWithUserId = it.uid,
                            sharedWithEmail = it.email
                        )

                        firestore.collection("compartilhamentos")
                            .document(compartilhamento.id)
                            .set(compartilhamento)
                    }
                }
                _usersSelecionados.value = emptySet() // Limpa as seleções após compartilhar
                onSuccess()
            } catch (e: Exception) {
                onError("Erro ao compartilhar carrinho")
            }
        }
    }

    fun toggleUsuarioSelecionado(userId: String) {
        val atuais = _usersSelecionados.value.toMutableSet()
        if (atuais.contains(userId)) {
            atuais.remove(userId)
        } else {
            atuais.add(userId)
        }
        _usersSelecionados.value = atuais
    }

    private fun carregarCompartilhamentos() {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("compartilhamentos")
            .whereEqualTo("ownerUserId", userId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) return@addSnapshotListener

                val compartilhamentos = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(CarrinhoCompartilhado::class.java)
                } ?: emptyList()

                _usersCompartilhados.value = compartilhamentos
            }
    }

    fun compartilharComUsuario(email: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val currentUser = auth.currentUser ?: return

        // Primeiro, encontrar o usuário pelo email
        firestore.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    onError("Usuário não encontrado")
                    return@addOnSuccessListener
                }

                val targetUser = documents.documents.first()
                val compartilhamento = CarrinhoCompartilhado(
                    id = UUID.randomUUID().toString(),
                    ownerUserId = currentUser.uid,
                    sharedWithUserId = targetUser.id,
                    sharedWithEmail = email
                )

                firestore.collection("compartilhamentos")
                    .document(compartilhamento.id)
                    .set(compartilhamento)
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener {
                        onError("Erro ao compartilhar carrinho")
                    }
            }
            .addOnFailureListener {
                onError("Erro ao buscar usuário")
            }
    }

    fun removerCompartilhamento(compartilhamentoId: String) {
        firestore.collection("compartilhamentos")
            .document(compartilhamentoId)
            .delete()
    }

    fun carregarCarrinhosCompartilhadosComigo(): Flow<List<String>> = callbackFlow {
        val userId = auth.currentUser?.uid ?: return@callbackFlow

        val listener = firestore.collection("compartilhamentos")
            .whereEqualTo("sharedWithUserId", userId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    close(e)
                    return@addSnapshotListener
                }

                val ownerIds = snapshot?.documents?.mapNotNull { doc ->
                    doc.getString("ownerUserId")
                } ?: emptyList()

                trySend(ownerIds)
            }

        awaitClose { listener.remove() }
    }
}

data class AppUser(
    val uid: String,
    val email: String
)