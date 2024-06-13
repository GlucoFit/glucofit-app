package com.fitcoders.glucofitapp.view.fragment.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
    private lateinit var foodAdapter: FoodAdapter

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

        // Ambil dan pantau perubahan data gula hari ini
        homeViewModel.fetchTodaySugarIntake()
        homeViewModel.todaySugarIntake.observe(viewLifecycleOwner, Observer { totalSugar ->
            updateEmojiAndText(totalSugar)
        })

        binding.scanButton.setOnClickListener {
            val intent = Intent(requireContext(), ScannerActivity::class.java)
            startActivity(intent)
        }

/*        binding.toggleButton.setOnClickListener {
            toggleLayout()
        }*/

    }

/*    private fun toggleLayout() {
        isListLayout = !isListLayout
        FoodAdapter.setViewType(isListLayout)
    }*/

    @SuppressLint("SetTextI18n")
    private fun updateEmojiAndText(intakeGula: Int) {

        // Perbarui total gula pada TextView dengan ID @+id/sugarAmount
        val sugarAmountTextView: TextView = binding.sugarAmount
        sugarAmountTextView.text = intakeGula.toString()

        // Ambil nilai maksimum gula dari TextView
        val maxSugarTextView: TextView = binding.sugarPercentage // Ubah ke ID TextView yang benar untuk max gula
        val maxSugarString = maxSugarTextView.text.toString().replace(" g", "").trim()
        val maxSugar = maxSugarString.toIntOrNull() ?: 2000 // Default ke 100 jika parsing gagal

        val percentage = (intakeGula.toDouble() / maxSugar) * 100
        binding.sugarPercentage.text = "${percentage.toInt()}% "
        binding.tvSugarIntakeMax.text= "/ ${maxSugar} g"

        // Update emoji berdasarkan jumlah gula yang dikonsumsi
        when {
            intakeGula <= 0 -> {
                binding.emojiFace.setImageResource(R.drawable.ic_happy_large) // Emoji senang untuk gula 0 atau negatif
            }
            intakeGula in 1..(maxSugar / 2) -> {
                binding.emojiFace.setImageResource(R.drawable.ic_good_large) // Emoji baik untuk gula antara 1 dan setengah dari max
            }
            intakeGula in (maxSugar / 2 + 1) until maxSugar -> {
                binding.emojiFace.setImageResource(R.drawable.ic_angry_large) // Emoji marah untuk gula antara setengah max dan max
            }
            intakeGula >= maxSugar -> {
                binding.emojiFace.setImageResource(R.drawable.ic_angry_large) // Emoji marah untuk gula melebihi atau sama dengan max
            }
            else -> {
                binding.emojiFace.setImageResource(R.drawable.ic_happy_large) // Emoji default jika tidak ada kondisi yang cocok
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
