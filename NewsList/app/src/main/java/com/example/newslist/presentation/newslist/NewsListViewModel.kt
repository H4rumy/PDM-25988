package com.example.newslist.presentation.newslist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newslist.data.remote.api.RetrofitInstance
import com.example.newslist.data.repository.NewsRepositoryImpl
import com.example.newslist.domain.model.News
import com.example.newslist.domain.use_case.GetNewsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NewsListViewModel : ViewModel() {

    private val api = RetrofitInstance.api
    private val repository = NewsRepositoryImpl(api)
    private val getNewsUseCase = GetNewsUseCase(repository)

    private val _news = MutableStateFlow<List<News>>(emptyList())
    val news = _news

    fun fetchNews() {
        viewModelScope.launch {
            try {
                _news.value = getNewsUseCase()
                Log.d("NewsListViewModel", "Noticias carregadas: ${_news.value.size}")
            } catch (e: Exception) {
                Log.e("NewsListViewModel", "Erro ao carregar notícias: ${e.message}")
                _news.value = emptyList()
            }
        }
    }
}