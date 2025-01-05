package com.example.loja.classes

class User(
    val uid: Int,
    val nome: String,
    val email: String,
    val password: String,
    val carrinho: Carrinho = Carrinho()
)
