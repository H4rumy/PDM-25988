package com.example.loja.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loja.classes.CarrinhoCompartilhado
import com.example.loja.classes.User
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

    private val _todosUsers = MutableStateFlow<List<User>>(emptyList())
    val todosUsers: StateFlow<List<User>> = _todosUsers

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
        println("DEBUG: Iniciando carregamento de usuários")
        println("DEBUG: CurrentUserId = $currentUserId")

        firestore.collection("user")
            .whereNotEqualTo("uid", currentUserId)
            .get()
            .addOnSuccessListener { snapshot ->
                val users = snapshot.documents.mapNotNull { doc ->
                    try {
                        User( // Alterado para User
                            uid = doc.getString("uid") ?: doc.id,
                            email = doc.getString("email") ?: return@mapNotNull null
                        )
                    } catch (e: Exception) {
                        println("Erro ao converter documento: ${doc.data}")
                        null
                    }
                }
                println("Usuários carregados: $users")
                _todosUsers.value = users
            }
            .addOnFailureListener { e ->
                println("Erro ao carregar usuários: ${e.message}")
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