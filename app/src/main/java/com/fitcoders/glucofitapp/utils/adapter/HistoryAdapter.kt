package com.fitcoders.glucofitapp.utils.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.data.History

class HistoryAdapter(private var historyList: List<History>, private var isListView: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private val VIEW_TYPE_LIST = 1
        private val VIEW_TYPE_GRID = 2

        inner class HorizontalCalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
/*          val dateMonth: TextView = itemView.findViewById(R.id.tv_date_month)
            val calendarNext: ImageView = itemView.findViewById(R.id.iv_calendar_next)
            val calendarPrevious: ImageView = itemView.findViewById(R.id.iv_calendar_previous)*/
            val horizontalCalendar: RecyclerView = itemView.findViewById(R.id.recyclerView)
        }

        inner class HistoryListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val foodImage: ImageView = itemView.findViewById(R.id.avatarImageView)
            val foodName: TextView = itemView.findViewById(R.id.food_name)
            val scanDate: TextView = itemView.findViewById(R.id.scan_date)
            val scanTime: TextView = itemView.findViewById(R.id.scan_time)
            var sugarWeight: TextView = itemView.findViewById(R.id.sugar_weight)
        }

        inner class HistoryGridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val foodImage: ImageView = itemView.findViewById(R.id.food_image)
            val foodName: TextView = itemView.findViewById(R.id.food_name)
            val scanDate: TextView = itemView.findViewById(R.id.scan_date)
            val scanTime: TextView = itemView.findViewById(R.id.scan_time)
            var sugarWeight: TextView = itemView.findViewById(R.id.sugar_weight)
        }

        override fun getItemViewType(position: Int): Int {
            return if (isListView) VIEW_TYPE_LIST else VIEW_TYPE_GRID
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return if (viewType == VIEW_TYPE_LIST) {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_scan_list, parent, false)
                HistoryListViewHolder(view)
            } else {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_scan_grid, parent, false)
                HistoryGridViewHolder(view)
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val history = historyList[position]
            if (holder is HistoryListViewHolder) {
                holder.foodName.text = history.foodName
                holder.scanDate.text = history.date
                holder.scanTime.text = history.time
                holder.sugarWeight.text = history.sugarWeight
                Glide.with(holder.itemView.context).load(history.imageUrl).into(holder.foodImage)
            } else if (holder is HistoryGridViewHolder) {
                holder.foodName.text = history.foodName
                holder.scanDate.text = history.date
                holder.scanTime.text = history.time
                holder.sugarWeight.text = history.sugarWeight
                Glide.with(holder.itemView.context).load(history.imageUrl).into(holder.foodImage)
            }
        }

        override fun getItemCount(): Int = historyList.size

        fun setViewType(isListView: Boolean) {
            this.isListView = isListView
            notifyDataSetChanged()
        }
}