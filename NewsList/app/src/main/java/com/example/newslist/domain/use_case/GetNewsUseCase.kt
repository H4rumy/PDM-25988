package com.example.newslist.domain.use_case

import com.example.newslist.domain.model.News
import com.example.newslist.domain.repository.NewsRepository

class GetNewsUseCase (private val repository: NewsRepository) {
    suspend operator fun invoke(): List<News> {
        return  repository.getNews()
    }
}