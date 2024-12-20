package com.example.newslist.presentation.newsdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun NewsDetailsScreen(
    viewModel: NewsDetailsViewModel,
    newsuuid: String,
    onBack: () -> Unit
) {
    val newsDetails by viewModel.newsDetails.collectAsState(initial = null)
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect (newsuuid) {
        viewModel.fetchNewsDetails(newsuuid)
    }

    if (isLoading) {
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.size(16.dp))
            CircularProgressIndicator()
        }
    } else if (newsDetails != null) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = newsDetails!!.title,
                style = androidx.compose.material3.MaterialTheme.typography.headlineMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    } else {
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("News details not available.")
        }
    }
}