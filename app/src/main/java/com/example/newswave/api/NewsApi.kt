package com.example.newswave.api

import com.example.newswave.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country") country: String = "us",
        @Query("page") pageNumber: Int = 1,
        @Query("category") category: String = "general"
    ): NewsResponse

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q") searchQuery: String,
        @Query("page") pageNumber: Int = 1,
    ): NewsResponse
}