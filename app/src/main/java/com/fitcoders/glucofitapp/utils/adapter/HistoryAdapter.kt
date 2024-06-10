package com.fitcoders.glucofitapp.utils.adapter

import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.data.History
import com.fitcoders.glucofitapp.databinding.FragmentHistoryBinding
import com.fitcoders.glucofitapp.databinding.ItemScanListBinding
import com.fitcoders.glucofitapp.utils.ResultWrapper
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter(
    private val itemClick: (History) -> Unit,
//    private val listMode: Int,
//    private var isListView: Boolean
) : RecyclerView.Adapter<HistoryAdapter.ItemHistoryViewHolder>() {

    private val VIEW_TYPE_LIST = 1
    private val VIEW_TYPE_GRID = 2
    private val dataDiffer =
        AsyncListDiffer(
            this,
            object : DiffUtil.ItemCallback<History>() {
                override fun areItemsTheSame(
                    oldItem: History,
                    newItem: History
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: History,
                    newItem: History
                ): Boolean {
                    return oldItem.hashCode() == newItem.hashCode()
                }
            }
        )

    fun submitData(data: List<History>) {
        dataDiffer.submitList(data)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHistoryViewHolder {
        val binding = ItemScanListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ItemHistoryViewHolder(binding, itemClick)
    }

    override fun onBindViewHolder(holder: ItemHistoryViewHolder, position: Int) {
        holder.bindView(dataDiffer.currentList[position])
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    class ItemHistoryViewHolder(
        private val binding: ItemScanListBinding,
        val itemClick: (History) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: History) {
            with(item) {
                binding.avatarImageView.load(item.objectImageUrl) {
                    crossfade(true)
                }
                binding.foodName.text = item.objectName
                binding.sugarWeight.text = item.objectSugar.toString()
                binding.scanDate.text = item.createdAt
                binding.scanTime.text = item.createdAt
                itemView.setOnClickListener { itemClick(this) }
            }
        }
//        private fun formatCreatedAt(createdAt: String): Pair<String, String> {
//            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
//            val date = inputFormat.parse(createdAt)
//
//            val dateFormat = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())
//            val timeFormat = SimpleDateFormat("HH.mm 'WIB'", Locale.getDefault())
//
//            val formattedDate = dateFormat.format(date)
//            val formattedTime = timeFormat.format(date)
//
//            return Pair(formattedDate, formattedTime)
//        }
    }


}
