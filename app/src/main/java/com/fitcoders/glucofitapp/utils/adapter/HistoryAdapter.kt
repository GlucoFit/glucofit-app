package com.fitcoders.glucofitapp.utils.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitcoders.glucofitapp.databinding.ItemScanGridBinding
import com.fitcoders.glucofitapp.databinding.ItemScanListBinding
import com.fitcoders.glucofitapp.response.DataItem
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone


class HistoryAdapter(
    private val itemClick: (DataItem) -> Unit,
    private val itemDelete: (DataItem) -> Unit, // Correct type for delete action
    private var isGridView: Boolean = false // Parameter to toggle between list and grid view
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_LIST = 1
        private const val VIEW_TYPE_GRID = 2
    }

    private val dataDiffer = AsyncListDiffer(
        this,
        object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }
        }
    )

    fun submitList(data: List<DataItem>) {
        dataDiffer.submitList(data)
    }

    fun setViewType(isGridView: Boolean) {
        this.isGridView = isGridView
        notifyDataSetChanged() // Notify the adapter to refresh the layout
    }

    override fun getItemViewType(position: Int): Int {
        return if (isGridView) VIEW_TYPE_GRID else VIEW_TYPE_LIST
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_GRID -> {
                val binding = ItemScanGridBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ItemHistoryGridViewHolder(binding, itemClick, itemDelete) // Pass delete action
            }
            else -> {
                val binding = ItemScanListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ItemHistoryListViewHolder(binding, itemClick, itemDelete) // Pass delete action
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataDiffer.currentList[position]
        when (holder) {
            is ItemHistoryListViewHolder -> holder.bindView(item)
            is ItemHistoryGridViewHolder -> holder.bindView(item)
        }
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    // ViewHolder for List View
    class ItemHistoryListViewHolder(
        private val binding: ItemScanListBinding,
        private val itemClick: (DataItem) -> Unit,
        private val itemDelete: (DataItem) -> Unit // Correct type for delete action
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: DataItem) {
            with(binding) {
                // Load the image using Glide
                Glide.with(avatarImageView.context)
                    .load(item.objectImageUrl)
                    .into(avatarImageView)

                foodName.text = item.objectName

                // Convert UTC time to local time
                val localDateTime = convertUtcToLocalTime(item.createdAt ?: "")

                // Format only the date part
                val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                scanDate.text = localDateTime.toLocalDate().format(dateFormatter)

                // Format only the time part
                val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
                scanTime.text = localDateTime.toLocalTime().format(timeFormatter)

                sugarUnit.text = "g"
                sugarWeight.text = item.objectSugar.toString()

                // Handle item click
                itemView.setOnClickListener { itemClick(item) }

                // Handle delete click
                trashIcon.setOnClickListener { itemDelete(item) }
            }
        }

        private fun convertUtcToLocalTime(utcTime: String): ZonedDateTime {
            val utcFormatter = DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.of("UTC"))
            val localZone = ZoneId.systemDefault()

            // Parse the UTC time and convert to local ZonedDateTime
            return ZonedDateTime.parse(utcTime, utcFormatter).withZoneSameInstant(localZone)
        }
    }

    // ViewHolder for Grid View
    class ItemHistoryGridViewHolder(
        private val binding: ItemScanGridBinding,
        private val itemClick: (DataItem) -> Unit,
        private val itemDelete: (DataItem) -> Unit // Correct type for delete action
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: DataItem) {
            with(binding) {
                // Load the image using Glide
                Glide.with(foodImage.context)
                    .load(item.objectImageUrl)
                    .into(foodImage)

                foodName.text = item.objectName

                // Convert UTC time to local time
                val localDateTime = convertUtcToLocalTime(item.createdAt ?: "")

                // Format only the date part
                val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                scanDate.text = localDateTime.toLocalDate().format(dateFormatter)

                // Format only the time part
                val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
                scanTime.text = localDateTime.toLocalTime().format(timeFormatter)

                sugarUnit.text = "g"
                sugarWeight.text = item.objectSugar.toString()

                // Handle item click
                itemView.setOnClickListener { itemClick(item) }

                // Handle delete click
                trashIcon.setOnClickListener { itemDelete(item) }
            }
        }

        private fun convertUtcToLocalTime(utcTime: String): ZonedDateTime {
            val utcFormatter = DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.of("UTC"))
            val localZone = ZoneId.systemDefault()

            // Parse the UTC time and convert to local ZonedDateTime
            return ZonedDateTime.parse(utcTime, utcFormatter).withZoneSameInstant(localZone)
        }
    }
}