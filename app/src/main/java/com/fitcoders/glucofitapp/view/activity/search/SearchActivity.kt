package com.fitcoders.glucofitapp.view.activity.search

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.databinding.ActivitySearchBinding
import com.fitcoders.glucofitapp.utils.adapter.SearchHistoryAdapter
import com.fitcoders.glucofitapp.view.ViewModelFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchHistoryAdapter: SearchHistoryAdapter
    private lateinit var modelFactory: ViewModelFactory
    private val searchViewModel: SearchViewModel by viewModels { modelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        binding =  ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        modelFactory = ViewModelFactory.getInstance(this)

        searchHistoryAdapter = SearchHistoryAdapter(emptyList())
        binding.searchHistoryRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.searchHistoryRecyclerView.adapter = searchHistoryAdapter

        searchViewModel.searchHistory.observe(this, { result ->
            result.onSuccess { historyItems ->
                searchHistoryAdapter.updateData(historyItems)
            }.onFailure { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        })

        searchViewModel.getSearchHistory()
    }
}