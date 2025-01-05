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

    val email by registoViewModel.email.collectAsState()
    val password by registoViewModel.password.collectAsState()
    val confirmPassword by registoViewModel.confirmPassword.collectAsState()
    val errorMessage by registoViewModel.errorMessage.collectAsState()
    val isLoading by registoViewModel.isLoading.collectAsState()
    val isRegistrationSuccessful by registoViewModel.isRegistrationSuccessful.collectAsState()

    LaunchedEffect(isRegistrationSuccessful) {
        if (isRegistrationSuccessful) {
            navController.navigate("login") {
                popUpTo("registo") { inclusive = true }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize())
    {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(bottom = 16.dp)
            ){
            // Campo Email
                Text(
                    text = "Email",
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                CustomTextField(
                placeholder = "Email",
                value = email,
                onValueChange = { registoViewModel.onEmailChange(it) },
                )

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
                    placeholder = "Palavra-Passe",
                    value = password,
                    onValueChange = { registoViewModel.onPasswordChange(it) },
                    )
                }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Confirmar Palavra-passe
            CustomTextField(
                placeholder = "Confirmar Palavra-Passe",
                value = confirmPassword,
                onValueChange = { registoViewModel.onConfirmPasswordChange(it) },
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mensagem de erro
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // Botão de registo
            CustomButton(
                label = if (isLoading) "..." else "Registar",
                color = Orange,
                onClick = {
                    if (!isLoading) {
                        registoViewModel.viewModelScope.launch {
                            registoViewModel.signUp(email, password, confirmPassword)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(0.5f)
            )

            //Spacer(modifier = Modifier.height(8.dp))


        }}
    }
}