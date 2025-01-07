package com.example.loja.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.loja.Componentes.Orange
import com.example.loja.Navigation.Routes
import com.example.loja.classes.CarrinhoItem
import com.example.loja.classes.Produto
import com.example.loja.viewmodel.CarrinhoViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarrinhoScreen(navController: NavController, userId: String? = null) {
    val carrinhoViewModel: CarrinhoViewModel = viewModel()
    val itensCarrinho = carrinhoViewModel.itensCarrinho.collectAsState(initial = emptyList()).value
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    val ownerEmail = remember { mutableStateOf("") }

    LaunchedEffect(userId) {
        if (userId != null) {
            carrinhoViewModel.carregarCarrinhoDeOutroUser(userId)
            carrinhoViewModel.buscarEmailDoUser(userId) { email ->
                ownerEmail.value = email
            }
        } else {
            carrinhoViewModel.carregarProdutosCarrinho(uid.toString())
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (userId != null) "Carrinho de ${ownerEmail.value}" else "Meu Carrinho",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333)
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color(0xFF333333)
                        )
                    }
                },
                actions = {
                    // Botão de compartilhar
                    IconButton(onClick = { navController.navigate(Routes.PARTILHARCARRINHO) }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Compartilhar Carrinho",
                            tint = Orange
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF333333)
                )
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
            if (itensCarrinho.isEmpty()){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "O seu carrinho está vazio",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333)
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Adicione produtos para começar suas compras",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF666666)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { navController.navigate(Routes.PRODUTOS) },
                        colors = ButtonDefaults.buttonColors(containerColor = Orange)
                    ) {
                        Text("Ver Produtos")
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Lista de produtos
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(itensCarrinho) { item ->
                            ItemCarrinhoCard(item = item, carrinhoViewModel = carrinhoViewModel)
                        }
                    }

                    // Resumo do carrinho
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                        ) {
                            // Subtotal
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Subtotal",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                    "€${String.format("%.2f", carrinhoViewModel.calcularTotal())}",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Custos de envio
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Custos de envio estimados",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    "€0,00",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                color = Color(0xFFE0E0E0)
                            )

                            // Total
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Total",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    "€${String.format("%.2f", carrinhoViewModel.calcularTotal())}",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Orange
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Botão Comprar
                            Button(
                                onClick = { /* Implementar checkout */ },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Orange),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    "COMPRAR",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun ItemCarrinhoCard(item: CarrinhoItem, carrinhoViewModel: CarrinhoViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Imagem do produto
            AsyncImage(
                model = item.imagemUrl,
                contentDescription = item.nome,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.nome,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "€${item.preco}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Orange,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Controles de quantidade
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = {
                            carrinhoViewModel.atualizarQuantidade(item.id, item.quantidade - 1)
                        },
                        modifier = Modifier
                            .size(32.dp)
                            .background(Orange.copy(alpha = 0.1f), CircleShape)
                    ) {
                        Text(
                            "−",
                            color = Orange,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Text(
                        item.quantidade.toString(),
                        style = MaterialTheme.typography.bodyLarge
                    )

                    IconButton(
                        onClick = {
                            carrinhoViewModel.atualizarQuantidade(item.id, item.quantidade + 1)
                        },
                        modifier = Modifier
                            .size(32.dp)
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
        }
    }
}

private fun calcularSubtotal(produtos: List<Produto>): String {
    return "%.2f".format(produtos.sumOf { it.preco.toDouble() })
}