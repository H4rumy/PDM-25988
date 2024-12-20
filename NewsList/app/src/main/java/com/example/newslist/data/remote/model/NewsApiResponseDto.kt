package com.example.newslist.data.remote.model

import com.example.newslist.domain.model.News

data class NewsApiResponseDto(

    val meta: Meta,
    val data: List<NewsDto>
)

data class Meta(
    val found: Int,
    val returned: Int,
    val limit: Int,
    val page: Int
)

