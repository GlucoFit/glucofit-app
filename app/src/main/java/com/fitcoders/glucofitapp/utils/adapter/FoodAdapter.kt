package com.fitcoders.glucofitapp.utils.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.data.Food

class FoodAdapter(private var foodList: List<Food>, private var isListView: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_LIST = 1
    private val VIEW_TYPE_GRID = 2

    inner class FoodListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodImage: ImageView = itemView.findViewById(R.id.avatarImageView)
        val foodName: TextView = itemView.findViewById(R.id.food_name)
        val foodDescription: TextView = itemView.findViewById(R.id.food_description)
    }

    inner class FoodGridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodImage: ImageView = itemView.findViewById(R.id.food_image)
        val foodName: TextView = itemView.findViewById(R.id.food_name)
        val foodDescription: TextView = itemView.findViewById(R.id.food_description)
        val foodWeight: TextView = itemView.findViewById(R.id.food_weight)
        val favoriteIcon: ImageView = itemView.findViewById(R.id.favorite_icon)
    }

    override fun getItemViewType(position: Int): Int {
        return if (isListView) VIEW_TYPE_LIST else VIEW_TYPE_GRID
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_LIST) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food_list, parent, false)
            FoodListViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food_grid, parent, false)
            FoodGridViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val food = foodList[position]
        if (holder is FoodListViewHolder) {
            holder.foodName.text = food.name
            holder.foodDescription.text = food.description
            Glide.with(holder.itemView.context).load(food.imageUrl).into(holder.foodImage)
        } else if (holder is FoodGridViewHolder) {
            holder.foodName.text = food.name
            holder.foodDescription.text = food.description
            holder.foodWeight.text = food.weight
            Glide.with(holder.itemView.context).load(food.imageUrl).into(holder.foodImage)
        }
    }

    override fun getItemCount(): Int = foodList.size

    fun setViewType(isListView: Boolean) {
        this.isListView = isListView
        notifyDataSetChanged()
    }
}
