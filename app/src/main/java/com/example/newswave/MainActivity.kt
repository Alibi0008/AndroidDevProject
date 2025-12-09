package com.example.newswave

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newswave.adapters.NewsAdapter
import com.example.newswave.data.NewsRepository
import com.example.newswave.ui.NewsViewModel
import com.example.newswave.ui.NewsViewModelProviderFactory

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.fabSearch).setOnClickListener {
            val intent = android.content.Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        // 1. Создаем ViewModel (через фабрику, это правильный способ)
        val repository = NewsRepository()
        val viewModelProviderFactory = NewsViewModelProviderFactory(repository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        // 2. Настраиваем список (RecyclerView)
        setupRecyclerView()

        // ... после setupRecyclerView()

        // --- ЛОГИКА КАТЕГОРИЙ ---

        // Находим кнопки (можно сделать красивее, но так понятнее всего)
        val btnGeneral = findViewById<android.widget.Button>(R.id.btnGeneral)
        val btnBusiness = findViewById<android.widget.Button>(R.id.btnBusiness)
        val btnSports = findViewById<android.widget.Button>(R.id.btnSports)
        val btnTech = findViewById<android.widget.Button>(R.id.btnTech)
        val btnScience = findViewById<android.widget.Button>(R.id.btnScience)
        val btnHealth = findViewById<android.widget.Button>(R.id.btnHealth)

        // Функция-помощник, чтобы не писать одно и то же
        fun onCategoryClick(category: String) {
            // Показываем загрузку (можно добавить visual effect)
            viewModel.getBreakingNews("us", category)
        }

        // Вешаем слушатели
        btnGeneral.setOnClickListener { onCategoryClick("general") }
        btnBusiness.setOnClickListener { onCategoryClick("business") }
        btnSports.setOnClickListener { onCategoryClick("sports") }
        btnTech.setOnClickListener { onCategoryClick("technology") }
        btnScience.setOnClickListener { onCategoryClick("science") }
        btnHealth.setOnClickListener { onCategoryClick("health") }

        // ... остальной код (fabSearch, observe)

        // 3. Подписываемся на новости ("Слушаем прямой эфир")
        viewModel.breakingNews.observe(this, Observer { response ->
            if(response != null) {
                // Если пришли новости - отдаем их адаптеру
                newsAdapter.differ.submitList(response.articles)
            } else {
                Log.e("MainActivity", "Ошибка: Ответ пустой")
            }
        })
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        val rvBreakingNews = findViewById<RecyclerView>(R.id.rvBreakingNews)

        // 1. Настройка списка (как было)
        rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        // 👇 2. ДОБАВЛЯЕМ ОБРАБОТКУ КЛИКА СЮДА
        newsAdapter.setOnItemClickListener { article ->
            // Создаем намерение (Intent) перейти на экран ArticleActivity
            val intent = android.content.Intent(this, ArticleActivity::class.java)

            // Кладем новость в "рюкзак", чтобы передать её
            intent.putExtra("article", article)

            // Поехали!
            startActivity(intent)
        }
    }
}