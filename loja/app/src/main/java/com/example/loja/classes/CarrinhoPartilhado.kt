package com.example.loja.classes

data class CarrinhoCompartilhado(
    val id: String = "",
    val ownerUserId: String = "",  // ID do dono do carrinho
    val sharedWithUserId: String = "",  // ID do usuário com quem foi compartilhado
    val sharedWithEmail: String = "",  // Email do usuário (para exibição)
    val timestamp: Long = System.currentTimeMillis()
)