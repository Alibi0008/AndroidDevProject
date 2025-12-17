package com.example.newswave

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
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
import com.example.newswave.util.CountryPreferences

class SearchActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val database = ArticleDatabase(this)
        val newsRepository = NewsRepository(database)

        val countryPreferences = CountryPreferences(this)
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository, countryPreferences)

        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        setupRecyclerView()

        val etSearch = findViewById<EditText>(R.id.etSearch)
        val btnSearch = findViewById<ImageButton>(R.id.btnSearch)

        btnSearch.setOnClickListener {
            val query = etSearch.text.toString()
            if (query.isNotEmpty()) {
                viewModel.searchNews(query)
            }
        }

        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = etSearch.text.toString()
                if (query.isNotEmpty()) {
                    viewModel.searchNews(query)
                }
                true
            } else {
                false
            }
        }

        viewModel.searchNews.observe(this, Observer { response ->
            if(response != null) {
                newsAdapter.differ.submitList(response.articles)
            }
        })

        viewModel.errorMessage.observe(this, Observer { message ->
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        val rvSearchNews = findViewById<RecyclerView>(R.id.rvSearchNews)
        rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(this@SearchActivity)
        }

        newsAdapter.setOnItemClickListener { article ->
            val intent = Intent(this, ArticleActivity::class.java)
            intent.putExtra("article", article)
            startActivity(intent)
        }
    }
}