package com.fitcoders.glucofitapp.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class FavoritFoodResponse(

	@field:SerializedName("FavoritFoodResponse")
	val favoritFoodResponse: List<FavoritFoodResponseItem?>? = null
)

data class FavoritFoodResponseItem(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("foodId")
	val foodId: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("userId")
	val userId: Int? = null,

	@field:SerializedName("food")
	val food: Food? = null,

	@field:SerializedName("isFavorite")
	var isFavorite: Boolean? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
@Parcelize
data class Food(

	@field:SerializedName("recipeName")
	val recipeName: String? = null,

	@field:SerializedName("sugarContent")
	val sugarContent: Float? = null,

	@field:SerializedName("instructionUrl")
	val instructionUrl: String? = null,

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
	val calories: Float? = null
): Parcelable
