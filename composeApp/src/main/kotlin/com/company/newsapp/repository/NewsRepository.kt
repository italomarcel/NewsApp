package com.company.newsapp.repository

import com.company.newsapp.api.NewsApiService
import com.company.newsapp.models.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.time.ExperimentalTime

interface NewsRepository {
    suspend fun getBBCHeadlines(apiKey: String): Result<List<Article>>
    suspend fun getHeadlinesBySource(source: String, apiKey: String): Result<List<Article>>
}

class NewsRepositoryImpl(
    private val apiService: NewsApiService
) : NewsRepository {
    
    @OptIn(ExperimentalTime::class)
    override suspend fun getBBCHeadlines(apiKey: String): Result<List<Article>> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val response = apiService.getBBCHeadlines(apiKey = apiKey)
                response.articles.sortedByDescending { it.publishedDate }
            }
        }
    }
    
    @OptIn(ExperimentalTime::class)
    override suspend fun getHeadlinesBySource(source: String, apiKey: String): Result<List<Article>> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val response = apiService.getHeadlinesBySource(sources = source, apiKey = apiKey)
                response.articles.sortedByDescending { it.publishedDate }
            }
        }
    }
}