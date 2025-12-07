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

        // 1. Создаем ViewModel (через фабрику, это правильный способ)
        val repository = NewsRepository()
        val viewModelProviderFactory = NewsViewModelProviderFactory(repository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        // 2. Настраиваем список (RecyclerView)
        setupRecyclerView()

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