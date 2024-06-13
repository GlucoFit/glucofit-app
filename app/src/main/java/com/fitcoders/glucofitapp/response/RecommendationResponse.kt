package com.fitcoders.glucofitapp.response

import com.google.gson.annotations.SerializedName

data class RecommendationResponse(

	@field:SerializedName("RecommendationResponse")
	val recommendationResponse: List<RecommendationResponseItem?>? = null
)

data class RecommendationResponseItem(

	@field:SerializedName("diet_labels")
	val dietLabels: String? = null,

	@field:SerializedName("recipe_name")
	val recipeName: String? = null,

	@field:SerializedName("compatibility_score")
	val compatibilityScore: Float? = null,

	@field:SerializedName("food_details")
	val foodDetails: List<FoodDetails>? = null
)

data class FoodDetails(

	@field:SerializedName("recipeName")
	val recipeName: String? = null,

	@field:SerializedName("sugarContent")
	val sugarContent: Float? = null,

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
	val calories: Float? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
