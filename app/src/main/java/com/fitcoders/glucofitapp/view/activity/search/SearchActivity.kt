package com.fitcoders.glucofitapp.view.activity.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.databinding.ActivitySearchBinding
import com.fitcoders.glucofitapp.response.FoodRecipeResponseItem
import com.fitcoders.glucofitapp.utils.adapter.SearchHistoryAdapter
import com.fitcoders.glucofitapp.utils.adapter.SearchResultAdapter
import com.fitcoders.glucofitapp.view.ViewModelFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchHistoryAdapter: SearchHistoryAdapter
    private lateinit var searchResultAdapter: SearchResultAdapter
    private lateinit var modelFactory: ViewModelFactory
    private val searchViewModel: SearchViewModel by viewModels { modelFactory }

    private val SearchResultAdapter: SearchResultAdapter by lazy {
        SearchResultAdapter(
            { item ->
                // Handle item click here
            },
            { item, isFavorite ->
                // Handle favorite button click here
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        modelFactory = ViewModelFactory.getInstance(this)

        searchHistoryAdapter = SearchHistoryAdapter(emptyList())
        binding.searchHistoryRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.searchHistoryRecyclerView.adapter = searchHistoryAdapter

        searchResultAdapter = SearchResultAdapter(
            { item ->
                // Handle item click here
            },
            { item, isFavorite ->
                // Handle favorite button click here
            }
        )
        binding.searchResultRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.searchResultRecyclerView.adapter = searchResultAdapter
        binding.searchResultRecyclerView.layoutManager = GridLayoutManager(this, 2)


        binding.searchBarEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.length > 0) {
                    binding.searchButton.setOnClickListener {
                        observeViewModel()
                        searchViewModel.searchFoodByName(s.toString())
                        binding.searchHistoryRecyclerView.visibility = View.GONE
                        binding.searchResultRecyclerView.visibility = View.VISIBLE

                    }
                } else {
                    binding.searchHistoryRecyclerView.visibility = View.VISIBLE
                    binding.searchResultRecyclerView.visibility = View.GONE
                }
            }
        })


        binding.searchResultRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.searchResultRecyclerView.adapter = searchResultAdapter // Set adapter here

        searchViewModel.searchHistory.observe(this, { result ->
            result.onSuccess { historyItems ->
                searchHistoryAdapter.updateData(historyItems)
            }.onFailure { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        })

        searchViewModel.getSearchHistory()
    }

    private fun observeViewModel() {
        searchViewModel.searchResults.observe(this, { result ->
            result.onSuccess { searchResults ->
                searchResultAdapter.submitList(searchResults)
            }.onFailure { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}