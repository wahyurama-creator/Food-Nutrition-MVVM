package com.way.foodnutrition.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.way.foodnutrition.data.local.model.RecipesEntity
import com.way.foodnutrition.data.local.utils.RecipesTypeConverter

@Database(entities = [RecipesEntity::class], version = 1, exportSchema = false)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase : RoomDatabase() {
    abstract fun recipesDao(): RecipesDao
}