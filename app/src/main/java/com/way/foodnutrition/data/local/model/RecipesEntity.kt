package com.way.foodnutrition.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.way.foodnutrition.data.local.model.RecipesEntity.Companion.TABLE_RECIPES
import com.way.foodnutrition.data.remote.model.FoodRecipe

@Entity(tableName = TABLE_RECIPES)
class RecipesEntity(var foodRecipe: FoodRecipe) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0

    companion object {
        const val TABLE_RECIPES = "recipes_table"
        const val DATABASE_RECIPES = "recipes_db"
    }
}