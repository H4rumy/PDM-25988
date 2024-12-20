package com.example.newslist.domain.model

data class News(
    val uuid: String,
    val title: String,
    val imageUrl: String?,
    val publishedAt: String?,
)
