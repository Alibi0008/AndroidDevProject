package com.example.newswave

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newswave.adapters.NewsAdapter
import com.example.newswave.data.NewsRepository
import com.example.newswave.db.ArticleDatabase
import com.example.newswave.ui.NewsViewModel
import com.example.newswave.ui.NewsViewModelProviderFactory

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.fabSaved).setOnClickListener {
            val intent = Intent(this, SavedNewsActivity::class.java)
            startActivity(intent)
        }

        findViewById<View>(R.id.fabSearch).setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }


        val database = ArticleDatabase(this)

        val newsRepository = NewsRepository(database)

        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[NewsViewModel::class.java]


        setupRecyclerView()

        val btnGeneral = findViewById<Button>(R.id.btnGeneral)
        val btnBusiness = findViewById<Button>(R.id.btnBusiness)
        val btnSports = findViewById<Button>(R.id.btnSports)
        val btnTech = findViewById<Button>(R.id.btnTech)
        val btnScience = findViewById<Button>(R.id.btnScience)
        val btnHealth = findViewById<Button>(R.id.btnHealth)

        fun onCategoryClick(category: String) {
            viewModel.getBreakingNews("us", category)
        }

        btnGeneral.setOnClickListener { onCategoryClick("general") }
        btnBusiness.setOnClickListener { onCategoryClick("business") }
        btnSports.setOnClickListener { onCategoryClick("sports") }
        btnTech.setOnClickListener { onCategoryClick("technology") }
        btnScience.setOnClickListener { onCategoryClick("science") }
        btnHealth.setOnClickListener { onCategoryClick("health") }

        viewModel.breakingNews.observe(this, Observer { response ->
            if(response != null) {
                newsAdapter.differ.submitList(response.articles)
            } else {
                Log.e("MainActivity", "Ошибка: Ответ пустой")
            }
        })
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        val rvBreakingNews = findViewById<RecyclerView>(R.id.rvBreakingNews)

        rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        newsAdapter.setOnItemClickListener { article ->
            val intent = Intent(this, ArticleActivity::class.java)
            intent.putExtra("article", article)
            startActivity(intent)
        }
    }
}