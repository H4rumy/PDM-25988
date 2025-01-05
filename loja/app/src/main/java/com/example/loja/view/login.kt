package com.example.loja.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.loja.Componentes.CustomButton
import com.example.loja.Componentes.CustomTextField
import com.example.loja.Navigation.Routes.PRODUTOS
import com.example.loja.Componentes.Orange
import com.example.loja.Navigation.Routes
import com.example.loja.viewmodel.LoginViewModel
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(navController: NavController) {

    val loginViewModel: LoginViewModel = viewModel()

    val email by loginViewModel.email.collectAsState()
    val password by loginViewModel.password.collectAsState()
    val errorMessage by loginViewModel.errorMessage.collectAsState()
    val isLoading by loginViewModel.isLoading.collectAsState()
    val isLoginSuccessful by loginViewModel.isLoginSuccessful.collectAsState()

    LaunchedEffect(isLoginSuccessful) {
        if (isLoginSuccessful) {
            navController.navigate(PRODUTOS) {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = "Email",
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                CustomTextField(
                    placeholder = "Escreva aqui...",
                    value = email,
                    onValueChange = { loginViewModel.onEmailChange(it) }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(bottom = 32.dp)
            ) {
                Text(
                    text = "Palavra-Passe",
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                CustomTextField(
                    placeholder = "Escreva aqui...",
                    value = password,
                    onValueChange = { loginViewModel.onPasswordChange(it) }
                )

            }

            // Mensagem de erro, se houver
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // Botão de login
            CustomButton(
                label = if (isLoading) "..." else "Entrar",
                color = Orange,
                onClick = {
                    if (!isLoading) {
                        // Lança a corrotina para chamar a função suspensa
                        loginViewModel.viewModelScope.launch {
                            loginViewModel.signIn(email, password)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(0.32f)
            )
            // caminho para registoScreen
            CustomButton(
                label = "Registar",
                color = Orange,
                onClick = {navController.navigate(Routes.REGISTO)}, // navigate to login screen
                modifier = Modifier.fillMaxWidth(0.32f)
            )

        }
    }
}
