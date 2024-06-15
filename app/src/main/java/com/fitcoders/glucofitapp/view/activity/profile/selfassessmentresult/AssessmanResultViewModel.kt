package com.fitcoders.glucofitapp.view.activity.profile.selfassessmentresult

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitcoders.glucofitapp.data.AppRepository
import com.fitcoders.glucofitapp.response.GetAssesmantResponse
import com.fitcoders.glucofitapp.response.GetUserResponse
import com.fitcoders.glucofitapp.response.QuestionsItem
import kotlinx.coroutines.launch

class AssessmanResultViewModel(private val repository: AppRepository) : ViewModel() {

    val resultResponse: LiveData<GetAssesmantResponse?> = repository.assessmentResponse

    // LiveData untuk menampung hasil ekstraksi pertanyaan
    private val _questions = MutableLiveData<List<QuestionsItem>>()
    val questions: LiveData<List<QuestionsItem>> = _questions

    fun fetchAssessmentData() {
        viewModelScope.launch {
            repository.fetchAssessments()
        }
    }

    // Fungsi untuk mengekstrak pertanyaan dari respons
    fun extractQuestions() {
        resultResponse.value?.questions?.let { questionsList ->
            _questions.value = questionsList.filterNotNull()
        } ?: run {
            _questions.value = emptyList()
        }
    }
}
