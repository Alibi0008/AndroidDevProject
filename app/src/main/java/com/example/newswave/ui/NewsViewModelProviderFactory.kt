package com.example.newswave.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newswave.data.NewsRepository
import com.example.newswave.util.CountryPreferences

class NewsViewModelProviderFactory(
    val newsRepository: NewsRepository,
    val countryPreferences: CountryPreferences
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(newsRepository, countryPreferences) as T
    }
}