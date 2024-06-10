package com.fitcoders.glucofitapp.response

import com.google.gson.annotations.SerializedName

data class AssessmentStatusResponse(
    @SerializedName("hasAssessment")
    val hasAssessment: Boolean
)


