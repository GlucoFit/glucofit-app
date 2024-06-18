package com.fitcoders.glucofitapp.view.activity.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.databinding.ActivitySearchBinding
import com.fitcoders.glucofitapp.response.SearchHistoryResponseItem
import com.fitcoders.glucofitapp.utils.adapter.SearchHistoryAdapter
import com.fitcoders.glucofitapp.utils.adapter.SearchResultAdapter
import com.fitcoders.glucofitapp.view.ViewModelFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchHistoryAdapter: SearchHistoryAdapter
    private lateinit var searchResultAdapter: SearchResultAdapter
    private lateinit var modelFactory: ViewModelFactory
    private val searchViewModel: SearchViewModel by viewModels { modelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        modelFactory = ViewModelFactory.getInstance(this)

        val backButton: ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }

        // Initialize search history adapter with click listener
        searchHistoryAdapter = SearchHistoryAdapter(emptyList()) { item ->
            handleSearchHistoryItemClick(item)
        }
        binding.searchHistoryRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.searchHistoryRecyclerView.adapter = searchHistoryAdapter

        // Initialize search result adapter
        searchResultAdapter = SearchResultAdapter(
            { item ->
                // Handle item click here
            },
            { item, isFavorite ->
                // Handle favorite button click here
            }
        )
        binding.searchResultRecyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.searchResultRecyclerView.adapter = searchResultAdapter

        // Add text watcher to search bar
        binding.searchBarEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.isNotEmpty()) {
                    binding.searchButton.setOnClickListener {
                        performSearch(s.toString())
                    }
                } else {
                    binding.searchHistoryRecyclerView.visibility = View.VISIBLE
                    binding.searchResultRecyclerView.visibility = View.GONE
                }
            }
        })

        binding.clearButton.setOnClickListener {
            binding.searchBarEditText.text.clear()
            searchViewModel.getSearchHistory()
            binding.searchHistoryRecyclerView.visibility = View.VISIBLE
            binding.searchResultRecyclerView.visibility = View.GONE
        }

        // Observe search history
        searchViewModel.searchHistory.observe(this, { result ->
            result.onSuccess { historyItems ->
                searchHistoryAdapter.updateData(historyItems)
            }.onFailure { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        })

        // Trigger the search history retrieval
        searchViewModel.getSearchHistory()
    }

    private fun handleSearchHistoryItemClick(item: SearchHistoryResponseItem) {
        val searchText = item.searchText ?: return // Return early if searchText is null
        binding.searchBarEditText.setText(searchText)
        performSearch(searchText)
    }


    private fun performSearch(query: String) {
        observeViewModel()
        searchViewModel.searchFoodByName(query)
        binding.searchHistoryRecyclerView.visibility = View.GONE
        binding.searchResultRecyclerView.visibility = View.VISIBLE
    }

    private fun observeViewModel() {
        // Observe search results
        searchViewModel.searchResults.observe(this, { result ->
            result.onSuccess { searchResults ->
                searchResultAdapter.submitList(searchResults)
            }.onFailure { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
