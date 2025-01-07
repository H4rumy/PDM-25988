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
        firestore.collection("carrinhos")
            .document(userId) // Usando o ID do usuário logado
            .collection("produtos")
            .get()
            .addOnSuccessListener { result ->
                val listaProdutos = mutableListOf<Produto>()
                for (document in result) {
                    val nome = document.getString("nome") ?: ""
                    val preco = document.getDouble("preco") ?: 0.0
                    val id = document.id
                    listaProdutos.add(Produto(id = id, nome = nome, preco = preco))
                }
                _produtosCarrinho.value = listaProdutos
            }
            .addOnFailureListener { exception ->
                Log.e("CarrinhoViewModel", "Erro ao carregar produtos do carrinho", exception)
            }
    }

    fun adicionarAoCarrinho(produto: Produto, quantidade: Int = 1) {
        val userId = auth.currentUser?.uid ?: return

        val carrinhoItem = CarrinhoItem(
            id = UUID.randomUUID().toString(),
            userId = userId,
            productId = produto.id,
            quantidade = quantidade,
            nome = produto.nome,
            preco = produto.preco,
            imagemUrl = produto.imagemUrl
        )

        firestore.collection("carrinho")
            .document(carrinhoItem.id)
            .set(carrinhoItem)
            .addOnSuccessListener {
                // Item adicionado com sucesso
            }
            .addOnFailureListener { e ->
                // Tratar erro
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

}
