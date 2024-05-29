package com.fitcoders.glucofitapp.view.fragment.lifestyle

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.databinding.FragmentLifeStyleBinding

class LifeStyleFragment : Fragment() {

    private var _binding: FragmentLifeStyleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLifeStyleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.iconVegetarianInfo.setOnClickListener {
            showTooltip(it, getString(R.string.toolip_vetarian))
        }

        binding.iconVeganInfo.setOnClickListener {
            showTooltip(it, getString(R.string.toolip_vegan))
        }

        binding.iconPaleoInfo.setOnClickListener {
            showTooltip(it, getString(R.string.toolip_paleo))
        }

        binding.iconGlutenFreeInfo.setOnClickListener {
            showTooltip(it, getString(R.string.toolip_gluten))
        }

        binding.iconLactoseFreeInfo.setOnClickListener {
            showTooltip(it, getString(R.string.toolip_lactose))
        }

        binding.iconOrganicInfo.setOnClickListener {
            showTooltip(it, getString(R.string.toolip_organik))
        }


    }

    private fun showTooltip(anchorView: View, tooltipText: String) {
        // Inflate layout untuk tooltip
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val tooltipView = inflater.inflate(R.layout.tooltip_layout, null)

        // Atur teks untuk tooltip
        val textView: TextView = tooltipView.findViewById(R.id.tooltip_text)
        textView.text = tooltipText

        // Buat PopupWindow
        val popupWindow = PopupWindow(
            tooltipView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        // Menutup PopupWindow ketika di-klik
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true

        // Tampilkan PopupWindow di anchorView
        popupWindow.showAsDropDown(anchorView, -2, 1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}