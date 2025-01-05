package com.example.loja.repository

import com.example.loja.classes.Produto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth

class Carrinho {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Função para adicionar um produto ao carrinho
    fun adicionarProdutoAoCarrinho(produto: Produto) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val cartRef = db.collection("users").document(userId).collection("cart")

            cartRef.document(produto.nome) // Usando o nome como ID do produto
                .set(mapOf(
                    "nome" to produto.nome,
                    "preco" to produto.preco
                ))
                .addOnSuccessListener {
                    println("Produto adicionado ao carrinho!")
                }
                .addOnFailureListener { e ->
                    println("Erro ao adicionar produto ao carrinho: $e")
                }
        }
    }

    // Função para carregar os produtos do carrinho
    fun carregarCarrinho(onComplete: (List<Produto>) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val cartRef = db.collection("users").document(userId).collection("cart")
            cartRef.get()
                .addOnSuccessListener { snapshot ->
                    val produtosCarrinho = snapshot.documents.mapNotNull { doc ->
                        val nome = doc.getString("nome") ?: return@mapNotNull null
                        val preco = doc.getDouble("preco") ?: return@mapNotNull null
                        Produto(nome = nome, preco = preco)
                    }
                    onComplete(produtosCarrinho)
                }
                .addOnFailureListener { e ->
                    println("Erro ao carregar carrinho: $e")
                }
        }
    }
}
