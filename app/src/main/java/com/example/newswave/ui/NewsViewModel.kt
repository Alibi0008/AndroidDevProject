package com.example.newswave.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newswave.data.NewsRepository
import com.example.newswave.model.Article
import com.example.newswave.model.NewsResponse
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(
    val newsRepository: NewsRepository
) : ViewModel() {

    val breakingNews: MutableLiveData<NewsResponse> = MutableLiveData()
    var breakingNewsPage = 1
    var currentCategory = "general"

    val searchNews: MutableLiveData<NewsResponse> = MutableLiveData()
    var searchNewsPage = 1

    val errorMessage: MutableLiveData<String> = MutableLiveData()

    init {
        getBreakingNews("us", "general")
    }

    fun getBreakingNews(countryCode: String, category: String) = viewModelScope.launch {
        currentCategory = category
        try {
            val response = newsRepository.getBreakingNews(countryCode, category, breakingNewsPage)
            breakingNews.postValue(response)
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