package com.example.newslist.presentation.newslist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.newslist.domain.model.News

@Composable
fun NewsListScreen(
    viewModel: NewsListViewModel,
    onNewsClick: (String) -> Unit
) {
    val newsList by viewModel.news.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        viewModel.fetchNews()
    }

    if (newsList.isEmpty()){
        Text("A carregar notícias...", modifier = Modifier.fillMaxSize())
    }
    else {
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            items(newsList) { news ->
                NewsItem(news, onNewsClick)
            }
        }
    }
}

@Composable
fun NewsItem(
    news: News,
    onNewsClick: (String) -> Unit
) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onNewsClick(news.uuid) }
    ) {
        Text(
            text = news.title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = news.publishedAt ?: "Date not available",
            style = androidx.compose.material3.MaterialTheme.typography.bodySmall
        )
    }
}