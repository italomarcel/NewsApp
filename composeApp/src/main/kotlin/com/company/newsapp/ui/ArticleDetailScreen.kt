package com.company.newsapp.ui

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun ArticleDetailScreen(
    article: com.company.newsapp.models.Article?,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    article?.let {
        ArticleContent(
            article = it,
            onBackClick = onBackClick,
            onReadFullArticle = {
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, it.url.toUri())
                )
            }
        )
    } ?: EmptyState(onBackClick = onBackClick)
}

@Composable
private fun EmptyState(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Article not available",
                style = MaterialTheme.typography.headlineSmall
            )
            Button(onClick = onBackClick) {
                Text("Go Back")
            }
        }
    }
}

@Composable
private fun ArticleImage(imageUrl: String?, contentDescription: String) {
    imageUrl?.let {
        AsyncImage(
            model = it,
            contentDescription = contentDescription,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun ArticleTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@OptIn(ExperimentalTime::class)
@Composable
private fun ArticleMetadata(article: com.company.newsapp.models.Article) {
    val date = article.publishedDate.toLocalDateTime(TimeZone.currentSystemDefault())
    val formattedTime = "%02d:%02d".format(date.time.hour, date.time.minute)

    val metadataText = buildString {
        article.author?.let { append("By $it • ") }
        append("${date.date} $formattedTime")
    }

    Text(
        text = metadataText,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun ArticleDescription(description: String) {
    Text(
        text = description,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontWeight = FontWeight.Medium
    )
}

@Composable
private fun ArticleContent(content: String) {
    Text(
        text = content,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface,
        lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.2
    )
}

@Composable
private fun ReadFullArticleCard(
    sourceName: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Source: $sourceName",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Read full article at original source",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
private fun ArticleContent(
    article: com.company.newsapp.models.Article,
    onBackClick: () -> Unit,
    onReadFullArticle: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = article.source.name,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ArticleImage(article.urlToImage, article.title)
            }

            item {
                ArticleTitle(article.title)
            }

            item {
                ArticleMetadata(article)
            }

            article.description?.takeIf { it.isNotBlank() }?.let { description ->
                item {
                    ArticleDescription(description)
                }
            }

            article.content?.let { content ->
                val cleanContent = content
                    .replace(Regex("\\[\\+\\d+\\s+chars]"), "")
                    .trim()
                    .takeIf { it.isNotBlank() }

                cleanContent?.let {
                    item {
                        ArticleContent(it)
                    }
                }
            }

            item {
                ReadFullArticleCard(
                    sourceName = article.source.name,
                    onClick = onReadFullArticle
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

