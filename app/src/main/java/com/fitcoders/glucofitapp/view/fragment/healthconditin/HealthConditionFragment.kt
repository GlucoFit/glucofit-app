import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.view.activity.assessment.AssessmentActivity

class HealthConditionFragment : Fragment() {
    private lateinit var checkBoxes: List<CheckBox>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_health_condition, container, false)
        checkBoxes = listOf(
            view.findViewById(R.id.cb_diabetes),
            view.findViewById(R.id.cb_hypertension),
            view.findViewById(R.id.cb_obesity),
            view.findViewById(R.id.cb_metabolic_syndrome),
            view.findViewById(R.id.cb_digestion_issues),
            view.findViewById(R.id.cb_high_cholesterol),
            view.findViewById(R.id.cb_heart_disease),
            view.findViewById(R.id.cb_special_diet),
            view.findViewById(R.id.cb_none)
        )

        return view
    }

    fun validateInputs(): Boolean {
        return checkBoxes.any { it.isChecked }
    }

    fun onNextButtonClicked() {
        if (validateInputs()) {
            (activity as AssessmentActivity).moveToNextStep()
        } else {
            Toast.makeText(requireContext(), "Please select at least one option", Toast.LENGTH_SHORT).show()
        }
    }
}

