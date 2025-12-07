package com.example.newswave.data

import com.example.newswave.api.RetrofitInstance

class NewsRepository {

    // Функция 1: Запрашиваем главные новости через наш RetrofitInstance
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    // Функция 2: Ищем новости
    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchNews(searchQuery, pageNumber)
}