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
import com.example.loja.viewmodel.RegistoViewModel
import com.example.loja.viewmodel.RegistoState
import androidx.navigation.NavController
import androidx.navigation.NavHostController


@Composable
fun RegisterScreen(registoViewModel: RegistoViewModel, navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    val registoState by registoViewModel.registoState.collectAsState()

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Campos de entrada (email, senha, etc.)

        Button(onClick = {
            if (email.isNotEmpty() && senha.isNotEmpty() && senha.length >= 6) {
                registoViewModel.registar(email, senha)
            } else {
                Toast.makeText(context, "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Registar")
        }

        // Texto para navegar para a tela de login
        Spacer(modifier = Modifier.height(16.dp))
        ClickableText(
            text = buildAnnotatedString {
                append("Já tem uma conta? ")
                // Estilo do texto "Login"
                append("Login", TextStyle(color = Color.Blue).toString())
            },
            onClick = {
                // Navega para a tela de login
                navController.navigate("login_screen")
            }
        )
    }
}
