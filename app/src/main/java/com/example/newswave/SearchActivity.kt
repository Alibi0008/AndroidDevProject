package com.example.newswave

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newswave.adapters.NewsAdapter
import com.example.newswave.data.NewsRepository
import com.example.newswave.databinding.ActivitySearchBinding
import com.example.newswave.ui.NewsViewModel
import com.example.newswave.ui.NewsViewModelProviderFactory

class SearchActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Настраиваем ViewModel
        val repository = NewsRepository()
        val viewModelProviderFactory = NewsViewModelProviderFactory(repository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        // 2. Настраиваем список
        setupRecyclerView()

        // 3. Обработка клика по кнопке "Поиск"
        binding.btnSearch.setOnClickListener {
            val query = binding.etSearch.text.toString()

            if (query.isNotEmpty()) {
                Log.d("SearchActivity", "Кнопка нажата, ищем: $query") // <-- ПРОВЕРКА В ЛОГАХ

                // Показываем загрузку
                showProgressBar()

                // Прячем клавиатуру
                hideKeyboard()

                // Запускаем поиск
                viewModel.searchNews(query)
            } else {
                Toast.makeText(this, "Введите текст для поиска...", Toast.LENGTH_SHORT).show()
            }
        }

        // ... (после binding.btnSearch.setOnClickListener)

        // Обработка нажатия Enter на клавиатуре
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                binding.btnSearch.performClick() // Эмулируем нажатие на нашу кнопку
                return@setOnEditorActionListener true
            }
            false
        }

        // 4. Следим за результатами
        viewModel.searchNews.observe(this, Observer { response ->
            // Как только пришли данные — прячем загрузку
            hideProgressBar()

            if (response != null) {
                Log.d("SearchActivity", "Пришли новости: ${response.articles.size} шт.")
                newsAdapter.differ.submitList(response.articles)
            } else {
                Log.e("SearchActivity", "Ошибка: Ответ пустой")
            }
        })


        // 1. Ставим фокус на строку ввода
        binding.etSearch.requestFocus()

        // 2. Открываем клавиатуру принудительно
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.etSearch, InputMethodManager.SHOW_IMPLICIT)
    }

    // Функция скрытия клавиатуры (для удобства)
    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    // Управление загрузкой
    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(this@SearchActivity)
        }

        // Клик по новости
        newsAdapter.setOnItemClickListener { article ->
            val intent = Intent(this, ArticleActivity::class.java)
            intent.putExtra("article", article)
            startActivity(intent)
        }
    }
}