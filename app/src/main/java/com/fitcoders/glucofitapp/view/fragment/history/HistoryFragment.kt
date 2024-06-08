    package com.fitcoders.glucofitapp.view.fragment.history

    import android.content.Intent
    import android.os.Bundle
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.ImageButton
    import android.widget.ImageView
    import android.widget.TextView
    import androidx.fragment.app.Fragment
    import androidx.fragment.app.viewModels
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import androidx.recyclerview.widget.GridLayoutManager
    import com.fitcoders.glucofitapp.R
    import com.fitcoders.glucofitapp.databinding.FragmentHistoryBinding
    import com.fitcoders.glucofitapp.view.activity.main.MainActivity
    import com.fitcoders.glucofitapp.utils.adapter.HistoryAdapter
    import com.arjungupta08.horizontal_calendar_date.HorizontalCalendarAdapter
    import com.arjungupta08.horizontal_calendar_date.HorizontalCalendarSetUp
    import com.fitcoders.glucofitapp.data.History

    class HistoryFragment : Fragment(), HorizontalCalendarAdapter.OnItemClickListener {

        private lateinit var recyclerView: RecyclerView
        private lateinit var tvDateMonth: TextView
        private lateinit var ivCalendarNext: ImageView
        private lateinit var ivCalendarPrevious: ImageView

        private val viewModel: HistoryViewModel by viewModels()
        private var _binding: FragmentHistoryBinding? = null
        private val binding get() = _binding!!
        private var isListLayout = true
        private lateinit var historyAdapter: HistoryAdapter

        private val mockHistoryList = listOf(
            History(1, "Sliced meat", "2021-09-01",  "12:00", "https://example.com/sliced_meat.jpg", "15 g"),
            History(2, "Pizza", "2021-09-02", "13:00", "https://example.com/pizza.jpg", "200 g"),
            History(3, "Burger", "2021-09-03", "14:00", "https://example.com/burger.jpg", "300 g"),
            History(4, "Burger", "2021-09-04", "15:00", "https://example.com/burger.jpg", "300 g"),
            History(5, "Burger", "2021-09-05", "16:00", "https://example.com/burger.jpg", "300 g")
        )

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = FragmentHistoryBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            val titleText: TextView = binding.root.findViewById(R.id.titleText)
            val backButton: ImageButton = binding.root.findViewById(R.id.backButton)

            titleText.text = "Scan History"
            backButton.visibility = View.GONE

            backButton.setOnClickListener {
                startActivity(Intent(activity, MainActivity::class.java))
            }

            tvDateMonth = binding.root.findViewById(R.id.text_date_month)
            ivCalendarNext = binding.root.findViewById(R.id.iv_calendar_next)
            ivCalendarPrevious = binding.root.findViewById(R.id.iv_calendar_previous)

            recyclerView = binding.recyclerViewDate
            recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            val calendarSetUp = HorizontalCalendarSetUp()
            val tvMonth = calendarSetUp.setUpCalendarAdapter(recyclerView, this@HistoryFragment)
            tvDateMonth.text = tvMonth

            calendarSetUp.setUpCalendarPrevNextClickListener(ivCalendarNext, ivCalendarPrevious, this@HistoryFragment) {
                tvDateMonth.text = it
            }

            historyAdapter = HistoryAdapter(mockHistoryList, isListLayout)
            binding.recyclerViewScanHistory.layoutManager = LinearLayoutManager(context)
            binding.recyclerViewScanHistory.adapter = historyAdapter

            binding.toggleButton.setOnClickListener {
                toggleLayout()
            }
        }

        private fun toggleLayout() {
            if (isListLayout) {
                binding.recyclerViewScanHistory.layoutManager = GridLayoutManager(context, 2)
                binding.toggleButton.setImageResource(R.drawable.ic_window)
            } else {
                binding.recyclerViewScanHistory.layoutManager = LinearLayoutManager(context)
                binding.toggleButton.setImageResource(R.drawable.ic_table)
            }
            isListLayout = !isListLayout
            historyAdapter.setViewType(isListLayout)
        }

        override fun onItemClick(ddMmYy: String, dd: String, day: String) {
    /*        binding.root.findViewById<TextView>(R.id.selectedDate).text =
                getString(R.string.selected_date, ddMmYy)
            binding.root.findViewById<TextView>(R.id.selectedDD).text =
                getString(R.string.selected_dd, dd)
            binding.root.findViewById<TextView>(R.id.selectedDay).text =
                getString(R.string.selected_day, day)*/
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }

        companion object {
            @JvmStatic
            fun newInstance() = HistoryFragment()
        }
    }
