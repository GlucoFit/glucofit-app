import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.fitcoders.glucofitapp.databinding.ItemScanGridBinding
import com.fitcoders.glucofitapp.databinding.ItemScanListBinding
import com.fitcoders.glucofitapp.response.DataItem

class HistoryAdapter(
    private val itemClick: (DataItem) -> Unit,
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
                ItemHistoryGridViewHolder(binding, itemClick)
            }
            else -> {
                val binding = ItemScanListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ItemHistoryListViewHolder(binding, itemClick)
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
        private val itemClick: (DataItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: DataItem) {
            with(binding) {
                // Load the image using Glide
                Glide.with(avatarImageView.context)
                    .load(item.objectImageUrl)
                    .into(avatarImageView)

                foodName.text = item.objectName
                sugarWeight.text = item.objectSugar.toString()
                scanDate.text = item.createdAt
                scanTime.text = item.createdAt
                itemView.setOnClickListener { itemClick(item) }
            }
        }
    }

    // ViewHolder for Grid View
    class ItemHistoryGridViewHolder(
        private val binding: ItemScanGridBinding,
        private val itemClick: (DataItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: DataItem) {
            with(binding) {
                // Load the image using Glide
                Glide.with(foodImage.context)
                    .load(item.objectImageUrl)
                    .into(foodImage)

                foodName.text = item.objectName
                scanDate.text = item.createdAt
                scanTime.text = item.createdAt
                sugarWeight.text = item.objectSugar.toString()
                itemView.setOnClickListener { itemClick(item) }
            }
        }
    }
}
