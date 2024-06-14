package com.fitcoders.glucofitapp.response

import com.google.gson.annotations.SerializedName

data class GetAssesmantResponse(

	@field:SerializedName("result")
	val result: Int? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("questions")
	val questions: List<QuestionsItem?>? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("userId")
	val userId: Int? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)

data class QuestionsItem(

	@field:SerializedName("questionId")
	val questionId: String? = null,

	@field:SerializedName("questionAnswer")
	val questionAnswer: String? = null
)
