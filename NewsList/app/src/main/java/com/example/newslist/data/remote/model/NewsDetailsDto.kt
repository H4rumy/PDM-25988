package com.example.newslist.data.remote.model

import com.example.newslist.domain.model.NewsDetails

data class NewsDetailsDto(
    val uuid: String,
    val title: String,
    val description: String?,
    val url: String?,
    val image_url: String?,
    val published_at: String?,
    val source: String?,
    val categories: List<String>?
) {
    fun toNewsDetails(): NewsDetails {
        return NewsDetails(
            uuid = uuid,
            title = title,
            description = description,
            url = url,
            image_url = image_url,
            published_at = published_at,
            source = source,
            categories = categories
        )
    }
}
