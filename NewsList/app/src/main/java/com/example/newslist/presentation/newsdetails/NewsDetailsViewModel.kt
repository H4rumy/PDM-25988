package com.example.newslist.presentation.newsdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newslist.data.remote.api.RetrofitInstance
import com.example.newslist.data.repository.NewsRepositoryImpl
import com.example.newslist.domain.model.NewsDetails
import com.example.newslist.domain.use_case.GetNewsDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NewsDetailsViewModel : ViewModel() {

    private val api = RetrofitInstance.api
    private val repository = NewsRepositoryImpl(api)
    private val getNewsDetailsUseCase = GetNewsDetailsUseCase(repository)

    val newsDetails = MutableStateFlow<NewsDetails?>(null)
    val isLoading = MutableStateFlow(false)

    fun fetchNewsDetails(uuid: String) {
        viewModelScope.launch {
            try {
                isLoading.value = true
                val apiToken = "XPEV1RnPRZD7aj5p9bN1vAZQ463KNqgcGn40x5nO"
                newsDetails.value = getNewsDetailsUseCase(uuid, apiToken)
            } catch (e: Exception) {
                newsDetails.value = null
            } finally {
                isLoading.value = false
            }
        }
    }
}