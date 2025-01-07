package com.example.loja.view

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.loja.Componentes.Orange
import com.example.loja.Navigation.Routes
import com.example.loja.classes.Produto
import com.example.loja.viewmodel.CarrinhoViewModel
import com.example.loja.viewmodel.LoginViewModel
import com.example.loja.viewmodel.ProdutosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProdutosScreen(navController: NavController) {
    val carrinhoViewModel: CarrinhoViewModel = viewModel()
    val produtosViewModel: ProdutosViewModel = viewModel()
    val produtos by produtosViewModel.produtos.collectAsState()
    val errorMessage by produtosViewModel.errorMessage.collectAsState()
    val carrinhoCount by carrinhoViewModel.carrinhoCount.collectAsState()

    val loginViewModel: LoginViewModel = viewModel()

    LaunchedEffect(Unit) {
        produtosViewModel.carregarProdutos()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Produtos",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333)
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF333333)
                ),
                actions = {
                    IconButton(
                        onClick = { navController.navigate(Routes.CARRINHOSRECEBIDOS) }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Carrinhos Partilhados"
                        )
                    }
                    BadgedBox(
                        badge = {
                            if (carrinhoCount > 0) {
                                Badge {
                                    Text(
                                        carrinhoCount.toString(),
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    ) {
                        IconButton(onClick = { navController.navigate(Routes.CARRINHO) }) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Carrinho",
                                tint = Orange
                            )
                        }
                    }
                    IconButton(
                        onClick = {
                            loginViewModel.signOut()
                            navController.navigate(Routes.LOGIN) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Terminar Sessão"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFFFAF5),
                            Color(0xFFFFF5EB)
                        )
                    )
                )
                .padding(paddingValues)
        ) {
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(produtos) { produto ->
                        ProdutoCard(produto, carrinhoViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun ProdutoCard(produto: Produto, carrinhoViewModel: CarrinhoViewModel) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var quantidade by remember { mutableStateOf(1) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Imagem do produto
            AsyncImage(
                model = produto.imagemUrl,
                contentDescription = produto.nome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Nome do produto
                Text(
                    text = produto.nome,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Preço do produto
                Text(
                    text = String.format("%.2f €", produto.preco),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Orange,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Botão de adicionar ao carrinho
                Button(
                    onClick = { showDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Orange),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Adicionar ao carrinho",
                        modifier = Modifier.size(16.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Adicionar",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }
        }
    }
    // Diálogo de seleção de quantidade
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                quantidade = 1
            },
            title = {
                Text(
                    "Selecionar Quantidade",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        produto.nome,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = { if (quantidade > 1) quantidade-- },
                            modifier = Modifier
                                .size(40.dp)
                                .background(Orange.copy(alpha = 0.1f), CircleShape)
                        ) {
                            Text(
                                text = "−", // usando o símbolo matemático de menos
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Orange
                            )
                        }

                        Text(
                            quantidade.toString(),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        IconButton(
                            onClick = { quantidade++ },
                            modifier = Modifier
                                .size(40.dp)
                                .background(Orange.copy(alpha = 0.1f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Aumentar",
                                tint = Orange
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        carrinhoViewModel.adicionarAoCarrinho(produto, quantidade)
                        Toast.makeText(
                            context,
                            "$quantidade ${if (quantidade == 1) "unidade" else "unidades"} adicionadas ao carrinho!",
                            Toast.LENGTH_SHORT
                        ).show()
                        showDialog = false
                        quantidade = 1
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Orange)
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        showDialog = false
                        quantidade = 1
                    },
                    border = BorderStroke(1.dp, Orange)
                ) {
                    Text("Cancelar", color = Orange)
                }
            }
        )
    }
}
