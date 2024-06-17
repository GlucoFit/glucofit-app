package com.fitcoders.glucofitapp.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

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
	val compatibilityScore: Any? = null,

	@field:SerializedName("food_details")
	val foodDetails: FoodDetails? = null
)
@Parcelize
data class FoodDetails(

	@SerializedName("id")
	val id: Int? = null,

	@SerializedName("recipeUri")
	val recipeUri: String? = null,

	@SerializedName("recipeName")
	val recipeName: String? = null,

	@SerializedName("calories")
	val calories: Float? = null,

	@SerializedName("sugarContent")
	val sugarContent: Float? = null,

	@SerializedName("dietLabels")
	val dietLabels: String? = null,

	@SerializedName("ingredients")
	val ingredients: String? = null,

	@SerializedName("imageUrl")
	val imageUrl: String? = null,

	@SerializedName("instructionUrl")
	val instructionUrl: String? = null,

	@SerializedName("createdAt")
	val createdAt: String? = null,

	@SerializedName("updatedAt")
	val updatedAt: String? = null,

	@field:SerializedName("servings")
	val servings: Int? = null,

	@field:SerializedName("isFavorite")
	val isFavorite: Boolean? = null,

): Parcelable
