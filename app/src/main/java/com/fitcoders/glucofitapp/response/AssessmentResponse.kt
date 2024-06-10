package com.fitcoders.glucofitapp.response

import com.google.gson.annotations.SerializedName

data class AssessmentResponse(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("result")
    val result: Int? = null,

    @field:SerializedName("userId")
    val userId: Int? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null,

    @field:SerializedName("questions")
    val questions: List<Question>? = null
)

data class Question(

    @field:SerializedName("questionId")
    val questionId: String? = null,

    @field:SerializedName("questionAnswer")
    val questionAnswer: String? = null
)
