package com.example.newswave

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var countryPreferences: CountryPreferences

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
        countryPreferences = CountryPreferences(this) // 👇 Инициализируем настройки

        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository, countryPreferences)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[NewsViewModel::class.java]

        setupRecyclerView()

        val btnSettings = findViewById<ImageButton>(R.id.btnSettings)
        btnSettings.setOnClickListener {
            showCountryDialog()
        }

        val btnGeneral = findViewById<Button>(R.id.btnGeneral)
        val btnBusiness = findViewById<Button>(R.id.btnBusiness)
        val btnSports = findViewById<Button>(R.id.btnSports)
        val btnTech = findViewById<Button>(R.id.btnTech)
        val btnScience = findViewById<Button>(R.id.btnScience)
        val btnHealth = findViewById<Button>(R.id.btnHealth)

        fun onCategoryClick(category: String) {
            val currentCountry = countryPreferences.getCountry()
            viewModel.getBreakingNews(currentCountry, category)
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

        viewModel.errorMessage.observe(this, Observer { message ->
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun showCountryDialog() {
        val languages = arrayOf("🇺🇸 English (USA)", "🇷🇺 Русский (Russia)")
        val codes = arrayOf("us", "ru")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Content Language")
        builder.setItems(languages) { _, which ->
            val selectedCountry = codes[which]

            countryPreferences.saveCountry(selectedCountry)

            viewModel.getBreakingNews(selectedCountry, "general")

            Toast.makeText(this, "Language changed to: ${languages[which]}", Toast.LENGTH_SHORT).show()
        }
        builder.show()
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