package com.fitcoders.glucofitapp.view.activity.profile.selfassessmentresult

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import com.fitcoders.glucofitapp.R
import com.fitcoders.glucofitapp.databinding.ActivitySelfAssessmentResultBinding
import com.fitcoders.glucofitapp.response.QuestionsItem
import com.fitcoders.glucofitapp.view.ViewModelFactory
import com.fitcoders.glucofitapp.view.fragment.profile.ProfileViewModel

class SelfAssessmentResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelfAssessmentResultBinding
    private lateinit var modelFactory: ViewModelFactory
    private val assessmentResultViewModel: AssessmanResultViewModel by viewModels { modelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelfAssessmentResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        modelFactory = ViewModelFactory.getInstance(this)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        val titleText: TextView = findViewById(R.id.titleText)
        val backButton: ImageButton = findViewById(R.id.backButton)

        titleText.text = "Self Assessment"
        backButton.visibility = ImageButton.VISIBLE

        backButton.setOnClickListener {
            finish() // Go back to the previous activity
        }
    }

    private fun observeViewModel() {
        // Memulai proses pengambilan data assessment
        assessmentResultViewModel.fetchAssessmentData()

        // Mengamati perubahan pada respons assessment
        assessmentResultViewModel.resultResponse.observe(this) { assessment ->
            assessment?.let {
                binding.tvAssessmentResult.text = it.result.toString()
                binding.tvAssessmentDate.text = it.createdAt
                // Setelah mendapatkan respons, ekstraksi data questions
                assessmentResultViewModel.extractQuestions()
            }
        }

        // Mengamati perubahan pada data questions yang telah diekstraksi
        assessmentResultViewModel.questions.observe(this) { questions ->
            updateTextViewsWithQuestions(questions)
        }
    }

    private fun updateTextViewsWithQuestions(questions: List<QuestionsItem>) {
        questions.forEach { question ->
            when (question.questionId) {
                "name" -> binding.tvName.text = question.questionAnswer ?: "N/A"
                "dob" -> binding.tvDateOfBirth.text = question.questionAnswer ?: "N/A"
                "gender" -> binding.tvGender.text = question.questionAnswer ?: "N/A"
                "weight" -> binding.tvWeight.text = question.questionAnswer ?: "N/A"
                "height" -> binding.tvHeight.text = question.questionAnswer ?: "N/A"
                "historyOfDiabetes" -> binding.tvHistoryOfDiabetes.text = question.questionAnswer ?: "N/A"
                "familyHistoryOfDiabetes" -> binding.tvFamilyHistoryOfDiabetes.text = question.questionAnswer ?: "N/A"
                "sweetConsumption" -> binding.tvSweetConsumption.text = question.questionAnswer ?: "N/A"
                "sugarIntake" -> binding.tvDailySugarConsumption.text = question.questionAnswer ?: "N/A"
                "exerciseFrequency" -> binding.tvExerciseFrequency.text = question.questionAnswer ?: "N/A"
                "foodPreferences" -> binding.tvFoodPreferences.text = question.questionAnswer ?: "N/A"
                "foodAllergies" -> binding.tvFoodAllergiesOrIntolerances.text = question.questionAnswer ?: "N/A"
                "foodLikes" -> binding.tvFavoriteFoodsOrIngredients.text = question.questionAnswer ?: "N/A"
                "foodDislikes" -> binding.tvDislikedFoodsOrIngredients.text = question.questionAnswer ?: "N/A"
                else -> {
                    // Handle question IDs that don't match any known field
                    Log.w("SelfAssessmentResult", "Unknown question ID: ${question.questionId}")
                }
            }
        }
    }
}