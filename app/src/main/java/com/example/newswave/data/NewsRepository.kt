package com.example.newswave.data

import com.example.newswave.api.RetrofitInstance
import com.example.newswave.db.ArticleDatabase
import com.example.newswave.model.Article

class NewsRepository (
    val db: ArticleDatabase // 👈 1. Теперь мы требуем базу данных при создании
){
    // Функция 2: Ищем новости
    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchNews(searchQuery, pageNumber)

    // Теперь принимаем (countryCode, category, pageNumber)
    suspend fun getBreakingNews(countryCode: String, category: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber, category)

    // --- БАЗА ДАННЫХ (Room) --- 👈 2. Новые функции

    // Сохранить новость
    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    // Получить все новости (для экрана Избранное)
    fun getSavedNews() = db.getArticleDao().getSavedNews()

    // Удалить новость
    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}