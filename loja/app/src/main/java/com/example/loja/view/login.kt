package com.example.loja.view

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.loja.viewmodel.LoginViewModel


@Composable
fun LoginScreen(loginViewModel: LoginViewModel, navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    val loginState by loginViewModel.loginState.collectAsState()

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Campos de entrada (email, senha, etc.)
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botão de login
        Button(onClick = {
            if (email.isNotEmpty() && senha.isNotEmpty()) {
                loginViewModel.login(email, senha)
            } else {
                Toast.makeText(context, "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Entrar")
        }

        // Exibe mensagem de erro ou sucesso após o login
        if (loginState != null) {
            Text("Bem-vindo, ${loginState?.email}")
        } else {
            Text("Erro no login.")
        }

        // Texto para navegar para a tela de registro
        Spacer(modifier = Modifier.height(16.dp))
        ClickableText(
            text = buildAnnotatedString {
                append("Não tem uma conta? ")
                // Estilo do texto "Registrar"
                append("Registrar", TextStyle(color = Color.Blue).toString())
            },
            onClick = {
                // Navega para a tela de registro
                navController.navigate("register_screen")
            }
        )
    }
}
