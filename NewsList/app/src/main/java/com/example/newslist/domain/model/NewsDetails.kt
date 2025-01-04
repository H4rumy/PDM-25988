package com.example.newslist.domain.model

data class NewsDetails(
    val uuid: String,
    val title: String,
    val description: String?,
    val url: String?,
    val image_url: String?,
    val published_at: String?,
    val source: String?,
    val categories: List<String>?
)
