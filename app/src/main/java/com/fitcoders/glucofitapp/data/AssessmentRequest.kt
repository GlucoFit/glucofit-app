package com.fitcoders.glucofitapp.data

data class UserData(
    val name: String,
    val dob: String,
    val gender: String,
    val weight: String,
    val height: String
)

data class HealthInfo(
    val historyOfDiabetes: String,
    val familyHistoryOfDiabetes: String
)

data class LifestyleInfo(
    val sweetConsumption: String,
    val sugarIntake: String,
    val exerciseFrequency: String,
    val foodPreferences: String,
    val foodAllergies: String,
    val foodLikes: String,
    val foodDislikes: String
)
