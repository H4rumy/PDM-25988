package com.example.loja.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.loja.Navigation.Routes
import com.example.loja.classes.Produto
import com.example.loja.viewmodel.ProdutosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProdutosScreen(navController: NavController) {
    val produtosViewModel: ProdutosViewModel = viewModel()

    val produtos by produtosViewModel.produtos.collectAsState()
    val errorMessage by produtosViewModel.errorMessage.collectAsState()

    // Carregar os produtos ao abrir a tela
    LaunchedEffect(Unit) {
        produtosViewModel.carregarProdutos()
    }

    // Estado para o carrinho (exemplo de lista local)
    var carrinho by remember { mutableStateOf<List<Produto>>(emptyList()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Produtos") },
                actions = {
                    IconButton(onClick = { navController.navigate(Routes.CARRINHO) }) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "Adicionar Produto")
                    }
                },
            )
        },
        content = { paddingValues ->
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) { // Aplicando o contentPadding aqui
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
                            ProdutoCard(produto, onAddToCart = {
                                // Adicionar produto à lista de carrinho
                                carrinho = carrinho + produto
                            })
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun ProdutoCard(produto: Produto, onAddToCart: (Produto) -> Unit) {
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

            // Botão de adicionar ao carrinho
            Button(
                onClick = { onAddToCart(produto) },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Adicionar ao Carrinho")
            }
        }
    }
}
