package com.fitcoders.glucofitapp.view.fragment.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.databinding.FragmentHomeBinding
import com.fitcoders.glucofitapp.utils.adapter.FoodAdapter
import com.fitcoders.glucofitapp.view.ViewModelFactory
import com.fitcoders.glucofitapp.view.activity.fooddetail.FoodDetailActivity
import com.fitcoders.glucofitapp.view.activity.scanner.ScannerActivity
import com.fitcoders.glucofitapp.view.activity.search.SearchActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var modelFactory: ViewModelFactory
    private val homeViewModel: HomeViewModel by viewModels { modelFactory }
    private var isListLayout = false
    private var maxSugar: Int = 50 // Default value set to 50

    // Store favorite status
    private val favoriteStatusMap = mutableMapOf<Int, Boolean>()

    private val foodAdapter: FoodAdapter by lazy {
        FoodAdapter(
            { item ->
                val intent = Intent(requireContext(), FoodDetailActivity::class.java)
                intent.putExtra("foodDetails", item)
                startActivity(intent)
            },
            { item, isFavorite ->
                item.id?.let {
                    homeViewModel.markAsFavorite(it, if (isFavorite) 1 else 0)
                    item.isFavorite = isFavorite // Update item favorite status locally
                    favoriteStatusMap[it] = isFavorite // Update local map
                    foodAdapter.notifyDataSetChanged() // Notify adapter to refresh the view
                }
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
        setupListeners()
        observeViewModel()

        homeViewModel.fetchTodaySugarIntake()
        homeViewModel.todaySugarIntake.observe(viewLifecycleOwner) { totalSugar ->
            updateEmojiAndText(totalSugar, maxSugar)
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

    private fun setupListeners() {
        binding.searchButton.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }

        binding.scanButton.setOnClickListener {
            val intent = Intent(requireContext(), ScannerActivity::class.java)
            startActivity(intent)
        }

        binding.toggleButton.setOnClickListener {
            toggleLayout()
        }
    }

    private fun observeViewModel() {
        homeViewModel.fetchRecommendations()
        homeViewModel.recommendationResponse.observe(viewLifecycleOwner) { result ->
            result.onSuccess { recommendations ->
                val foodList = recommendations.mapNotNull { it.foodDetails }
                foodList.forEach { item ->
                    // Sync favorite status from local map if available
                    item.isFavorite = favoriteStatusMap[item.id] ?: item.isFavorite
                }
                foodAdapter.submitList(foodList)
            }.onFailure { exception ->
                Toast.makeText(requireContext(), "Failed to load recommendations: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }

        homeViewModel.fetchUserData()
        homeViewModel.userResponse1.observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.name.text = it.userName
            }
        }

        // Observe favorite response and update local favorite status map
        homeViewModel.favoriteResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                favoriteStatusMap[it.foodId ?: 0] = it.isFavorite == true
                foodAdapter.notifyDataSetChanged()
            }
        }

        homeViewModel.fetchAssessmentData()
        homeViewModel.resultResponse.observe(viewLifecycleOwner) { assessment ->
            assessment?.let {
                maxSugar = it.result!! // Update maxSugar with the assessment result
                binding.tvSugarIntakeMax.text = "/ $maxSugar g" // Update UI with the maxSugar value
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

    private fun updateEmojiAndText(intakeGula: Int, maxSugar: Int) {
        binding.sugarAmount.text = intakeGula.toString()

        val percentage = (intakeGula.toDouble() / maxSugar) * 100
        binding.sugarPercentage.text = "${percentage.toInt()}% "
        binding.tvSugarIntakeMax.text = "/ $maxSugar g"

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
