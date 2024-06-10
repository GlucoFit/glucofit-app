import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.data.HealthInfo
import com.fitcoders.glucofitapp.view.activity.assessment.AssessmentActivity
import com.fitcoders.glucofitapp.view.activity.assessment.AssessmentViewModel

class HealthConditionFragment : Fragment() {
    private lateinit var historyOfDiabetesRadioGroup: RadioGroup
    private lateinit var familyHistoryOfDiabetesRadioGroup: RadioGroup
    private val viewModel: AssessmentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_health_condition, container, false)

        historyOfDiabetesRadioGroup = view.findViewById(R.id.rg_history_of_diabetes)
        familyHistoryOfDiabetesRadioGroup = view.findViewById(R.id.rg_family_history_of_diabetes)

        return view
    }

    fun validateInputs(): Boolean {
        return historyOfDiabetesRadioGroup.checkedRadioButtonId != -1 &&
                familyHistoryOfDiabetesRadioGroup.checkedRadioButtonId != -1
    }

    fun collectData(): Boolean {
        return if (validateInputs()) {
            updateHealthConditions()
            true
        } else {
            Toast.makeText(requireContext(), "Please select an option for both questions", Toast.LENGTH_SHORT).show()
            false
        }
        return true
    }

    private fun updateHealthConditions() {
        // Ambil teks dari RadioButton yang dipilih
        val historyOfDiabetesChecked = view?.findViewById<RadioButton>(historyOfDiabetesRadioGroup.checkedRadioButtonId)
        val familyHistoryOfDiabetesChecked = view?.findViewById<RadioButton>(familyHistoryOfDiabetesRadioGroup.checkedRadioButtonId)

        // Pastikan kedua pilihan telah dipilih
        if (historyOfDiabetesChecked != null && familyHistoryOfDiabetesChecked != null) {
            val historyOfDiabetes = historyOfDiabetesChecked.text.toString()
            val familyHistoryOfDiabetes = familyHistoryOfDiabetesChecked.text.toString()

            // Buat objek HealthInfo yang baru
            val healthInfo = HealthInfo(
                historyOfDiabetes = historyOfDiabetes,
                familyHistoryOfDiabetes = familyHistoryOfDiabetes
            )

            // Update ViewModel dengan objek HealthInfo
            viewModel.updateHealthConditions(healthInfo)
        }
    }
}



