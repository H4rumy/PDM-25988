package com.example.newslist.domain.model

import com.example.newslist.data.remote.model.NewsDto

data class NewsApiResponse(
    val meta: Meta,
    val data: List<News>
)

data class Meta(
    val found: Int,
    val returned: Int,
    val limit: Int,
    val page: Int
)
