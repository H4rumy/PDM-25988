package com.example.newslist.data.remote.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: NewsApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thenewsapi.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)
    }
}