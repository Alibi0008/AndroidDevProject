package com.example.newswave.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newswave.data.NewsRepository
import com.example.newswave.model.Article
import com.example.newswave.model.NewsResponse
import com.example.newswave.util.CountryPreferences
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(
    val newsRepository: NewsRepository,
    val countryPreferences: CountryPreferences
) : ViewModel() {

    val breakingNews: MutableLiveData<NewsResponse> = MutableLiveData()
    var breakingNewsPage = 1
    var currentCategory = "general"

    val searchNews: MutableLiveData<NewsResponse> = MutableLiveData()
    var searchNewsPage = 1

    val errorMessage: MutableLiveData<String> = MutableLiveData()

    init {
        val savedCountry = countryPreferences.getCountry()
        getBreakingNews(savedCountry, "general")
    }

    fun getBreakingNews(countryCode: String, category: String = currentCategory) = viewModelScope.launch {
        currentCategory = category
        breakingNews.postValue(null)

        try {
            if (countryCode == "ru") {
                val query = when(category) {
                    "sports" -> "Спорт"
                    "technology" -> "Технологии"
                    "business" -> "Бизнес"
                    "science" -> "Наука"
                    "health" -> "Здоровье"
                    else -> "Новости"
                }
                val response = newsRepository.searchNews(query, breakingNewsPage)
                breakingNews.postValue(response)
            } else {
                val response = newsRepository.getBreakingNews("us", category, breakingNewsPage)
                breakingNews.postValue(response)
            }
        } catch (t: Throwable) {
            handleError(t)
        }
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        try {
            val response = newsRepository.searchNews(searchQuery, searchNewsPage)
            searchNews.postValue(response)
        } catch (t: Throwable) {
            handleError(t)
        }
    }

    private fun handleError(t: Throwable) {
        when(t) {
            is IOException -> errorMessage.postValue("Нет соединения с Интернетом 📶")
            else -> errorMessage.postValue("Ошибка: ${t.localizedMessage}")
        }
        Log.e("NewsViewModel", "Error: ${t.message}")
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }
}