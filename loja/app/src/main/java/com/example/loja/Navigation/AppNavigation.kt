package com.example.loja.Navigation

import RegistoScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.loja.view.CarrinhoScreen
import com.example.loja.view.LoginScreen
import com.example.loja.view.ProdutosScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String) {

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Routes.LOGIN) {
            LoginScreen(navController = navController)
        }
        composable(Routes.REGISTO)  {
            RegistoScreen(navController = navController)
        }
        composable(Routes.PRODUTOS)  {
            ProdutosScreen(navController = navController)
        }
        composable(Routes.CARRINHO)  {
            CarrinhoScreen(navController = navController)
        }

    }
}