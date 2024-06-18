package com.fitcoders.glucofitapp.utils.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.databinding.ItemFoodGridBinding
import com.fitcoders.glucofitapp.databinding.ItemFoodListBinding
import com.fitcoders.glucofitapp.response.FavoritFoodResponseItem

class FavoriteAdapter(
    private val onItemClicked: (FavoritFoodResponseItem) -> Unit,
    private val onFavoriteClicked: (FavoritFoodResponseItem, Boolean) -> Unit,
    private var isListView: Boolean = false
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_LIST = 1
        private const val VIEW_TYPE_GRID = 2
    }

    private val differCallback = object : DiffUtil.ItemCallback<FavoritFoodResponseItem>() {
        override fun areItemsTheSame(oldItem: FavoritFoodResponseItem, newItem: FavoritFoodResponseItem): Boolean {
            return oldItem.food?.id == newItem.food?.id
        }

        override fun areContentsTheSame(oldItem: FavoritFoodResponseItem, newItem: FavoritFoodResponseItem): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    fun submitList(data: List<FavoritFoodResponseItem>) {
        Log.d("FavoriteAdapter", "Submitting list of favorite foods: ${data.size} items")
        differ.submitList(data)
    }

    fun setViewType(isListView: Boolean) {
        this.isListView = isListView
        notifyDataSetChanged()
    }

    // Memperbarui status favorit pada item tertentu
    fun updateFavoriteStatus(foodId: Int, isFavorite: Boolean) {
        val index = differ.currentList.indexOfFirst { it.food?.id == foodId }
        if (index != -1) {
            differ.currentList[index].isFavorite = isFavorite
            notifyItemChanged(index)
        }
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
                FoodGridViewHolder(binding, onItemClicked, onFavoriteClicked)
            }
            else -> {
                val binding = ItemFoodListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                FoodListViewHolder(binding, onItemClicked, onFavoriteClicked)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = differ.currentList[position]
        when (holder) {
            is FoodListViewHolder -> {
                Log.d("FavoriteAdapter", "Binding item in list view: ${item.food?.recipeName}")
                holder.bind(item)
            }
            is FoodGridViewHolder -> {
                Log.d("FavoriteAdapter", "Binding item in grid view: ${item.food?.recipeName}")
                holder.bind(item)
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    // ViewHolder untuk tampilan daftar
    class FoodListViewHolder(
        private val binding: ItemFoodListBinding,
        private val itemClick: (FavoritFoodResponseItem) -> Unit,
        private val favoriteClick: (FavoritFoodResponseItem, Boolean) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FavoritFoodResponseItem) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(item.food?.imageUrl)
                    .into(foodImage)
                foodName.text = item.food?.recipeName
                calories.text = item.food?.calories.toString()
                sugarContent.text = item.food?.sugarContent.toString()
                sugarUnit.text  = "g"
                sugarUnitCalory.text ="Cal"
                favoriteIcon.setImageResource(if (item.isFavorite == true) R.drawable.ic_heart_filled else R.drawable.ic_heart)
                itemView.setOnClickListener { itemClick(item) }

                // Setup favorite button click
                favoriteIcon.setOnClickListener {
                    val newFavoriteStatus = item.isFavorite?.not() ?: true
                    favoriteClick(item, newFavoriteStatus)
                }
            }
        }
    }

    // ViewHolder untuk tampilan grid
    class FoodGridViewHolder(
        private val binding: ItemFoodGridBinding,
        private val itemClick: (FavoritFoodResponseItem) -> Unit,
        private val favoriteClick: (FavoritFoodResponseItem, Boolean) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FavoritFoodResponseItem) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(item.food?.imageUrl)
                    .into(foodImage)
                foodName.text = item.food?.recipeName
                calories.text = item.food?.calories.toString()
                sugarContent.text = item.food?.sugarContent.toString()
                sugarUnit.text  = "g"
                sugarUnitCalory.text ="Cal"
                favoriteIcon.setImageResource(if (item.isFavorite == true) R.drawable.ic_heart_filled else R.drawable.ic_heart)
                itemView.setOnClickListener { itemClick(item) }

                // Setup favorite button click
                favoriteIcon.setOnClickListener {
                    val newFavoriteStatus = item.isFavorite?.not() ?: true
                    favoriteClick(item, newFavoriteStatus)
                }
            }
        }
    }
}
