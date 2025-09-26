package com.company.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.newsapp.BuildConfig
import com.company.newsapp.di.AppModule
import com.company.newsapp.models.NewsUiState
import com.company.newsapp.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewsViewModel(
    private val repository: NewsRepository = AppModule.provideNewsRepository(
        AppModule.provideNewsApiService(AppModule.provideOkHttpClient())
    )
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(NewsUiState())
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()
    
    fun loadBBCHeadlines() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            repository.getBBCHeadlines(BuildConfig.NEWS_API_KEY)
                .fold(
                    onSuccess = { articles ->
                        _uiState.value = NewsUiState(articles = articles, isLoading = false)
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = exception.message ?: "Failed to load news"
                        )
                    }
                )
        }
    }
    
    fun refresh() = loadBBCHeadlines()
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}