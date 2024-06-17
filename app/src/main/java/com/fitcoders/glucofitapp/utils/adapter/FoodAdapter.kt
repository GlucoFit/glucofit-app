package com.fitcoders.glucofitapp.utils.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fitcoders.glucofitapp.databinding.ItemFoodGridBinding
import com.fitcoders.glucofitapp.databinding.ItemFoodListBinding
import com.fitcoders.glucofitapp.response.FoodDetails
import com.bumptech.glide.Glide

class FoodAdapter(
    private val itemClick: (FoodDetails) -> Unit,
    private val favoriteClick: (FoodDetails, Boolean) -> Unit, // New callback for favorite button
    private var isListView: Boolean = false
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_LIST = 1
        private const val VIEW_TYPE_GRID = 2
    }

    private val foodDiffer = AsyncListDiffer(
        this,
        object : DiffUtil.ItemCallback<FoodDetails>() {
            override fun areItemsTheSame(oldItem: FoodDetails, newItem: FoodDetails): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: FoodDetails, newItem: FoodDetails): Boolean {
                return oldItem == newItem
            }
        }
    )

    fun submitList(data: List<FoodDetails>) {
        foodDiffer.submitList(data)
    }

    fun setViewType(isListView: Boolean) {
        this.isListView = isListView
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (isListView) VIEW_TYPE_LIST else VIEW_TYPE_GRID
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_GRID -> {
                val binding = ItemFoodGridBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                FoodGridViewHolder(binding, itemClick, favoriteClick)
            }
            else -> {
                val binding = ItemFoodListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                FoodListViewHolder(binding, itemClick, favoriteClick)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = foodDiffer.currentList[position]
        when (holder) {
            is FoodListViewHolder -> holder.bindView(item)
            is FoodGridViewHolder -> holder.bindView(item)
        }
    }

    override fun getItemCount(): Int = foodDiffer.currentList.size

    // ViewHolder for list view
    class FoodListViewHolder(
        private val binding: ItemFoodListBinding,
        private val itemClick: (FoodDetails) -> Unit,
        private val favoriteClick: (FoodDetails, Boolean) -> Unit // Favorite button click callback
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: FoodDetails) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(item.imageUrl)
                    .into(foodImage)
                foodName.text = item.recipeName
                calories.text = item.calories.toString()
                sugarUnitCalory.text = "Cal"
                sugarContent.text = item.sugarContent.toString()
                sugarUnit.text = "g"
                itemView.setOnClickListener { itemClick(item) }

                // Setup favorite button click
                favoriteIcon.setOnClickListener {
                    val isFavorite = item.isFavorite != true // Toggle favorite state
                    favoriteClick(item, isFavorite)
                }
            }
        }
    }

    // ViewHolder for grid view
    class FoodGridViewHolder(
        private val binding: ItemFoodGridBinding,
        private val itemClick: (FoodDetails) -> Unit,
        private val favoriteClick: (FoodDetails, Boolean) -> Unit // Favorite button click callback
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: FoodDetails) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(item.imageUrl)
                    .into(foodImage)
                foodName.text = item.recipeName
                calories.text = item.calories.toString()
                sugarUnitCalory.text = "Cal"
                sugarContent.text = item.sugarContent.toString()
                sugarUnit.text = "g"
                itemView.setOnClickListener { itemClick(item) }

                // Setup favorite button click
                favoriteIcon.setOnClickListener {
                    val isFavorite = item.isFavorite != true // Toggle favorite state
                    favoriteClick(item, isFavorite)
                }
            }
        }
    }
}