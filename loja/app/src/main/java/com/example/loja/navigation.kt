import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.loja.view.LoginScreen
import com.example.loja.viewmodel.LoginViewModel
import com.example.loja.viewmodel.RegistoViewModel

@Composable
fun NavigationGraph(
    navController: NavHostController,
    registoViewModel: RegistoViewModel,
    loginViewModel: LoginViewModel
) {
    NavHost(navController = navController, startDestination = "register_screen") {
        // Tela de Registro
        composable("register_screen") {
            RegisterScreen(registoViewModel = registoViewModel, navController = navController)
        }

        // Tela de Login
        composable("login_screen") {
            LoginScreen(loginViewModel = loginViewModel, navController = navController)
        }
    }
}
