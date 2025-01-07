package com.example.loja.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loja.classes.CarrinhoItem
import com.example.loja.classes.Produto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class CarrinhoViewModel : ViewModel() {
    private val _produtosCarrinho = MutableStateFlow<List<Produto>>(emptyList())
    val produtosCarrinho: StateFlow<List<Produto>> = _produtosCarrinho

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _carrinhoCount = MutableStateFlow(0)
    val carrinhoCount: StateFlow<Int> = _carrinhoCount

    private val _itensCarrinho = MutableStateFlow<List<CarrinhoItem>>(emptyList())
    val itensCarrinho: StateFlow<List<CarrinhoItem>> = _itensCarrinho

    init {
        // Iniciar observação do carrinho
        observarCarrinho()
    }

    private fun observarCarrinho() {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("carrinho")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                // Soma todas as quantidades
                val totalQuantidade = snapshot?.documents?.sumOf { doc ->
                    doc.getLong("quantidade")?.toInt() ?: 0
                } ?: 0

                _carrinhoCount.value = totalQuantidade
            }
    }

    // Método público para carregar os produtos do carrinho
    fun carregarProdutosCarrinho(userId: String) {
        firestore.collection("carrinho")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("CarrinhoViewModel", "Erro ao carregar carrinho", e)
                    return@addSnapshotListener
                }

                val itens = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        CarrinhoItem(
                            id = doc.id,
                            userId = doc.getString("userId") ?: "",
                            productId = doc.getString("productId") ?: "",
                            quantidade = doc.getLong("quantidade")?.toInt() ?: 1,
                            nome = doc.getString("nome") ?: "",
                            preco = doc.getDouble("preco") as Number? ?: 0.0,
                            imagemUrl = doc.getString("imagemUrl") ?: ""
                        )
                    } catch (e: Exception) {
                        Log.e("CarrinhoViewModel", "Erro ao converter documento", e)
                        null
                    }
                } ?: emptyList()

                _itensCarrinho.value = itens
                Log.d("CarrinhoViewModel", "Itens carregados: ${itens.size}")
            }
    }

    fun adicionarAoCarrinho(produto: Produto, quantidade: Int = 1) {
        val userId = auth.currentUser?.uid ?: return

        val carrinhoItem = hashMapOf(
            "id" to UUID.randomUUID().toString(),
            "userId" to userId,
            "productId" to produto.id,
            "quantidade" to quantidade,
            "nome" to produto.nome,
            "preco" to produto.preco.toDouble(), // Convertendo para Double ao salvar
            "imagemUrl" to produto.imagemUrl
        )

        firestore.collection("carrinho")
            .document(carrinhoItem["id"] as String)
            .set(carrinhoItem)
            .addOnSuccessListener {
                // Item adicionado com sucesso
            }
            .addOnFailureListener { e ->
                Log.e("CarrinhoViewModel", "Erro ao adicionar item", e)
            }
    }

    fun removerDoCarrinho(carrinhoItemId: String) {
        firestore.collection("carrinho")
            .document(carrinhoItemId)
            .delete()
            .addOnFailureListener { e ->
                Log.e("CarrinhoViewModel", "Erro ao remover item do carrinho", e)
            }
    }

    fun calcularTotal(): Double {
        return itensCarrinho.value.sumOf { item ->
            item.preco.toDouble() * item.quantidade
        }
    }

    fun atualizarQuantidade(itemId: String, novaQuantidade: Int) {
        if (novaQuantidade <= 0) {
            removerDoCarrinho(itemId)
        } else {
            firestore.collection("carrinho")
                .document(itemId)
                .update("quantidade", novaQuantidade)
                .addOnFailureListener { e ->
                    Log.e("CarrinhoViewModel", "Erro ao atualizar quantidade", e)
                }
        }
    }


    fun getCarrinhoItems(onSuccess: (List<CarrinhoItem>) -> Unit) {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("carrinho")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                val items = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(CarrinhoItem::class.java)
                } ?: emptyList()

                onSuccess(items)
            }
    }

    fun carregarCarrinhoCompartilhado(ownerUserId: String) {
        firestore.collection("carrinho")
            .whereEqualTo("userId", ownerUserId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) return@addSnapshotListener

                val itens = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        CarrinhoItem(
                            id = doc.id,
                            userId = doc.getString("userId") ?: "",
                            productId = doc.getString("productId") ?: "",
                            quantidade = doc.getLong("quantidade")?.toInt() ?: 1,
                            nome = doc.getString("nome") ?: "",
                            preco = doc.getDouble("preco") as Number? ?: 0.0,
                            imagemUrl = doc.getString("imagemUrl") ?: ""
                        )
                    } catch (e: Exception) {
                        null
                    }
                } ?: emptyList()

                _itensCarrinho.value = itens
            }
    }

}
