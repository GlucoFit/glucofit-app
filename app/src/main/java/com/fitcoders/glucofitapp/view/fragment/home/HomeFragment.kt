package com.fitcoders.glucofitapp.view.fragment.home

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.databinding.FragmentHomeBinding
import com.fitcoders.glucofitapp.utils.adapter.FoodAdapter
import com.fitcoders.glucofitapp.view.ViewModelFactory
import com.fitcoders.glucofitapp.view.activity.fooddetail.FoodDetailActivity
import com.fitcoders.glucofitapp.view.activity.scanner.ScannerActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var modelFactory: ViewModelFactory
    private val homeViewModel: HomeViewModel by viewModels { modelFactory }
    private var isListLayout = true

    private val foodAdapter: FoodAdapter by lazy {
        FoodAdapter(
            { item ->
                // Aksi ketika item diklik, misalnya membuka detail makanan
                val intent = Intent(requireContext(), FoodDetailActivity::class.java)
                intent.putExtra("foodDetails", item)
                startActivity(intent)
            },
            isListView = isListLayout
        )
    }

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        modelFactory = ViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        homeViewModel.getSession().observe(viewLifecycleOwner) { user ->
            binding.name.text = user.username
        }

        observeViewModel()

        homeViewModel.fetchTodaySugarIntake()
        homeViewModel.todaySugarIntake.observe(viewLifecycleOwner, { totalSugar ->
            updateEmojiAndText(totalSugar)
        })

        binding.scanButton.setOnClickListener {
            val intent = Intent(requireContext(), ScannerActivity::class.java)
            startActivity(intent)
        }

        binding.toggleButton.setOnClickListener {
            toggleLayout()
        }
    }

    private fun setupRecyclerView() {
        binding.foodRecommendationRecyclerView.apply {
            adapter = foodAdapter
            layoutManager = if (isListLayout) {
                LinearLayoutManager(context)
            } else {
                GridLayoutManager(context, 2)
            }
        }
    }

    private fun observeViewModel() {
        homeViewModel.fetchRecommendations()
        homeViewModel.recommendationResponse.observe(viewLifecycleOwner) { result ->
            result.onSuccess { recommendations ->
                Log.d(TAG, "Recommendations received: ${recommendations.size}")

                // Mengatur daftar item ke adapter
                val foodList = recommendations.mapNotNull { it.foodDetails }
                Log.d(TAG, "Food list generated for adapter: ${foodList.size}")
                foodAdapter.submitList(foodList)
            }.onFailure { exception ->
                Log.e(TAG, "Error fetching recommendations", exception)
                Toast.makeText(requireContext(), "Failed to load recommendations: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun toggleLayout() {
        isListLayout = !isListLayout
        foodAdapter.setViewType(isListLayout)

        binding.foodRecommendationRecyclerView.layoutManager = if (isListLayout) {
            LinearLayoutManager(context)
        } else {
            GridLayoutManager(context, 2)
        }

        binding.toggleButton.setImageResource(
            if (isListLayout) R.drawable.ic_window else R.drawable.ic_table
        )
    }

    @SuppressLint("SetTextI18n")
    private fun updateEmojiAndText(intakeGula: Int) {
        val sugarAmountTextView: TextView = binding.sugarAmount
        sugarAmountTextView.text = intakeGula.toString()

        val maxSugarTextView: TextView = binding.sugarPercentage
        val maxSugarString = maxSugarTextView.text.toString().replace(" g", "").trim()
        val maxSugar = maxSugarString.toIntOrNull() ?: 2000

        val percentage = (intakeGula.toDouble() / maxSugar) * 100
        binding.sugarPercentage.text = "${percentage.toInt()}% "
        binding.tvSugarIntakeMax.text = "/ ${maxSugar} g"

        when {
            intakeGula <= 0 -> {
                binding.emojiFace.setImageResource(R.drawable.ic_happy_large)
            }
            intakeGula in 1..(maxSugar / 2) -> {
                binding.emojiFace.setImageResource(R.drawable.ic_good_large)
            }
            intakeGula in (maxSugar / 2 + 1) until maxSugar -> {
                binding.emojiFace.setImageResource(R.drawable.ic_angry_large)
            }
            intakeGula >= maxSugar -> {
                binding.emojiFace.setImageResource(R.drawable.ic_angry_large)
            }
            else -> {
                binding.emojiFace.setImageResource(R.drawable.ic_happy_large)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


