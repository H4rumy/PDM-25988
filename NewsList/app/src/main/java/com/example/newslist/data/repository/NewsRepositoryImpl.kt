package com.example.newslist.data.repository

import com.example.newslist.data.remote.api.NewsApi
import com.example.newslist.domain.model.News
import com.example.newslist.domain.model.NewsDetails
import com.example.newslist.domain.repository.NewsRepository

class NewsRepositoryImpl (private val api: NewsApi) :  NewsRepository {
    override suspend fun getNews(): List<News> {
        val response = api.getNews("Jbyll1BrCZPElS0TRiZ5KuFcNsw47p6fHYHJFO8M")
        return response.data.map { it.toNews() }
    }

    override suspend fun getNewsDetail(uuid: String, apiToken: String): NewsDetails {
        return api.getNewsDetails(uuid, apiToken).toNewsDetails()
    }
}