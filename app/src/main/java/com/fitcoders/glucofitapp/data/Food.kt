package com.fitcoders.glucofitapp.data

data class Food(
    val id: Int,
    val recipeUri: String,
    val recipeName: String,
    val calories: Float,
    val sugarContent: Float,
    val dietLabels: String,
    val ingredients: String,
    val imageUrl: String,
    val instructionUrl: String,
    val createdAt: String,
    val updatedAt: String,
)
