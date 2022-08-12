package com.way.foodnutrition.data.local.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.way.foodnutrition.data.remote.model.Result

class FavoriteTypeConverter {

    private val gson: Gson = Gson()

    @TypeConverter
    fun resultToString(result: Result): String {
        return gson.toJson(result)
    }

    @TypeConverter
    fun stringToResult(data: String): Result {
        val listOfType = object : TypeToken<Result>() {}.type
        return gson.fromJson(data, listOfType)
    }
}