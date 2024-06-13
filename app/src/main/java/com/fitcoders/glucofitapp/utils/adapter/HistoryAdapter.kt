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
                sugarWeight.text = item.objectSugar.toString()
                sugarUnit.text = "g"
                // Convert UTC time to local time
                val localCreatedAt = convertUtcToLocalTime(item.createdAt ?: "")
                scanDate.text = localCreatedAt
                scanTime.text = localCreatedAt

                // Handle item click
                itemView.setOnClickListener { itemClick(item) }

                // Handle delete click
                trashIcon.setOnClickListener { itemDelete(item) } // Correct delete action
            }
        }

        private fun convertUtcToLocalTime(utcTime: String): String {
            // Format time provided in UTC
            val utcFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            utcFormat.timeZone = TimeZone.getTimeZone("UTC")

            // Parse time in UTC
            val date: Date? = utcFormat.parse(utcTime)

            // Format time for local time zone
            val localFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            localFormat.timeZone = TimeZone.getDefault() // Set to device's local time zone

            // Convert and return time in local format
            return date?.let { localFormat.format(it) } ?: utcTime
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
                val localCreatedAt = convertUtcToLocalTime(item.createdAt ?: "")
                scanDate.text = localCreatedAt
                scanTime.text = localCreatedAt
                sugarUnit.text = "g"
                sugarWeight.text = item.objectSugar.toString()

                // Handle item click
                itemView.setOnClickListener { itemClick(item) }


                // Handle delete click
                trashIcon.setOnClickListener { itemDelete(item) } // Correct delete action
            }
        }

        private fun convertUtcToLocalTime(utcTime: String): String {
            // Format time provided in UTC
            val utcFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            utcFormat.timeZone = TimeZone.getTimeZone("UTC")

            // Parse time in UTC
            val date: Date? = utcFormat.parse(utcTime)

            // Format time for local time zone
            val localFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            localFormat.timeZone = TimeZone.getDefault() // Set to device's local time zone

            // Convert and return time in local format
            return date?.let { localFormat.format(it) } ?: utcTime
        }
    }
}