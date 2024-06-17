package com.fitcoders.glucofitapp.response

import com.google.gson.annotations.SerializedName

data class SearchHistoryResponse(

	@field:SerializedName("SearchHistoryResponse")
	val searchHistoryResponse: List<SearchHistoryResponseItem?>? = null
)

data class SearchHistoryResponseItem(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("searchText")
	val searchText: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("userId")
	val userId: Int? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
