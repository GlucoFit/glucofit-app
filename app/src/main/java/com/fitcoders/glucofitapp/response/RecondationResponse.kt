package com.fitcoders.glucofitapp.response

import com.google.gson.annotations.SerializedName

data class RecondationResponse(

	@field:SerializedName("RecondationResponse")
	val recondationResponse: List<RecondationResponseItem?>? = null
)

data class RecondationResponseItem(

	@field:SerializedName("diet_labels")
	val dietLabels: String? = null,

	@field:SerializedName("recipe_name")
	val recipeName: String? = null,

	@field:SerializedName("compatibility_score")
	val compatibilityScore: Any? = null,

	@field:SerializedName("food_details")
	val foodDetails: Any? = null
)

data class FoodDetails(

	@field:SerializedName("recipeName")
	val recipeName: String? = null,

	@field:SerializedName("sugarContent")
	val sugarContent: Any? = null,

	@field:SerializedName("instructionUrl")
	val instructionUrl: String? = null,

	@field:SerializedName("recipeUri")
	val recipeUri: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("dietLabels")
	val dietLabels: String? = null,

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("ingredients")
	val ingredients: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("calories")
	val calories: Any? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
