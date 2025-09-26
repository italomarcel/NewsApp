package com.company.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.newsapp.BuildConfig
import com.company.newsapp.api.NewsApiService
import com.company.newsapp.models.Article
import com.company.newsapp.models.NewsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.time.ExperimentalTime

class NewsViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(NewsUiState())
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()
    
    private var cachedArticles: List<Article> = emptyList()
    
    private val apiService: NewsApiService by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        
        Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)
    }
    
    @OptIn(ExperimentalTime::class)
    fun loadBBCHeadlines() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                val response = apiService.getBBCHeadlines(apiKey = BuildConfig.NEWS_API_KEY)
                val sortedArticles = response.articles.sortedByDescending { it.publishedDate }
                
                cachedArticles = sortedArticles
                _uiState.value = NewsUiState(
                    articles = sortedArticles,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load news"
                )
            }
        }
    }
    
    @OptIn(ExperimentalTime::class)
    fun loadHeadlines(source: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                val response = apiService.getHeadlinesBySource(sources = source, apiKey = BuildConfig.NEWS_API_KEY)
                val sortedArticles = response.articles.sortedByDescending { it.publishedDate }
                
                cachedArticles = sortedArticles  
                _uiState.value = NewsUiState(
                    articles = sortedArticles,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load news"
                )
            }
        }
    }
    
    fun refresh() {
        loadBBCHeadlines()
    }
    
    fun getArticleById(articleId: String): Article? {
        return cachedArticles.find { it.id == articleId }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}