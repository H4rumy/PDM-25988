package com.example.loja.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loja.classes.Produto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import android.util.Log  // Adiciona o log para depuração

class ProdutosViewModel : ViewModel() {

    private val _produtos = MutableStateFlow<List<Produto>>(emptyList())
    val produtos: StateFlow<List<Produto>> = _produtos

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val firestore = FirebaseFirestore.getInstance()

    // Função para carregar os produtos do Firestore
    fun carregarProdutos() {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                firestore.collection("produto")
                    .get()
                    .addOnSuccessListener { result ->
                        val listaProdutos = mutableListOf<Produto>()
                        for (document in result) {
                            val nome = document.getString("nome") ?: ""
                            val preco = document.getDouble("preco") ?: 0.0
                            val id = document.id

                            listaProdutos.add(Produto(id = id, nome = nome, preco = preco))
                        }
                        _produtos.value = listaProdutos
                        _isLoading.value = false
                    }
                    .addOnFailureListener { exception ->
                        _errorMessage.value = "Erro ao carregar produtos: ${exception.message}"
                        _isLoading.value = false
                    }
            } catch (e: Exception) {
                _errorMessage.value = "Erro desconhecido: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    // Função para adicionar produto ao carrinho
    fun adicionarAoCarrinho(produto: Produto, carrinhoViewModel: CarrinhoViewModel) {
        viewModelScope.launch {
            try {

            } catch (e: Exception) {
                _errorMessage.value = "Erro ao adicionar ao carrinho: ${e.message}"
            }
        }
    }
}
