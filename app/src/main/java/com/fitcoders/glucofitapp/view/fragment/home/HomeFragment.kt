package com.fitcoders.glucofitapp.view.fragment.home

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.data.Food
import com.fitcoders.glucofitapp.databinding.FragmentHomeBinding
import com.fitcoders.glucofitapp.utils.adapter.FoodAdapter
import com.fitcoders.glucofitapp.view.ViewModelFactory
import com.fitcoders.glucofitapp.view.fragment.profile.ProfileViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var modelFactory: ViewModelFactory
    private val homeViewModel: HomeViewModel by viewModels { modelFactory }
    private var isListLayout = true
    private lateinit var foodAdapter: FoodAdapter

    // Data Mock
    private val mockFoodList = listOf(
        Food(1, "Sliced meat", "sliced meat and potatoes", "https://example.com/sliced_meat.jpg", "15 g"),
        Food(2, "Pizza", "Cheesy pizza with a variety of toppings","https://example.com/pizza.jpg", "200 g"),
        Food(3, "Burger", "Juicy burger with fresh lettuce and tomato", "https://example.com/burger.jpg", "300 g"),
        Food(4, "Burger", "Juicy burger with fresh lettuce and tomato", "https://example.com/burger.jpg", "300 g"),
        Food(5, "Burger", "Juicy burger with fresh lettuce and tomato", "https://example.com/burger.jpg", "300 g")
    )

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

        homeViewModel.getSession().observe(viewLifecycleOwner) { user ->
            binding.name.text = user.username
        }

        // Set up RecyclerView with mock data
        /*foodAdapter = FoodAdapter(mockFoodList, isListLayout)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = foodAdapter*/

        // Set default values
        val intakeGula: Int = 4 // Misalnya ini nilai awalnya
        updateEmojiAndText(intakeGula)

        binding.scanButton.setOnClickListener {
            // Contoh scan button, update nilai intakeGula dengan hasil scan
            val updatedIntakeGula = intakeGula + 95 // Misalnya ini nilai yang diperbarui dari scan
            updateEmojiAndText(updatedIntakeGula)
        }

        binding.toggleButton.setOnClickListener {
            toggleLayout()
        }
    }

    private fun toggleLayout() {
        if (isListLayout) {
            binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
            binding.toggleButton.setImageResource(R.drawable.ic_window)
        } else {
            binding.recyclerView.layoutManager = LinearLayoutManager(context)
            binding.toggleButton.setImageResource(R.drawable.ic_table)
        }
        isListLayout = !isListLayout
        foodAdapter.setViewType(isListLayout)
    }

    private fun updateEmojiAndText(intakeGula: Int) {
        binding.sugarAmount.text = intakeGula.toString()
        val minSugar = 0
        val maxSugar = 100
        val rageSugar = (maxSugar / 2)
        val percentage = (intakeGula.toDouble() / maxSugar) * 100
        binding.sugarPercentage.text = "${percentage.toInt()}% / $maxSugar g"

        when {
            intakeGula < minSugar -> {
                binding.emojiFace.setImageResource(R.drawable.ic_happy)
            }
            intakeGula in rageSugar until maxSugar -> {
                binding.emojiFace.setImageResource(R.drawable.ic_good)
            }
            intakeGula >= maxSugar -> {
                binding.emojiFace.setImageResource(R.drawable.ic_angry)
            }
            else -> {
                binding.emojiFace.setImageResource(R.drawable.ic_happy)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
