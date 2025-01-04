package com.example.newslist.presentation.newslist

import android.os.Build
import android.text.Html
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.newslist.domain.model.News
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.ZoneOffset

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewsListScreen(
    viewModel: NewsListViewModel,
    onNewsClick: (String) -> Unit
) {
    val newsList by viewModel.news.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        viewModel.fetchNews()
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAEEE3))
    ) {
        Text(
            text = "Notícias",
            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            textAlign = TextAlign.Center
        )

        if (newsList.isEmpty()){
            Box (contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()){
                Text("A carregar notícias...", fontSize = 32.sp)
            }
        }
        else {
            LazyColumn (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp, horizontal = 16.dp)
            ) {
                items(newsList) { news ->
                    NewsItem(news, onNewsClick)
                }
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewsItem(
    news: News,
    onNewsClick: (String) -> Unit
) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onNewsClick(news.uuid) },
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xEEE8C4A2)
        )
    ) {
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            AsyncImage(
                model = news.image_url,
                contentDescription = "News Image",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = news.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatDate(news.published_at ?: "2025-01-04T16:27:24.000000Z"),
                    style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                    fontSize = 14.sp
                )
            }
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