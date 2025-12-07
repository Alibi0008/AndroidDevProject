package com.example.newswave.api

import com.example.newswave.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    // Функция 1: Получить главные новости
    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country") country: String = "us",
        @Query("page") pageNumber: Int = 1,
        // Ключ API мы будем добавлять автоматически через Interceptor,
        // но пока можно передавать его и так, если что.
    ): NewsResponse

    // Функция 2: Поиск новостей
    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q") searchQuery: String,
        @Query("page") pageNumber: Int = 1,
    ): NewsResponse
}