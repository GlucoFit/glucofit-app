package com.fitcoders.glucofitapp.response

import com.google.gson.annotations.SerializedName

data class AnalyzeResponse(

	@field:SerializedName("data")
	val data: Data? = null
)

data class Data(

	@field:SerializedName("food_name")
	val foodName: String? = null,

	@field:SerializedName("sugar_content")
	val sugarContent: String? = null
)
