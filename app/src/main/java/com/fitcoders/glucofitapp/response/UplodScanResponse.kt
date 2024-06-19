package com.fitcoders.glucofitapp.response

import com.google.gson.annotations.SerializedName

data class UplodScanResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Data(

	@field:SerializedName("objectImageUrl")
	val objectImageUrl: String? = null,

	@field:SerializedName("objectSugar")
	val objectSugar: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("objectName")
	val objectName: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("userId")
	val userId: Int? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
