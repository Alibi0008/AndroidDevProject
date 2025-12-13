package com.example.newswave

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.newswave.data.NewsRepository
import com.example.newswave.databinding.ActivityArticleBinding
import com.example.newswave.db.ArticleDatabase
import com.example.newswave.model.Article
import com.example.newswave.ui.NewsViewModel
import com.example.newswave.ui.NewsViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar

class ArticleActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel // 👈 Объявляем переменную
    lateinit var binding: ActivityArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- 1. ИНИЦИАЛИЗАЦИЯ VIEWMODEL (ДОБАВЛЯЕМ ЭТОТ БЛОК) ---
        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)
        // --------------------------------------------------------

        // Получаем новость из Intent
        val article = intent.getSerializableExtra("article") as Article

        // Настраиваем WebView
        binding.webView.apply {
            webViewClient = WebViewClient()
            article.url?.let { loadUrl(it) }
        }

        // Настраиваем кнопку сохранения (FAB)
        binding.fab.setOnClickListener {
            viewModel.saveArticle(article) // Теперь viewModel существует!
            Snackbar.make(binding.root, "Article Saved Successfully", Snackbar.LENGTH_SHORT).show()
        }
    }
}