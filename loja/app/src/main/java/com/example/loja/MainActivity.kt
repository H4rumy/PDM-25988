package com.example.loja

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.loja.repository.AuthRepository
import com.example.loja.viewmodel.RegistoViewModel
import com.example.loja.view.RegisterScreen
import com.example.loja.view.LoginScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Criação do AuthRepository
        val authRepository = AuthRepository()

        // ViewModel
        val registoViewModel = RegistoViewModel(authRepository)

        setContent {
            // Criar o NavController
            val navController = rememberNavController()

            // Definir o NavHost para gerenciar a navegação entre as telas
            NavHost(navController = navController, startDestination = "register_screen") {
                // Definir as rotas (telas) de navegação
                composable("register_screen") {
                    RegisterScreen(registoViewModel = registoViewModel, navController = navController)
                }
                composable("login_screen") {
                    LoginScreen(navController = navController)
                }
            }
        }
    }
}
