package com.example.newslist.domain.repository

import com.example.newslist.domain.model.News
import com.example.newslist.domain.model.NewsDetails

interface NewsRepository {
    suspend fun getNews(): List<News>
    suspend fun getNewsDetail(uuid: String, apiToken: String): NewsDetails
}