package com.way.foodnutrition.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.way.foodnutrition.data.remote.model.Result

@Entity(tableName = FavoriteEntity.FAVORITE_TABLE)
class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var result: Result
) {
    companion object {
        const val FAVORITE_TABLE = "favorite_recipes_table"
    }
}