package com.example.loja.classes

import com.google.firebase.firestore.DocumentId

data class Produto(
    @DocumentId
    val id: String = "",  // A Firebase atribui o ID automaticamente e é melhor usar String para o ID
    val nome: String = "",
    val preco: Number = 0.00
)
