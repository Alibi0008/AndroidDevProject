package com.example.newswave.api

import com.example.newswave.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://newsapi.org/"

    private val retrofit by lazy {
        // 1. Логирование (чтобы мы видели в консоли, что скачивается)
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        // 2. Настройка клиента
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            // 👇 МАГИЯ: Автоматически добавляем API Key в каждый запрос
            .addInterceptor { chain ->
                val original = chain.request()
                val url = original.url.newBuilder()
                    .addQueryParameter("apiKey", "bbae1f763cff41c68f14cd219e3e9740")                    .build()
                val request = original.newBuilder().url(url).build()
                chain.proceed(request)
            }
            .build()

        // 3. Создаем Retrofit
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    // Это публичная переменная, через которую мы будем обращаться к API
    val api: NewsApi by lazy {
        retrofit.create(NewsApi::class.java)
    }
}