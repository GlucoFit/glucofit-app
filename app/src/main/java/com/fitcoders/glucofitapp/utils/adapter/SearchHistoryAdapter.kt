package com.fitcoders.glucofitapp.utils.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fitcoders.glucofitapp.databinding.ItemSearchHistoryBinding
import com.fitcoders.glucofitapp.response.SearchHistoryResponseItem

class SearchHistoryAdapter(
    private var historyList: List<SearchHistoryResponseItem>,
    private val itemClick: (SearchHistoryResponseItem) -> Unit // Add click listener
) : RecyclerView.Adapter<SearchHistoryAdapter.SearchHistoryViewHolder>() {

    inner class SearchHistoryViewHolder(private val binding: ItemSearchHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(historyItem: SearchHistoryResponseItem) {
            binding.historyText.text = historyItem.searchText ?: "No Search Text"
            itemView.setOnClickListener {
                itemClick(historyItem) // Handle item click
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryViewHolder {
        val binding = ItemSearchHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchHistoryViewHolder, position: Int) {
        val historyItem = historyList[position]
        holder.bind(historyItem)
    }

    override fun getItemCount(): Int = historyList.size

    // Update dataset and notify adapter of changes
    fun updateData(newHistoryList: List<SearchHistoryResponseItem>) {
        this.historyList = newHistoryList
        notifyDataSetChanged()
    }
}
