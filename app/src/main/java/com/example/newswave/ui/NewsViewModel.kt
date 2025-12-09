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

    val breakingNews: MutableLiveData<NewsResponse> = MutableLiveData()
    var breakingNewsPage = 1
    var currentCategory = "general" // 👈 Запоминаем категорию (по умолчанию Общее)

    val searchNews: MutableLiveData<NewsResponse> = MutableLiveData()
    var searchNewsPage = 1

    init {
        getBreakingNews("us", "general") // Грузим общее при старте
    }

    // Теперь функция принимает категорию
    fun getBreakingNews(countryCode: String, category: String) = viewModelScope.launch {
        currentCategory = category // Обновляем текущую категорию
        val response = newsRepository.getBreakingNews(countryCode, category, breakingNewsPage)
        breakingNews.postValue(response)
    }

    // 3. Функция поиска (ОНА ДОЛЖНА БЫТЬ ТУТ)
    fun searchNews(searchQuery: String) = viewModelScope.launch {
        val response = newsRepository.searchNews(searchQuery, searchNewsPage)
        searchNews.postValue(response)
    }
}