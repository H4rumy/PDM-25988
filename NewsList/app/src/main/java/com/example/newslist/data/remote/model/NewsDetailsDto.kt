package com.example.newslist.data.remote.model

import com.example.newslist.domain.model.NewsDetails

data class NewsDetailsDto(
    val uuid: String,
    val title: String,
    val description: String?,
    val url: String?,
    val imageUrl: String?,
    val publishedAt: String?,
    val source: String?,
    val categories: List<String>?
) {
    fun toNewsDetails(): NewsDetails {
        return NewsDetails(
            uuid = uuid,
            title = title,
            description = description,
            url = url,
            imageUrl = imageUrl,
            publishedAt = publishedAt,
            source = source,
            categories = categories
        )
    }
}
