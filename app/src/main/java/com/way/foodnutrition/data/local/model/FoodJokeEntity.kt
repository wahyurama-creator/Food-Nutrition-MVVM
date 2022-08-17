package com.way.foodnutrition.data.local.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.way.foodnutrition.data.local.model.FoodJokeEntity.Companion.FOOD_JOKE_TABLE
import com.way.foodnutrition.data.remote.model.FoodJoke

@Entity(tableName = FOOD_JOKE_TABLE)
class FoodJokeEntity(
    @Embedded
    var foodJoke: FoodJoke
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    companion object {
        const val FOOD_JOKE_TABLE = "food_joke_table"
    }
}
