import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
import com.example.loja.R
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFFAF5),
                        Color(0xFFFFF5EB)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(1.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(380.dp)
                    .offset(y = (-20).dp)
                    .padding(top = 1.dp),
                alignment = Alignment.Center,
                contentScale = ContentScale.Fit
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .offset(y = (-100).dp)
                    .padding(top = 1.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Criar Conta",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333)
                        ),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { registoViewModel.onEmailChange(it) },
                        label = { Text("Email") },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Email,
                                contentDescription = "Email",
                                tint = Color(0xFF666666)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Orange,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        )
                    )

                    var passwordVisible by remember { mutableStateOf(false) }
                    OutlinedTextField(
                        value = password,
                        onValueChange = { registoViewModel.onPasswordChange(it) },
                        label = { Text("Senha") },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Lock,
                                contentDescription = "Senha",
                                tint = Color(0xFF666666)
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Filled.Close else Icons.Filled.Info,
                                    contentDescription = if (passwordVisible) "Ocultar senha" else "Mostrar senha",
                                    tint = Color(0xFF666666)
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Orange,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        )
                    )

                    var confirmPasswordVisible by remember { mutableStateOf(false) }
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { registoViewModel.onConfirmPasswordChange(it) },
                        label = { Text("Confirmar Senha") },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Lock,
                                contentDescription = "Confirmar Senha",
                                tint = Color(0xFF666666)
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(
                                    if (confirmPasswordVisible) Icons.Filled.Close else Icons.Filled.Info,
                                    contentDescription = if (confirmPasswordVisible) "Ocultar senha" else "Mostrar senha",
                                    tint = Color(0xFF666666)
                                )
                            }
                        },
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Orange,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        )
                    )

                    if (errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            color = Color.Red,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    Button(
                        onClick = {
                            if (!isLoading) {
                                registoViewModel.viewModelScope.launch {
                                    registoViewModel.signUp(email, password, confirmPassword)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Orange),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                "Registar",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.clickable { navController.navigate("login") },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Já tem conta? ",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = Color(0xFF666666)
                            )
                        )
                        Text(
                            "Entrar",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = Orange,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}