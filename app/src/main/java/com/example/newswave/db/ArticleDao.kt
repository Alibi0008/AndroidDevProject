package com.example.newswave.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newswave.model.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long

    @Query("SELECT * FROM articles")
    fun getSavedNews(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}