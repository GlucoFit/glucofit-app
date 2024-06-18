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
import com.fitcoders.glucofitapp.R

class FoodAdapter(
    private val itemClick: (FoodDetails) -> Unit, // Callback untuk item click
    private val favoriteClick: (FoodDetails, Boolean) -> Unit, // Callback untuk favorite click
    private var isListView: Boolean = false // Mengontrol tampilan daftar atau grid
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_LIST = 1
        private const val VIEW_TYPE_GRID = 2
    }

    private val differ = AsyncListDiffer(this, object : DiffUtil.ItemCallback<FoodDetails>() {
        override fun areItemsTheSame(oldItem: FoodDetails, newItem: FoodDetails): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FoodDetails, newItem: FoodDetails): Boolean {
            return oldItem == newItem
        }
    })

    fun submitList(data: List<FoodDetails>) {
        differ.submitList(data)
    }

    fun setViewType(isListView: Boolean) {
        this.isListView = isListView
        notifyDataSetChanged() // Memanggil ini untuk memberitahu adapter bahwa tampilan telah berubah
    }

    override fun getItemViewType(position: Int): Int {
        return if (isListView) VIEW_TYPE_LIST else VIEW_TYPE_GRID
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_LIST -> {
                val binding = ItemFoodListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                FoodListViewHolder(binding, itemClick, favoriteClick)
            }
            else -> {
                val binding = ItemFoodGridBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                FoodGridViewHolder(binding, itemClick, favoriteClick)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = differ.currentList[position]
        when (holder) {
            is FoodListViewHolder -> holder.bind(item)
            is FoodGridViewHolder -> holder.bind(item)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    // ViewHolder untuk tampilan daftar
    class FoodListViewHolder(
        private val binding: ItemFoodListBinding,
        private val itemClick: (FoodDetails) -> Unit,
        private val favoriteClick: (FoodDetails, Boolean) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FoodDetails) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(item.imageUrl)
                    .into(foodImage)
                foodName.text = item.recipeName
                calories.text = item.calories?.toString() ?: "0"
                sugarContent.text = item.sugarContent?.toString() ?: "0"

                // Set ikon favorit berdasarkan status
                favoriteIcon.setImageResource(if (item.isFavorite == true) R.drawable.ic_heart_filled else R.drawable.ic_heart)

                // Handle klik item
                itemView.setOnClickListener { itemClick(item) }

                // Handle klik ikon favorit
                favoriteIcon.setOnClickListener {
                    val isFavorite = item.isFavorite != true // Toggle status favorit
                    favoriteClick(item, isFavorite)
                    item.isFavorite = isFavorite // Update status lokal item

                    // Update tampilan setelah perubahan status favorit
                    favoriteIcon.setImageResource(if (isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart)
                }
            }
        }
    }

    // ViewHolder untuk tampilan grid
    class FoodGridViewHolder(
        private val binding: ItemFoodGridBinding,
        private val itemClick: (FoodDetails) -> Unit,
        private val favoriteClick: (FoodDetails, Boolean) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FoodDetails) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(item.imageUrl)
                    .into(foodImage)
                foodName.text = item.recipeName
                calories.text = item.calories?.toString() ?: "0"
                sugarContent.text = item.sugarContent?.toString() ?: "0"

                // Set ikon favorit berdasarkan status
                favoriteIcon.setImageResource(if (item.isFavorite == true) R.drawable.ic_heart_filled else R.drawable.ic_heart)

                // Handle klik item
                itemView.setOnClickListener { itemClick(item) }

                // Handle klik ikon favorit
                favoriteIcon.setOnClickListener {
                    val isFavorite = item.isFavorite != true // Toggle status favorit
                    favoriteClick(item, isFavorite)
                    item.isFavorite = isFavorite // Update status lokal item

                    // Update tampilan setelah perubahan status favorit
                    favoriteIcon.setImageResource(if (isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart)
                }
            }
        }
    }
}