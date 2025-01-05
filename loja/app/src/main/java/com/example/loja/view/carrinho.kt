package com.example.loja.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.loja.classes.Produto
import com.example.loja.viewmodel.CarrinhoViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarrinhoScreen(navController: NavController) {
    val carrinhoViewModel: CarrinhoViewModel = viewModel()
    val produtosCarrinho = carrinhoViewModel.produtosCarrinho.collectAsState(initial = emptyList()).value

    // Obtenha o ID do usuário logado usando o Firebase Authentication (ou outra fonte)
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    // Verificar se há um usuário logado
    if (userId != null) {
        // Carregar os produtos do carrinho do usuário logado
        LaunchedEffect(userId) {
            carrinhoViewModel.carregarProdutosCarrinho(userId)
        }
    } else {
        // Lidar com o caso onde não há um usuário logado
        // Você pode exibir uma mensagem ou navegar para a tela de login
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrinho") },
                actions = {
                    IconButton(onClick = {
                        // Navegar para a página de checkout ou outra ação
                        navController.navigate("checkout") // Substitua "checkout" pela rota desejada
                    }) {
                        Icon(imageVector = Icons.Filled.Check, contentDescription = "Finalizar Compra")
                    }
                }
            )
        },
        content = { paddingValues ->
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                if (produtosCarrinho.isEmpty()) {
                    Text(
                        text = "Nenhum produto no carrinho.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(produtosCarrinho) { produto ->
                            ProdutoCardCarrinho(navController = navController, produto = produto) // Passando 'produto' e 'navController'
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun ProdutoCardCarrinho(navController: NavController, produto: Produto) {  // Agora, recebe 'navController' e 'produto'
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = produto.nome,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Preço: ${produto.preco} €",
                style = MaterialTheme.typography.bodyMedium
            )
            // Adicione outras interações ou botões, como "Remover do carrinho"
        }
    }
}
