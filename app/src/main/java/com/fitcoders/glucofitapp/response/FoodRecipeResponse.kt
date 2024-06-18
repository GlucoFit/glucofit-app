package com.fitcoders.glucofitapp.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class FoodRecipeResponse(

	@field:SerializedName("FoodRecipeResponse")
	val foodRecipeResponse: List<FoodRecipeResponseItem?>? = null
)
@Parcelize
data class FoodRecipeResponseItem(

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

	@field:SerializedName("servings")
	val servings: Int? = null,

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
	val updatedAt: String? = null,

	@field:SerializedName("isFavorite")
	var isFavorite: Boolean? = null,
): Parcelable
