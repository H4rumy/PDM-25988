package com.example.newslist.domain.use_case

import com.example.newslist.domain.model.NewsDetails
import com.example.newslist.domain.repository.NewsRepository

class GetNewsDetailsUseCase (private val repository: NewsRepository) {
    suspend operator fun invoke(uuid: String, apiToken: String): NewsDetails {
        return repository.getNewsDetail(uuid, apiToken)
    }
}