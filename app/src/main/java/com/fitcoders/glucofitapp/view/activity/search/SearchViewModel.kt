package com.fitcoders.glucofitapp.view.activity.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitcoders.glucofitapp.data.AppRepository
import com.fitcoders.glucofitapp.response.FoodRecipeResponseItem
import com.fitcoders.glucofitapp.response.SearchHistoryResponseItem
import kotlinx.coroutines.launch

class SearchViewModel (private val repository: AppRepository) : ViewModel() {
    val searchResults: LiveData<Result<List<FoodRecipeResponseItem>>> get() = repository.searchResults

    val searchHistory: LiveData<Result<List<SearchHistoryResponseItem>>> get() = repository.searchHistory

    fun searchFoodByName(query: String) {
        viewModelScope.launch {
            repository.searchFoodByName(query)
        }
    }

    fun getSearchHistory() {
        viewModelScope.launch {
            repository.getSearchHistory()
        }
    }

}