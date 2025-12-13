package com.example.newswave.data

import com.example.newswave.api.RetrofitInstance
import com.example.newswave.db.ArticleDatabase
import com.example.newswave.model.Article

class NewsRepository (
    val db: ArticleDatabase
){
    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchNews(searchQuery, pageNumber)

    suspend fun getBreakingNews(countryCode: String, category: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber, category)


    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getSavedNews()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}