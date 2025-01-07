package com.example.loja.Navigation

import RegistoScreen
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.loja.view.CarrinhoScreen
import com.example.loja.view.CompartilharCarrinhoScreen
import com.example.loja.view.LoginScreen
import com.example.loja.view.ProdutosScreen
import com.example.loja.viewmodel.LoginViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.LOGIN
) {
    val context = LocalContext.current
    val loginViewModel: LoginViewModel = viewModel()

    val startDestination = if (loginViewModel.isUserLoggedIn()) Routes.PRODUTOS else Routes.LOGIN

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(navController = navController, viewModel = loginViewModel)
        }
        composable(Routes.REGISTO) {
            RegistoScreen(navController = navController)
        }
        composable(Routes.PRODUTOS) {
            ProdutosScreen(navController = navController)
        }
        composable(Routes.CARRINHO) {
            CarrinhoScreen(navController = navController)
        }
        composable(Routes.PARTILHARCARRINHO) {
            CompartilharCarrinhoScreen(navController = navController)
        }
        composable(Routes.CARRINHOSRECEBIDOS) {
            com.example.loja.view.CarrinhosRecebidosScreen(navController = navController)
        }

    }
}