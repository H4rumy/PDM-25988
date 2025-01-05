import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.loja.viewmodel.RegistoViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.loja.Componentes.CustomButton
import com.example.loja.Componentes.CustomTextField
import com.example.loja.Componentes.Orange
import com.example.loja.viewmodel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun RegistoScreen(navController: NavController) {

    val registoViewModel: RegistoViewModel = viewModel()
    val context = LocalContext.current

    // State to hold the text input for each field
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // Column to organize TextFields, Button
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
                // Nome
                Text(
                    text = "Nome *",
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                CustomTextField(
                    placeholder = "Digite o nome",
                    value = nome,
                    onValueChange = { nome = it }
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Email
                Text(
                    text = "Email *",
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                CustomTextField(
                    placeholder = "Digite o email",
                    value = email,
                    onValueChange = { email = it }
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Senha
                Text(
                    text = "Senha *",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                CustomTextField(
                    placeholder = "Digite a senha",
                    value = password,
                    onValueChange = { password = it }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            CustomButton(
                label = "Registar",
                color = Orange,
                modifier = Modifier.fillMaxWidth(0.50f),
                onClick = {
                    if (nome.isEmpty() || email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Por favor, preencha todos os campos obrigatórios.",
                            Toast.LENGTH_LONG
                        ).show()
                        return@CustomButton
                    }

                }
            )
        }
    }
}

