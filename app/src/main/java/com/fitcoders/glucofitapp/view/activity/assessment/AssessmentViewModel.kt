package com.fitcoders.glucofitapp.view.activity.assessment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitcoders.glucofitapp.data.AppRepository
import com.fitcoders.glucofitapp.data.HealthInfo
import com.fitcoders.glucofitapp.data.LifestyleInfo
import com.fitcoders.glucofitapp.data.UserData
import com.fitcoders.glucofitapp.utils.Event
import kotlinx.coroutines.launch



class AssessmentViewModel(private val repository: AppRepository) : ViewModel() {
    val userInformation: LiveData<UserData> = MutableLiveData()
    val healthConditions: LiveData<HealthInfo> = MutableLiveData()
    val lifestyleChoices: LiveData<LifestyleInfo> = MutableLiveData()
    val toastText: LiveData<Event<String>> = repository.toastText


    fun updateUserInformation(userData: UserData) {
        (userInformation as MutableLiveData).value = userData
    }

    fun updateHealthConditions(conditions: HealthInfo) {
        (healthConditions as MutableLiveData).value = conditions
    }

    fun updateLifestyleChoices(choices: LifestyleInfo) {
        (lifestyleChoices as MutableLiveData).value = choices
    }

    fun submitAssessment() {
        viewModelScope.launch {
            // Dapatkan data dari LiveData
            val userInfo = userInformation.value
            val healthInfo = healthConditions.value
            val lifestyleInfo = lifestyleChoices.value

            // Log data untuk debugging
            Log.d("AssessmentViewModel", "userInfo: $userInfo")
            Log.d("AssessmentViewModel", "healthInfo: $healthInfo")
            Log.d("AssessmentViewModel", "lifestyleInfo: $lifestyleInfo")

            // Periksa apakah semua data sudah lengkap
            if (userInfo != null && healthInfo != null && lifestyleInfo != null) {
                // Ekstraksi data dari objek
                val name = userInfo.name
                val dob = userInfo.dob
                val gender = userInfo.gender
                val weight = userInfo.weight
                val height = userInfo.height
                val historyOfDiabetes = healthInfo.historyOfDiabetes
                val familyHistoryOfDiabetes = healthInfo.familyHistoryOfDiabetes
                val sweetConsumption = lifestyleInfo.sweetConsumption
                val sugarIntake = lifestyleInfo.sugarIntake
                val exerciseFrequency = lifestyleInfo.exerciseFrequency
                val foodPreferences = lifestyleInfo.foodPreferences
                val foodAllergies = lifestyleInfo.foodAllergies
                val foodLikes = lifestyleInfo.foodLikes
                val foodDislikes = lifestyleInfo.foodDislikes

                // Panggil metode postAssessment dengan parameter yang diperlukan
                repository.postAssessment(
                    name, dob, gender, weight, height,
                    historyOfDiabetes, familyHistoryOfDiabetes,
                    sweetConsumption, sugarIntake,
                    exerciseFrequency, foodPreferences,
                    foodAllergies, foodLikes, foodDislikes
                )
            } else {
                // Tampilkan pesan jika data tidak lengkap
                (toastText as MutableLiveData).value = Event("Please complete all steps before submitting.")
            }
        }
    }
}