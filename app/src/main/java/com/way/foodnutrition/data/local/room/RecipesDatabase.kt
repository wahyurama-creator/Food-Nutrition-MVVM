package com.way.foodnutrition.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.way.foodnutrition.data.local.model.FavoriteEntity
import com.way.foodnutrition.data.local.model.FoodJokeEntity
import com.way.foodnutrition.data.local.model.RecipesEntity
import com.way.foodnutrition.data.local.utils.FavoriteTypeConverter
import com.way.foodnutrition.data.local.utils.RecipesTypeConverter

@Database(
    entities = [RecipesEntity::class, FavoriteEntity::class, FoodJokeEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    RecipesTypeConverter::class,
    FavoriteTypeConverter::class
)
abstract class RecipesDatabase : RoomDatabase() {
    abstract fun recipesDao(): RecipesDao
}