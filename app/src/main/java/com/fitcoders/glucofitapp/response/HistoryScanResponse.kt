package com.fitcoders.glucofitapp.response

import com.google.gson.annotations.SerializedName

data class HistoryScanResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataItem(

	@field:SerializedName("objectImageUrl")
	val objectImageUrl: String? = null,

	@field:SerializedName("objectSugar")
	val objectSugar: Int? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("objectName")
	val objectName: String? = null,

	@field:SerializedName("datasetId")
	val datasetId: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
