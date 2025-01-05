package com.example.loja.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.loja.classes.Produto
import com.example.loja.viewmodel.CarrinhoViewModel
import androidx.compose.runtime.collectAsState

@Composable
fun ProdutoCardCarrinho(navController: NavController, produto: Produto, carrinhoViewModel: CarrinhoViewModel) {
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

            Button(
                onClick = {
                    // Adiciona o produto ao carrinho ao clicar no botão
                    carrinhoViewModel.adicionarProdutoAoCarrinho(produto)
                }
            ) {
                Text("Adicionar ao carrinho")
            }
        }
    }
}
