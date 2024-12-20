package com.example.loja.repository

import com.example.loja.classes.Produto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProdutoRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val produtosCollection = firestore.collection("produtos")


    }

    /* // Função para adicionar um novo produto
    suspend fun adicionarProduto(produto: Produto) {
        try {
            produtosCollection.add(produto).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Função para atualizar um produto existente
    suspend fun atualizarProduto(produtoId: String, produto: Produto) {
        try {
            produtosCollection.document(produtoId).set(produto).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Função para deletar um produto
    suspend fun deletarProduto(produtoId: String) {
        try {
            produtosCollection.document(produtoId).delete().await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    } */


