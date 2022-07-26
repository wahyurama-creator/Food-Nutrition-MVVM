package com.way.foodnutrition.data.local.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.way.foodnutrition.data.remote.model.FoodRecipe

class RecipesTypeConverter {

    private lateinit var gson: Gson

    @TypeConverter
    fun foodRecipeToString(foodRecipe: FoodRecipe): String {
        gson = Gson()
        return gson.toJson(foodRecipe)
    }

    @TypeConverter
    fun stringToFoodRecipe(data: String): FoodRecipe {
        gson = Gson()
        val listOfType = object : TypeToken<FoodRecipe>() {}.type
        return gson.fromJson(data, listOfType)
    }
}