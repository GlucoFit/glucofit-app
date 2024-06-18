package com.fitcoders.glucofitapp.view.fragment.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitcoders.glucofitapp.data.AppRepository
import com.fitcoders.glucofitapp.response.FavoritFoodResponseItem
import com.fitcoders.glucofitapp.response.FavoritResponse
import com.fitcoders.glucofitapp.response.RecommendationResponseItem
import com.fitcoders.glucofitapp.utils.Event
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: AppRepository) : ViewModel() {

    val favoriteFoods: LiveData<Result<List<FavoritFoodResponseItem>>> get() = repository.favoriteFoods
    val favoriteResponse: LiveData<FavoritResponse?> get() = repository.favoriteResponse
    val isLoading: LiveData<Boolean> get() = repository.isLoading
    val toastText: LiveData<Event<String>> get() = repository.toastText

    // Memanggil metode untuk mendapatkan data favorit
    fun fetchFavoriteFoods() {
        viewModelScope.launch {
            repository.getFavoriteFoods()
        }
    }

    // Memanggil metode untuk menandai makanan sebagai favorit atau bukan favorit
    fun markAsFavorite(foodId: Int, isFavorite: Int) {
        viewModelScope.launch {
            repository.markAsFavorite(foodId, isFavorite)
        }
    }
}