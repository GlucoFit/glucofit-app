package com.fitcoders.glucofitapp.utils.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fitcoders.glucofitapp.R
import kotlin.random.Random

class DietLabelsAdapter(
    private val dietLabels: List<String>,
    private val context: Context
) : RecyclerView.Adapter<DietLabelsAdapter.DietLabelViewHolder>() {

    // Define a list of possible colors
    private val borderColors = listOf(
        "#FFEB3B", // Yellow
        "#8BC34A", // Green
        "#FF9800", // Orange
        "#F44336", // Red
        "#3F51B5", // Blue
        "#9C27B0", // Purple
        "#00BCD4", // Teal
        "#795548"  // Brown
    )

    class DietLabelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dietLabelTextView: TextView = view.findViewById(R.id.nama_label)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DietLabelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.diet_labels_template, parent, false)
        return DietLabelViewHolder(view)
    }

    override fun onBindViewHolder(holder: DietLabelViewHolder, position: Int) {
        val label = dietLabels[position]
        holder.dietLabelTextView.text = label

        // Randomly select a color for the border and text
        val randomColor = borderColors.random()

        // Apply the drawable and set the border color dynamically
        val drawable = ContextCompat.getDrawable(context, R.drawable.shape_15dp_rounded) as GradientDrawable
        drawable.setStroke(5, Color.parseColor(randomColor))

        // Set the background to the TextView
        holder.dietLabelTextView.background = drawable

        // Set the text color to match the border color
        holder.dietLabelTextView.setTextColor(Color.parseColor(randomColor))
    }

    override fun getItemCount(): Int {
        return dietLabels.size
    }
}
