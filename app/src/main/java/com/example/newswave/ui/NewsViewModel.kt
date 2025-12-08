package com.example.newswave.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newswave.data.NewsRepository
import com.example.newswave.model.NewsResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    val newsRepository: NewsRepository
) : ViewModel() {

    // 1. Главные новости
    val breakingNews: MutableLiveData<NewsResponse> = MutableLiveData()
    var breakingNewsPage = 1

    // 2. Поиск (ПРОВЕРЬ, ЕСТЬ ЛИ ЭТОТ БЛОК)
    val searchNews: MutableLiveData<NewsResponse> = MutableLiveData()
    var searchNewsPage = 1

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
        breakingNews.postValue(response)
    }

    // 3. Функция поиска (ОНА ДОЛЖНА БЫТЬ ТУТ)
    fun searchNews(searchQuery: String) = viewModelScope.launch {
        val response = newsRepository.searchNews(searchQuery, searchNewsPage)
        searchNews.postValue(response)
    }
}