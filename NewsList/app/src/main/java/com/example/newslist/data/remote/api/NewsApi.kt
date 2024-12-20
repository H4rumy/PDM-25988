package com.example.newslist.data.remote.api

import android.util.Log
import com.example.newslist.data.remote.model.NewsApiResponseDto
import com.example.newslist.data.remote.model.NewsDetailsDto
import com.example.newslist.data.remote.model.NewsDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsApi {
    @GET("news/all")
    suspend fun getNews(
        @Query("api_token") apiToken: String,
        @Query("language") language: String = "en",
        @Query("limit") limit: Int = 3
    ): NewsApiResponseDto

    @GET("news/uuid/{uuid}")
    suspend fun getNewsDetails(
        @Path("uuid") uuid: String,
        @Query("api_token") apiToken: String
    ): NewsDetailsDto
}