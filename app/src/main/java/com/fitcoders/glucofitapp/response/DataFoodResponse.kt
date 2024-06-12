package com.fitcoders.glucofitapp.response

import com.google.gson.annotations.SerializedName

data class DataFoodResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("sugar")
	val sugar: Sugar? = null
)

data class Sugar(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("sugar")
	val sugar: Int? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
