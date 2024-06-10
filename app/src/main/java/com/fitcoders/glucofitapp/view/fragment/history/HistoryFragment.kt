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
    import com.fitcoders.glucofitapp.data.datasource.history.ScanHistoryApiDataSource
    import com.fitcoders.glucofitapp.data.datasource.history.ScanHistoryApiDataSourceImpl
    import com.fitcoders.glucofitapp.data.repository.ScanHistoryRepository
    import com.fitcoders.glucofitapp.data.repository.ScanHistoryRepositoryImpl
    import com.fitcoders.glucofitapp.service.ApiConfig
    import com.fitcoders.glucofitapp.service.ApiService
    import com.fitcoders.glucofitapp.utils.GenericViewModelFactory
    import com.fitcoders.glucofitapp.utils.ResultWrapper
    import com.fitcoders.glucofitapp.utils.proceedWhen

    class HistoryFragment : Fragment(), HorizontalCalendarAdapter.OnItemClickListener {

        private lateinit var recyclerView: RecyclerView
        private lateinit var tvDateMonth: TextView
        private lateinit var ivCalendarNext: ImageView
        private lateinit var ivCalendarPrevious: ImageView
        private lateinit var binding: FragmentHistoryBinding
        private val viewModel: HistoryViewModel by viewModels {
            val service = ApiService.invoke()
            val ds = ScanHistoryApiDataSourceImpl(service)
            val repo : ScanHistoryRepository = ScanHistoryRepositoryImpl(ds)
            GenericViewModelFactory.create(HistoryViewModel(repo))
        }
        private val historyAdapter : HistoryAdapter by lazy {
            HistoryAdapter {
                // item click
            }
        }
        private var isListLayout = true


        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            binding = FragmentHistoryBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            val titleText: TextView = binding.root.findViewById(R.id.titleText)
            val backButton: ImageButton = binding.root.findViewById(R.id.backButton)
            setupHistory()
            getHistoryData()
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

        }

        private fun getHistoryData(){
            viewModel.getHistory().observe(viewLifecycleOwner){
                it.proceedWhen(
                    doOnSuccess = {
                        it.payload?.let {
                            bindHistory(it)
                        }
                    }
                )
            }

        }
        private fun bindHistory(data : List<History>){
            historyAdapter.submitData(data)
        }
        private fun setupHistory(){
            binding.recyclerViewScanHistory.apply{
                adapter = historyAdapter
            }
        }

        companion object {
            @JvmStatic
            fun newInstance() = HistoryFragment()
        }
    }
