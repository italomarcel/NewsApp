package com.company.newsapp.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.datetime.Instant
import kotlinx.parcelize.Parcelize
import kotlin.time.ExperimentalTime

data class NewsApiResponse(
    @SerializedName("status") val status: String,
    @SerializedName("totalResults") val totalResults: Int,
    @SerializedName("articles") val articles: List<Article>
)

@Parcelize
data class Article(
    @SerializedName("source") val source: Source,
    @SerializedName("author") val author: String?,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("url") val url: String,
    @SerializedName("urlToImage") val urlToImage: String?,
    @SerializedName("publishedAt") val publishedAt: String,
    @SerializedName("content") val content: String?
) : Parcelable {
    @OptIn(ExperimentalTime::class)
    val publishedDate: Instant
        get() = try {
            Instant.parse(publishedAt)
        } catch (e: Exception) {
            Instant.fromEpochMilliseconds(0)
        }
    
    val id: String get() = buildString {
        append(source.name.replace(" ", "-").lowercase())
        append("-")
        append(url.substringAfterLast("/").take(20))
        append("-")
        append(url.hashCode().toString().replace("-", "0"))
    }
}

@Parcelize
data class Source(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String
) : Parcelable

data class NewsUiState(
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)