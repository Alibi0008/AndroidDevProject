package com.example.newswave.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newswave.data.NewsRepository
import com.example.newswave.model.NewsResponse
import kotlinx.coroutines.launch
import retrofit2.Response

// 👇 ВАЖНО: Теперь мы принимаем (newsRepository) в скобках
class NewsViewModel(
    val newsRepository: NewsRepository
) : ViewModel() {

    // Старую строку "val repository = ..." УДАЛЯЕМ, она больше не нужна

    val breakingNews: MutableLiveData<NewsResponse> = MutableLiveData()
    var breakingNewsPage = 1

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        // Используем тот newsRepository, который пришел в конструкторе
        val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
        breakingNews.postValue(response)
    }
}