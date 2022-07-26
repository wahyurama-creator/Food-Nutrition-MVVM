package com.way.foodnutrition.data.local.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.way.foodnutrition.data.model.FoodRecipe
import javax.inject.Inject

class RecipesTypeConverter {

    @Inject
    lateinit var gson: Gson

    @TypeConverter
    fun foodRecipeToString(foodRecipe: FoodRecipe): String {
        return gson.toJson(foodRecipe)
    }

    @TypeConverter
    fun stringToFoodRecipe(data: String): FoodRecipe {
        val listOfType = object : TypeToken<FoodRecipe>() {}.type
        return gson.fromJson(data, listOfType)
    }
}