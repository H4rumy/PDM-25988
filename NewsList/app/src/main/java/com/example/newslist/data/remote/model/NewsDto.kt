package com.example.newslist.data.remote.model

import com.example.newslist.domain.model.News

data class NewsDto(
    val uuid: String,
    val title: String,
    val image_url: String?,
    val published_at: String?,
) {
    fun toNews(): News {
        return News(
            uuid = uuid,
            title = title,
            image_url = image_url,
            published_at = published_at)
    }
}