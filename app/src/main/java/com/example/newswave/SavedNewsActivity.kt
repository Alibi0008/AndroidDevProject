package com.example.newswave

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newswave.adapters.NewsAdapter
import com.example.newswave.data.NewsRepository
import com.example.newswave.db.ArticleDatabase
import com.example.newswave.ui.NewsViewModel
import com.example.newswave.ui.NewsViewModelProviderFactory
import com.google.android.material.snackbar.Snackbar

class SavedNewsActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_news)

        // 1. Инициализируем ViewModel (как обычно)
        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        // 2. Настраиваем список
        setupRecyclerView()

        // 3. СЛУШАЕМ БАЗУ ДАННЫХ 🔥
        // Как только ты сохранишь новую статью, этот список обновится сам!
        viewModel.getSavedNews().observe(this, Observer { articles ->
            newsAdapter.differ.submitList(articles)
        })

        // 4. СВАЙП ДЛЯ УДАЛЕНИЯ (БОНУСНАЯ ФИЧА) 🧹
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Получаем позицию и саму статью
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]

                // Удаляем
                viewModel.deleteArticle(article)

                // Показываем кнопку "Отмена"
                Snackbar.make(findViewById(R.id.rvSavedNews), "Article deleted", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        viewModel.saveArticle(article)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(findViewById(R.id.rvSavedNews))
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        val rvSavedNews = findViewById<RecyclerView>(R.id.rvSavedNews)

        rvSavedNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(this@SavedNewsActivity)
        }

        // Клик по статье -> Открыть читать
        newsAdapter.setOnItemClickListener { article ->
            val intent = Intent(this, ArticleActivity::class.java)
            intent.putExtra("article", article)
            startActivity(intent)
        }
    }
}