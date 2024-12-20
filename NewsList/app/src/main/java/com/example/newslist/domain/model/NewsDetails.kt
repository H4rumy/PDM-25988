package com.example.newslist.domain.model

data class NewsDetails(
    val uuid: String,
    val title: String,
    val description: String?,
    val url: String?,
    val imageUrl: String?,
    val publishedAt: String?,
    val source: String?,
    val categories: List<String>?
)
