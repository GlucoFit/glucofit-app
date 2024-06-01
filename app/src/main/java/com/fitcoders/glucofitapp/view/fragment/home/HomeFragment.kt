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
import com.fitcoders.glucofitapp.view.ViewModelFactory
import com.fitcoders.glucofitapp.view.fragment.profile.ProfileViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var modelFactory: ViewModelFactory
    private val HomeViewModel: HomeViewModel by viewModels { modelFactory }

    companion object {
        fun newInstance() = HomeFragment()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
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

        HomeViewModel.getSession().observe(viewLifecycleOwner) { user ->
            binding.name.text = user.username
        }

        // Set up RecyclerView
       /* binding.recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = FoodAdapter(getFoodRecommendations())*/

        // Set default values
        val intakeGula: Int = 4 // Misalnya ini nilai awalnya
        updateEmojiAndText(intakeGula)

        binding.scanButton.setOnClickListener {
            // Contoh scan button, update nilai intakeGula dengan hasil scan
            val updatedIntakeGula = intakeGula + 95// Misalnya ini nilai yang diperbarui dari scan
            updateEmojiAndText(updatedIntakeGula)
        }
    }

    private fun updateEmojiAndText(intakeGula: Int) {
        binding.sugarAmount.text = intakeGula.toString()
        val minSugar = 0
        val maxSugar = 100
        val rageSugar = (maxSugar/2)
        val percentage = (intakeGula.toDouble() / maxSugar) * 100
        binding.sugarPercentage.text = "${percentage.toInt()}% / $maxSugar g"

        when {
            intakeGula < minSugar-> {
                binding.emojiFace.setImageResource(R.drawable.ic_happy)
            }
            intakeGula in rageSugar..(maxSugar - 1) -> {
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