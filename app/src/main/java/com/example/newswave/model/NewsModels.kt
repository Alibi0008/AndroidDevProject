package com.example.newswave.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

// 1. Как выглядит ответ сервера целиком
data class NewsResponse(
    val status: String,
    val totalResults: Int,
    @SerializedName("articles")
    val articles: List<Article> // Список новостей
)

// 2. Как выглядит одна конкретная новость
data class Article(
    @SerializedName("source")
    val source: Source?,

    @SerializedName("author")
    val author: String?,

    @SerializedName("title")
    val title: String?,

    @SerializedName("description")
    val description: String?,

    @SerializedName("url")
    val url: String?, // Ссылка на полную статью

    @SerializedName("urlToImage")
    val urlToImage: String?, // Ссылка на картинку

    @SerializedName("publishedAt")
    val publishedAt: String?,

    @SerializedName("content")
    val content: String?
) : Serializable

// 3. Источник новости (BBC, CNN и т.д.)
data class Source(
    val id: String?,
    val name: String
) : Serializable