package com.example.newslist.data.remote.model

import com.example.newslist.domain.model.News

data class NewsDto(
    val uuid: String,
    val title: String,
    val imageUrl: String?,
    val publishedAt: String?,
) {
    fun toNews(): News {
        return News(
            uuid = uuid,
            title = title,
            imageUrl = imageUrl,
            publishedAt = publishedAt)
    }
}