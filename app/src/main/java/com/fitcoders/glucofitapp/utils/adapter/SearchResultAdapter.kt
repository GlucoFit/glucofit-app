package com.fitcoders.glucofitapp.utils.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitcoders.glucofitapp.databinding.ItemFoodGridBinding
import com.fitcoders.glucofitapp.response.FoodRecipeResponseItem

class SearchResultAdapter (
    private val itemClick: (FoodRecipeResponseItem) -> Unit,
    private val favoriteClick: (FoodRecipeResponseItem, Boolean) -> Unit, // New callback for favorite button
) : RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder>() {

    private val searchResultsDiffer = AsyncListDiffer(
        this,
        object : DiffUtil.ItemCallback<FoodRecipeResponseItem>() {
            override fun areItemsTheSame(oldItem: FoodRecipeResponseItem, newItem: FoodRecipeResponseItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: FoodRecipeResponseItem, newItem: FoodRecipeResponseItem): Boolean {
                return oldItem == newItem
            }
        }
    )

    fun submitList(data: List<FoodRecipeResponseItem>) {
        searchResultsDiffer.submitList(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val binding = ItemFoodGridBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchResultViewHolder(binding, itemClick, favoriteClick)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        val searchResultItem = searchResultsDiffer.currentList[position]
        holder.bind(searchResultItem)
    }

    override fun getItemCount(): Int = searchResultsDiffer.currentList.size

    class SearchResultViewHolder (
        private val binding: ItemFoodGridBinding,
        private val itemClick: (FoodRecipeResponseItem) -> Unit,
        private val favoriteClick: (FoodRecipeResponseItem, Boolean) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(searchResultItem: FoodRecipeResponseItem) {
            with(binding) {
                Glide.with(itemView)
                    .load(searchResultItem.imageUrl)
                    .centerCrop()
                    .into(foodImage)
                foodName.text = searchResultItem.recipeName
                calories.text = searchResultItem.calories.toString()
                sugarUnitCalory.text = "Cal"
                sugarContent.text = searchResultItem.sugarContent.toString()
                sugarUnit.text = "g"
                itemView.setOnClickListener {
                    itemClick(searchResultItem)
                }

            }
        }
    }


}