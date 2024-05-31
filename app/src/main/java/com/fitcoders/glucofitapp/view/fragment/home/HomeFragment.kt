package com.fitcoders.glucofitapp.view.fragment.home

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
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

        // Set up RecyclerView
       /* binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = FoodAdapter(getFoodRecommendations())*/

        // Set default values
        val intakeGula: Int = 5 // Misalnya ini nilai awalnya
        updateEmojiAndText(intakeGula)

        binding.scanButton.setOnClickListener {
            // Contoh scan button, update nilai intakeGula dengan hasil scan
            val updatedIntakeGula = intakeGula + 60 // Misalnya ini nilai yang diperbarui dari scan
            updateEmojiAndText(updatedIntakeGula)
        }
    }

    private fun updateEmojiAndText(intakeGula: Int) {
        binding.sugarAmount.text = intakeGula.toString()
        val maxSugar = 100
        val percentage = (intakeGula.toDouble() / maxSugar) * 100
        binding.sugarPercentage.text = "${percentage.toInt()}% / $maxSugar g"

        when {
            intakeGula < 60 -> {
                binding.emojiFace.setImageResource(R.drawable.ic_happy)
            }
            intakeGula in 60..90 -> {
                binding.emojiFace.setImageResource(R.drawable.ic_good)
            }
            intakeGula > 100 -> {
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