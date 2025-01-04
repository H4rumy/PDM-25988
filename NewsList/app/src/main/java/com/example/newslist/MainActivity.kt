package com.example.newslist

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.newslist.presentation.newsdetails.NewsDetailsScreen
import com.example.newslist.presentation.newsdetails.NewsDetailsViewModel
import com.example.newslist.presentation.newslist.NewsListScreen
import com.example.newslist.presentation.newslist.NewsListViewModel
import com.example.newslist.ui.theme.NewsListTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen() {
    var selectedNewsUuid by remember { mutableStateOf<String?>(null) }

    if (selectedNewsUuid == null) {
        val newsListViewModel: NewsListViewModel = viewModel()
        NewsListScreen(newsListViewModel) { uuid ->
            selectedNewsUuid = uuid
        }
    } else {
        val newsDetailsViewModel: NewsDetailsViewModel = viewModel()
        NewsDetailsScreen(newsDetailsViewModel, selectedNewsUuid!!) {
            selectedNewsUuid = null
        }
    }
}