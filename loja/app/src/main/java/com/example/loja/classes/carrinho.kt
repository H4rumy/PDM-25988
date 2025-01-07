package com.example.loja.classes

data class CarrinhoItem(
    val id: String = "",
    val userId: String = "",
    val productId: String = "",
    val quantidade: Int = 1,
    val nome: String = "",     // Para exibição no carrinho
    val preco: Number = 0.00,  // Para exibição no carrinho
    val imagemUrl: String = "" // Para exibição no carrinho
)