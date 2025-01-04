package com.example.newslist.data.repository

import com.example.newslist.data.remote.api.NewsApi
import com.example.newslist.domain.model.News
import com.example.newslist.domain.model.NewsDetails
import com.example.newslist.domain.repository.NewsRepository

class NewsRepositoryImpl (private val api: NewsApi) :  NewsRepository {
    override suspend fun getNews(): List<News> {
        val response = api.getNews("XPEV1RnPRZD7aj5p9bN1vAZQ463KNqgcGn40x5nO")
        return response.data.map { it.toNews() }
    }

    override suspend fun getNewsDetail(uuid: String, apiToken: String): NewsDetails {
        return api.getNewsDetails(uuid, apiToken).toNewsDetails()
    }
}