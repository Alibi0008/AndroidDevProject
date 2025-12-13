package com.example.newswave.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newswave.model.Article

@Dao
interface ArticleDao {

    // 1. Вставить или Обновить (Update + Insert)
    // Если такая новость уже есть, мы ее просто обновим (REPLACE)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long

    // 2. Получить все сохраненные новости
    // LiveData означает, что список обновится сам, если мы что-то добавим
    @Query("SELECT * FROM articles")
    fun getSavedNews(): LiveData<List<Article>>

    // 3. Удалить новость
    @Delete
    suspend fun deleteArticle(article: Article)
}