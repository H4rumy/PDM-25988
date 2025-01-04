package com.example.newslist.domain.model

data class News(
    val uuid: String,
    val title: String,
    val image_url: String?,
    val published_at: String?,
)
