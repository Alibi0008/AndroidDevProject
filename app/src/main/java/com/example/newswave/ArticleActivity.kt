package com.example.newswave

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.newswave.databinding.ActivityArticleBinding
import com.example.newswave.model.Article

class ArticleActivity : AppCompatActivity() {

    lateinit var binding: ActivityArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Подключаем Binding (чтобы удобно обращаться к WebView)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Получаем новость, которую передали
        // "article" - это ключ, по которому мы передадим данные из MainActivity
        val article = intent.getSerializableExtra("article") as? Article

        binding.webView.apply {
            // 2. Настраиваем WebView
            webViewClient = WebViewClient() // Чтобы ссылки открывались тут же, а не в Chrome
            settings.javaScriptEnabled = true // Многие сайты требуют JS

            // 3. Загружаем ссылку
            article?.url?.let {
                loadUrl(it)
            }
        }
    }
}