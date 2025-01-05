package com.example.loja

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.loja.Navigation.AppNavigation
import com.example.loja.Navigation.Routes
import com.example.loja.repository.AuthRepository
import com.example.loja.viewmodel.RegistoViewModel
import com.example.loja.view.LoginScreen
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val auth = FirebaseAuth.getInstance()

        setContent {
            val isLoggedIn = remember { mutableStateOf(auth.currentUser != null ) }
            AppNavigation(startDestination = if (isLoggedIn.value) Routes.LOGIN else Routes.LOGIN)
        }
    }
}
