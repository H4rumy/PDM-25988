package com.example.loja.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.loja.classes.Produto
import com.example.loja.viewmodel.ProdutosViewModel

@Composable
fun ProdutosScreen(navController: NavController) {
    val produtosViewModel: ProdutosViewModel = viewModel()

    val produtos by produtosViewModel.produtos.collectAsState()
    val errorMessage by produtosViewModel.errorMessage.collectAsState()

    // Carregar os produtos ao abrir a tela
    LaunchedEffect(Unit) {
        produtosViewModel.carregarProdutos()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (produtos.isEmpty()) {
            Text(
                text = "Nenhum produto disponível.",
                modifier = Modifier.align(Alignment.Center)
            )

        } else {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(produtos) { produto ->
                    ProdutoCard(produto)
                }
            }
        }
    }
}

@Composable
fun ProdutoCard(produto: Produto) {
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
                text = "Preço:${produto.preco} €",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}