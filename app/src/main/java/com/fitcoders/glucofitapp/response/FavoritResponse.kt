package com.fitcoders.glucofitapp.response

import com.google.gson.annotations.SerializedName

data class FavoritResponse(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("foodId")
	val foodId: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("userId")
	val userId: Int? = null,

	@field:SerializedName("isFavorite")
	val isFavorite: Boolean? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
