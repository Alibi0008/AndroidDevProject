package com.example.newswave.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

// 1. Как выглядит ответ сервера целиком
data class NewsResponse(
    val status: String,
    val totalResults: Int,
    @SerializedName("articles")
    val articles: MutableList<Article> // MutableList удобнее для добавления страниц
)

// 2. СУЩНОСТЬ БАЗЫ ДАННЫХ (Таблица)
@Entity(
    tableName = "articles"
)
data class Article(
    @PrimaryKey(autoGenerate = true) // 👈 ГЛАВНОЕ: Уникальный ID для базы (генерируется сам)
    var id: Int? = null,

    @SerializedName("source")
    val source: Source?, // Room сохранит это благодаря нашему TypeConverter

    @SerializedName("author")
    val author: String?,

    @SerializedName("title")
    val title: String?,

    @SerializedName("description")
    val description: String?,

    @SerializedName("url")
    val url: String?,

    @SerializedName("urlToImage")
    val urlToImage: String?,

    @SerializedName("publishedAt")
    val publishedAt: String?,

    @SerializedName("content")
    val content: String?
) : Serializable {
    // Переопределяем hashCode и equals, чтобы корректно работал DiffUtil в адаптере
    override fun hashCode(): Int {
        var result = id.hashCode()
        if(url.isNullOrEmpty()){
            result = 31 * result + url.hashCode()
        }
        return result
    }
}

// 3. Источник новости
data class Source(
    val id: String?,
    val name: String
) : Serializable