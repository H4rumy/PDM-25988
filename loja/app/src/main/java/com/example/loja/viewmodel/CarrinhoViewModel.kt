package com.example.loja.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loja.classes.Produto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CarrinhoViewModel : ViewModel() {
    private val _produtosCarrinho = MutableStateFlow<List<Produto>>(emptyList())
    val produtosCarrinho: StateFlow<List<Produto>> = _produtosCarrinho

    private val firestore = FirebaseFirestore.getInstance()

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
}
