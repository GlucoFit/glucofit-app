package com.fitcoders.glucofitapp.view.fragment.lifestyle

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.data.LifestyleInfo
import com.fitcoders.glucofitapp.databinding.FragmentLifeStyleBinding
import com.fitcoders.glucofitapp.view.activity.assessment.AssessmentActivity
import com.fitcoders.glucofitapp.view.activity.assessment.AssessmentViewModel

class LifeStyleFragment : Fragment() {

    private var _binding: FragmentLifeStyleBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AssessmentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLifeStyleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTooltipListeners()
        validateInputsAndEnableButton()
    }

    private fun setupTooltipListeners() {
        binding.iconVegetarianInfo.setOnClickListener {
            showTooltip(it, getString(R.string.toolip_vetarian))
        }
        binding.iconVeganInfo.setOnClickListener {
            showTooltip(it, getString(R.string.toolip_paleo))
        }
        binding.iconPaleoInfo.setOnClickListener {
            showTooltip(it, getString(R.string.toolip_vegan))
        }
        binding.iconGlutenFreeInfo.setOnClickListener {
            showTooltip(it, getString(R.string.toolip_gluten))
        }
        binding.iconLowFatInfo.setOnClickListener {
            showTooltip(it, getString(R.string.toolip_lactose))
        }
        binding.iconLowFatInfo.setOnClickListener {
            showTooltip(it, getString(R.string.toolip_organik))
        }
    }

    private fun showTooltip(anchorView: View, tooltipText: String) {
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val tooltipView = inflater.inflate(R.layout.tooltip_layout, null)
        val textView: TextView = tooltipView.findViewById(R.id.tooltip_text)
        textView.text = tooltipText
        val popupWindow = PopupWindow(tooltipView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true
        popupWindow.showAsDropDown(anchorView, -2, 1)
    }

    fun validateInputs(): Boolean {
        val isRadioGroup1Checked = binding.rgSweetConsumption.checkedRadioButtonId != -1
        val isRadioGroup2Checked = binding.rgSugarIntake.checkedRadioButtonId != -1
        val isRadioGroup3Checked = binding.rgExerciseFrequency.checkedRadioButtonId != -1
        val isRadioGroup4Checked = binding.rgFoodAllergies.checkedRadioButtonId != -1
        val isCheckboxChecked = binding.cbVegetarian.isChecked || binding.cbVegan.isChecked || binding.cbPaleo.isChecked ||
                binding.cbGlutenFree.isChecked || binding.cbLowCarb.isChecked || binding.cbLowFat.isChecked ||
                binding.cbLowSodium.isChecked || binding.cbLowSugar.isChecked || binding.cbAlcoholFree.isChecked ||
                binding.cbBalanced.isChecked

        return isRadioGroup1Checked && isRadioGroup2Checked && isRadioGroup3Checked && isCheckboxChecked && isRadioGroup4Checked
    }

    private fun validateInputsAndEnableButton() {
        binding.rgSweetConsumption.setOnCheckedChangeListener { _, _ -> checkValidation() }
        binding.rgSugarIntake.setOnCheckedChangeListener { _, _ -> checkValidation() }
        binding.rgExerciseFrequency.setOnCheckedChangeListener { _, _ -> checkValidation() }
        binding.rgFoodAllergies.setOnCheckedChangeListener { _, _ -> checkValidation() }

        binding.cbVegetarian.setOnCheckedChangeListener { _, _ -> checkValidation() }
        binding.cbVegan.setOnCheckedChangeListener { _, _ -> checkValidation() }
        binding.cbPaleo.setOnCheckedChangeListener { _, _ -> checkValidation() }
        binding.cbGlutenFree.setOnCheckedChangeListener { _, _ -> checkValidation() }
        binding.cbLowCarb.setOnCheckedChangeListener { _, _ -> checkValidation() }
        binding.cbLowFat.setOnCheckedChangeListener { _, _ -> checkValidation() }
        binding.cbLowSodium.setOnCheckedChangeListener { _, _ -> checkValidation() }
        binding.cbLowSugar.setOnCheckedChangeListener { _, _ -> checkValidation() }
        binding.cbAlcoholFree.setOnCheckedChangeListener { _, _ -> checkValidation() }
        binding.cbBalanced.setOnCheckedChangeListener { _, _ -> checkValidation() }
    }

    private fun checkValidation() {
        (activity as? AssessmentActivity)?.updateSubmitButtonState()
    }

    fun collectData(): Boolean {
        return if (validateInputs()) {
            updateLifestyleChoices()
            true
        } else {
            Toast.makeText(requireContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            false
        }
    }

    private fun updateLifestyleChoices() {
        // Sweet Consumption
        val sweetConsumption = view?.findViewById<RadioButton>(binding.rgSweetConsumption.checkedRadioButtonId)?.text.toString()

        // Sugar Intake
        val sugarIntake = view?.findViewById<RadioButton>(binding.rgSugarIntake.checkedRadioButtonId)?.text.toString()

        // Exercise Frequency
        val exerciseFrequency = view?.findViewById<RadioButton>(binding.rgExerciseFrequency.checkedRadioButtonId)?.text.toString()

        // Food Allergies
        val foodAllergies = view?.findViewById<RadioButton>(binding.rgFoodAllergies.checkedRadioButtonId)?.text.toString()

        // Checkboxes for Dietary Preferences
        val selectedDietaryPreferences = mutableListOf<String>()

        if (binding.cbVegetarian.isChecked) selectedDietaryPreferences.add("Vegetarian")
        if (binding.cbVegan.isChecked) selectedDietaryPreferences.add("Vegan")
        if (binding.cbPaleo.isChecked) selectedDietaryPreferences.add("Paleo")
        if (binding.cbGlutenFree.isChecked) selectedDietaryPreferences.add("Gluten Free")
        if (binding.cbLowCarb.isChecked) selectedDietaryPreferences.add("Low Carb")
        if (binding.cbLowFat.isChecked) selectedDietaryPreferences.add("Low Fat")
        if (binding.cbLowSodium.isChecked) selectedDietaryPreferences.add("Low Sodium")
        if (binding.cbLowSugar.isChecked) selectedDietaryPreferences.add("Low Sugar")
        if (binding.cbAlcoholFree.isChecked) selectedDietaryPreferences.add("Alcohol Free")
        if (binding.cbBalanced.isChecked) selectedDietaryPreferences.add("Balanced")

        val foodPreferences = selectedDietaryPreferences.joinToString(", ")

        // Food Likes and Dislikes
        val foodLikes = binding.etFoodLikes.text.toString().trim()
        val foodDislikes = binding.etFoodDislikes.text.toString().trim()

        // Buat objek LifestyleInfo
        val lifestyleInfo = LifestyleInfo(
            sweetConsumption = sweetConsumption,
            sugarIntake = sugarIntake,
            exerciseFrequency = exerciseFrequency,
            foodPreferences = foodPreferences,
            foodAllergies = foodAllergies,
            foodLikes = foodLikes,
            foodDislikes = foodDislikes
        )

        // Update ViewModel dengan objek LifestyleInfo
        viewModel.updateLifestyleChoices(lifestyleInfo)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
