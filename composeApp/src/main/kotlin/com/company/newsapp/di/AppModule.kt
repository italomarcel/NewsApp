package com.company.newsapp.di

import com.company.newsapp.BuildConfig
import com.company.newsapp.api.NewsApiService
import com.company.newsapp.repository.NewsRepository
import com.company.newsapp.repository.NewsRepositoryImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppModule {
    
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
            .build()
    }
    
    fun provideNewsApiService(okHttpClient: OkHttpClient): NewsApiService {
        return Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)
    }
    
    fun provideNewsRepository(apiService: NewsApiService): NewsRepository {
        return NewsRepositoryImpl(apiService)
    }
}