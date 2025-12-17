package com.example.newswave.util

import android.content.Context
import android.content.SharedPreferences

class CountryPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("news_settings", Context.MODE_PRIVATE)

    fun saveCountry(countryCode: String) {
        prefs.edit().putString("country_code", countryCode).apply()
    }

    fun getCountry(): String {
        return prefs.getString("country_code", "us") ?: "us"
    }
}