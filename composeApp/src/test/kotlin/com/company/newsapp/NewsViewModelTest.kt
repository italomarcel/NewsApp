package com.company.newsapp

import com.company.newsapp.models.Article
import com.company.newsapp.models.Source
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Unit tests for News App
 * Critical TechWorks Android Code Challenge requirement
 */
class NewsViewModelTest {

    @Test
    fun testArticleDateSorting() {
        // Test that articles are properly sorted by date
        val article1 = Article(
            source = Source("bbc", "BBC News"),
            author = "Test Author",
            title = "Old Article",
            description = "Description",
            url = "https://bbc.com/1",
            urlToImage = null,
            publishedAt = "2024-01-01T10:00:00Z",
            content = "Content"
        )
        
        val article2 = Article(
            source = Source("bbc", "BBC News"),
            author = "Test Author",
            title = "New Article",
            description = "Description", 
            url = "https://bbc.com/2",
            urlToImage = null,
            publishedAt = "2024-01-02T10:00:00Z",
            content = "Content"
        )
        
        val articles = listOf(article1, article2)
        val sortedArticles = articles.sortedByDescending { it.publishedDate }
        
        // Newer article should be first
        assertEquals("New Article", sortedArticles[0].title)
        assertEquals("Old Article", sortedArticles[1].title)
    }
    
    @Test
    fun testArticleIdGeneration() {
        val article = Article(
            source = Source("bbc", "BBC News"),
            author = "Test Author",
            title = "Test Article",
            description = "Description",
            url = "https://bbc.com/test",
            urlToImage = null,
            publishedAt = "2024-01-01T10:00:00Z",
            content = "Content"
        )
        
        // ID should be generated from source name and title hash
        assertTrue(article.id.contains("BBC News"))
        assertTrue(article.id.isNotEmpty())
    }
}