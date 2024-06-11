package com.fitcoders.glucofitapp.view.fragment.history

import HistoryAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.arjungupta08.horizontal_calendar_date.HorizontalCalendarAdapter
import com.arjungupta08.horizontal_calendar_date.HorizontalCalendarSetUp
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.databinding.FragmentHistoryBinding
import com.fitcoders.glucofitapp.response.DataItem
import com.fitcoders.glucofitapp.view.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class HistoryFragment : Fragment(), HorizontalCalendarAdapter.OnItemClickListener {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var tvDateMonth: TextView
    private lateinit var ivCalendarNext: ImageView
    private lateinit var ivCalendarPrevious: ImageView

    private lateinit var modelFactory: ViewModelFactory
    private val historyViewModel: HistoryViewModel by viewModels { modelFactory }

    private val historyAdapter: HistoryAdapter by lazy {
        HistoryAdapter({ item ->
            // Handle item click
        }, false) // Initialize adapter with list view as default
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
                // Tampilkan data yang difilter ke RecyclerView
                bindHistory(dataItems)
                // Hitung total objectSugar dan tampilkan di UI
                val totalSugar = historyViewModel.calculateTotalSugar(dataItems)
                displayTotalSugar(totalSugar)
                // Update emoji dan teks berdasarkan total gula
                updateEmojiAndText(totalSugar)
            }.onFailure { exception ->
                // Handle failure, e.g., show error message
                Log.e("HistoryFragment", "Error fetching history: ${exception.message}")
            }
        }

    }

    private fun bindHistory(data: List<DataItem>) {
        historyAdapter.submitList(data)
        Log.d("HistoryFragment", "Data yang ditampilkan: $data")
    }

    private fun displayTotalSugar(totalSugar: Int) {
        val totalSugarTextView: TextView = binding.root.findViewById(R.id.tv_sugar_intake_value)
        totalSugarTextView.text = "$totalSugar"
    }

    // Method untuk memperbarui emoji dan teks berdasarkan total gula
    private fun updateEmojiAndText(intakeGula: Int) {
        val emojiImageView: ImageView = binding.root.findViewById(R.id.iv_sugar_intake)
        val percentageTextView: TextView = binding.root.findViewById(R.id.tv_sugar_intake_percentage)
        val maxSugarTextView: TextView = binding.root.findViewById(R.id.tv_sugar_intake_max)

        // Ambil nilai maksimum gula dari TextView
        val maxSugarString = maxSugarTextView.text.toString().replace(" g", "").trim()
        val maxSugar = maxSugarString.toIntOrNull() ?: 2000 // Default ke 50 jika parsing gagal
        val percentage = (intakeGula.toDouble() / maxSugar) * 100

        // Update nilai persentase dan nilai maksimum gula
        percentageTextView.text = "${percentage.toInt()}%"
        maxSugarTextView.text = "/ ${maxSugar} g"

        // Update emoji berdasarkan jumlah gula yang dikonsumsi
        when {
            intakeGula <= 0 -> {
                emojiImageView.setImageResource(R.drawable.ic_happy_small) // Emoji senang untuk gula 0 atau negatif
            }
            intakeGula in 1..(maxSugar / 2) -> {
                emojiImageView.setImageResource(R.drawable.ic_good_small) // Emoji baik untuk gula antara 1 dan setengah dari max
            }
            intakeGula in (maxSugar / 2 + 1) until maxSugar -> {
                emojiImageView.setImageResource(R.drawable.ic_angry_small) // Emoji marah untuk gula antara setengah max dan max
            }
            intakeGula >= maxSugar -> {
                emojiImageView.setImageResource(R.drawable.ic_angry_small) // Emoji marah untuk gula melebihi atau sama dengan max
            }
            else -> {
                emojiImageView.setImageResource(R.drawable.ic_happy_small) // Emoji default jika tidak ada kondisi yang cocok
            }
        }
    }

    override fun onItemClick(ddMmYy: String, dd: String, day: String) {
        Log.d("HistoryFragment", "Tanggal yang dipilih: $ddMmYy")

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

    companion object {
        @JvmStatic
        fun newInstance() = HistoryFragment()
    }
}