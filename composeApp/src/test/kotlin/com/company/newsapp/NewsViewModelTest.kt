package com.company.newsapp

import com.company.newsapp.models.Article
import com.company.newsapp.models.Source
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

import kotlin.time.ExperimentalTime


class NewsViewModelTest {

    @OptIn(ExperimentalTime::class)
    @Test
    fun testArticleDateSorting() {
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
        
        assertTrue("ID should contain source name", article.id.contains("bbc-news"))
        assertTrue("ID should contain URL part", article.id.contains("test"))
        assertTrue("ID should not be empty", article.id.isNotEmpty())
        assertEquals("ID should have expected format", "bbc-news-test-${article.url.hashCode().toString().replace("-", "0")}", article.id)
    }
}