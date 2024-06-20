package com.fitcoders.glucofitapp.view.fragment.history

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.arjungupta08.horizontal_calendar_date.HorizontalCalendarAdapter
import com.arjungupta08.horizontal_calendar_date.HorizontalCalendarSetUp
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.databinding.FragmentHistoryBinding
import com.fitcoders.glucofitapp.response.DataItem
import com.fitcoders.glucofitapp.utils.adapter.HistoryAdapter
import com.fitcoders.glucofitapp.view.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryFragment : Fragment(), HorizontalCalendarAdapter.OnItemClickListener {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var tvDateMonth: TextView
    private lateinit var ivCalendarNext: ImageView
    private lateinit var ivCalendarPrevious: ImageView

    private lateinit var modelFactory: ViewModelFactory
    private val historyViewModel: HistoryViewModel by viewModels { modelFactory }

    private val historyAdapter: HistoryAdapter by lazy {
        HistoryAdapter(
            { item ->
                // Handle item click
            },
            { item ->
                // Handle item delete
                promptDeleteConfirmation(item)
            },
            false // Initialize adapter with list view as default
        )
    }

    private var isListLayout = true // Default to list view

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        modelFactory = ViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHistory()
        observeViewModel()

        val titleText: TextView = binding.root.findViewById(R.id.titleText)
        val backButton: ImageButton = binding.root.findViewById(R.id.backButton)

        titleText.text = "Scan History"
        backButton.visibility = View.GONE

        tvDateMonth = binding.root.findViewById(R.id.text_date_month)
        ivCalendarNext = binding.root.findViewById(R.id.iv_calendar_next)
        ivCalendarPrevious = binding.root.findViewById(R.id.iv_calendar_previous)

        setupCalendar()

        binding.toggleButton.setOnClickListener {
            toggleLayout()
        }
    }

    private fun setupCalendar() {
        val calendarSetUp = HorizontalCalendarSetUp()
        val tvMonth = calendarSetUp.setUpCalendarAdapter(binding.recyclerViewDate, this)
        tvDateMonth.text = tvMonth

        calendarSetUp.setUpCalendarPrevNextClickListener(ivCalendarNext, ivCalendarPrevious, this) {
            tvDateMonth.text = it
        }
    }

    private fun toggleLayout() {
        isListLayout = !isListLayout
        historyAdapter.setViewType(!isListLayout) // Update adapter with the new layout state

        if (isListLayout) {
            binding.recyclerViewScanHistory.layoutManager = LinearLayoutManager(context)
            binding.toggleButton.setImageResource(R.drawable.ic_window)
        } else {
            binding.recyclerViewScanHistory.layoutManager = GridLayoutManager(context, 2)
            binding.toggleButton.setImageResource(R.drawable.ic_table)
        }
    }

    private fun setupHistory() {
        binding.recyclerViewScanHistory.apply {
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(context) // Set to list view initially
        }
    }

    private fun observeViewModel() {
        historyViewModel.scanHistoryResponse.observe(viewLifecycleOwner) { result ->
            result.onSuccess { dataItems ->
                if (dataItems.isEmpty()) {
                    bindHistory(dataItems)
                    val totalSugar = historyViewModel.calculateTotalSugar(dataItems)
                    displayTotalSugar(totalSugar)
                    updateEmojiAndText(totalSugar)
                    showEmptyView()
                } else {
                    hideEmptyView()
                    bindHistory(dataItems)
                    val totalSugar = historyViewModel.calculateTotalSugar(dataItems)
                    displayTotalSugar(totalSugar)
                    updateEmojiAndText(totalSugar)
                }
            }.onFailure { exception ->
                Log.e("HistoryFragment", "Error fetching history: ${exception.message}")
                showEmptyView()
            }
        }

        // Observe delete response
        historyViewModel.deleteResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                Log.d("HistoryFragment", "Delete Response: ${it.message}")
                // Refresh the list after a deletion by fetching the data again
                historyViewModel.fetchScanHistoryByDate(getCurrentDate())
            }
        }
    }

    private fun showEmptyView() {
        binding.recyclerViewScanHistory.visibility = View.GONE
        binding.llScanNotFound.visibility = View.VISIBLE // Show empty view
    }

    private fun hideEmptyView() {
        binding.recyclerViewScanHistory.visibility = View.VISIBLE
        binding.llScanNotFound.visibility = View.GONE // Hide empty view
    }

    private fun bindHistory(data: List<DataItem>) {
        historyAdapter.submitList(data)
        Log.d("HistoryFragment", "Data displayed: $data")
    }

    private fun displayTotalSugar(totalSugar: Int) {
        val totalSugarTextView: TextView = binding.root.findViewById(R.id.tv_sugar_intake_value)
        totalSugarTextView.text = "$totalSugar"
    }

    private fun updateEmojiAndText(intakeGula: Int) {
        val emojiImageView: ImageView = binding.root.findViewById(R.id.iv_sugar_intake)
        val percentageTextView: TextView = binding.root.findViewById(R.id.tv_sugar_intake_percentage)
        val maxSugarTextView: TextView = binding.root.findViewById(R.id.tv_sugar_intake_max)

        val maxSugarString = maxSugarTextView.text.toString().replace(" g", "").trim()
        val maxSugar = maxSugarString.toIntOrNull() ?: 50
        val percentage = (intakeGula.toDouble() / maxSugar) * 100

        percentageTextView.text = "${percentage.toInt()}%"
        maxSugarTextView.text = "/ ${maxSugar} g"

        when {
            intakeGula <= 0 -> {
                emojiImageView.setImageResource(R.drawable.ic_happy_small)
            }
            intakeGula in 1..(maxSugar / 2) -> {
                emojiImageView.setImageResource(R.drawable.ic_good_small)
            }
            intakeGula in (maxSugar / 2 + 1) until maxSugar -> {
                emojiImageView.setImageResource(R.drawable.ic_angry_small)
            }
            intakeGula >= maxSugar -> {
                emojiImageView.setImageResource(R.drawable.ic_angry_small)
            }
            else -> {
                emojiImageView.setImageResource(R.drawable.ic_happy_small)
            }
        }
    }

    override fun onItemClick(ddMmYy: String, dd: String, day: String) {
        Log.d("HistoryFragment", "Selected date: $ddMmYy")

        val convertedDate = convertDateToIsoFormat(ddMmYy)

        historyViewModel.fetchScanHistoryByDate(convertedDate)
    }

    private fun convertDateToIsoFormat(date: String): String {
        val inputFormat = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return try {
            val parsedDate = inputFormat.parse(date)
            outputFormat.format(parsedDate!!)
        } catch (e: Exception) {
            Log.e("HistoryFragment", "Error converting date format: ${e.message}")
            date // Return the original date if parsing fails
        }
    }

    private fun promptDeleteConfirmation(item: DataItem) {

        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.custom_alert_layout)
        dialog.window?.setBackgroundDrawable(requireActivity().getDrawable(R.drawable.shape_rounded_25dp))
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.setCancelable(true)


        val btnNo: Button = dialog.findViewById(R.id.btnNo)
        val btnYes: Button = dialog.findViewById(R.id.btnYes)
        val tvMessage: TextView = dialog.findViewById(R.id.tvMessage)


        tvMessage.text = "Are you sure you want to delete this item?"
        btnYes.text = "Yes, remove"
        btnNo.text = "No"

        btnNo.setOnClickListener {
            dialog.dismiss()
            setWindowTransparency(false)
        }

        btnYes.setOnClickListener {
            dialog.dismiss()
            item.id?.let { historyViewModel.deleteScanHistoryById(it) }
            setWindowTransparency(false)
        }

        dialog.setOnCancelListener {
            setWindowTransparency(false)
        }

        dialog.show()
        setWindowTransparency(true)

    }

    private fun setWindowTransparency(isTransparent: Boolean) {
        val window = requireActivity().window
        val params = window.attributes
        params.alpha = if (isTransparent) 0.5f else 1.0f
        window.attributes = params
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    companion object {
        @JvmStatic
        fun newInstance() = HistoryFragment()
    }
}