package com.example.newslist.presentation.newsdetails

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewsDetailsScreen(
    viewModel: NewsDetailsViewModel,
    newsUuid: String,
    onBack: () -> Unit
) {
    val newsDetails by viewModel.newsDetails.collectAsState(initial = null)
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(newsUuid) {
        viewModel.fetchNewsDetails(newsUuid)
    }

    // Mudar o fundo de toda a tela
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFFAEEE3)) // Cor de fundo para toda a tela
            .padding(16.dp)
    ) {
        if (isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.size(16.dp))
                CircularProgressIndicator()
            }
        } else if (newsDetails != null) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = newsDetails!!.title,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp, top = 32.dp)
                )

                // Imagem
                newsDetails!!.image_url?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .padding(bottom = 16.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                // Descrição
                newsDetails!!.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                // Outros Detalhes (formatados como tabela)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    DetailRow(label = "Source", value = newsDetails!!.source)
                    DetailRow(label = "Published At", value = newsDetails!!.published_at?.let {
                        formatDate(
                            it
                        )
                    })
                    DetailRow(
                        label = "Categories",
                        value = newsDetails!!.categories?.joinToString(", ") { it.replaceFirstChar { char -> char.uppercase() } }
                    )
                    DetailRow(label = "URL", value = newsDetails!!.url)
                }

                Spacer(modifier = Modifier.weight(1f))

                // Botão Voltar
                Button(
                    onClick = { onBack() },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xEEE8C4A2),
                        contentColor = Color.Black
                    ),
                ) {
                    Text("Voltar")
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("News details not available.")
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String?) {
    if (!value.isNullOrEmpty()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$label:",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(apiDate: String): String {
    // Parsea a data do formato ISO 8601
    val parsedDate = LocalDateTime.parse(apiDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"))
    // Formata a data para o formato desejado
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    return parsedDate.atOffset(ZoneOffset.UTC).format(formatter)
}