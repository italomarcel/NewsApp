package com.company.newsapp.api

import com.company.newsapp.models.NewsApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("top-headlines")
    suspend fun getBBCHeadlines(
        @Query("sources") sources: String = "bbc-news",
        @Query("apiKey") apiKey: String,
        @Query("pageSize") pageSize: Int = 20
    ): NewsApiResponse

    @GET("top-headlines")
    suspend fun getHeadlinesBySource(
        @Query("sources") sources: String,
        @Query("apiKey") apiKey: String,
        @Query("pageSize") pageSize: Int = 20
    ): NewsApiResponse
}