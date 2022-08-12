package com.way.foodnutrition.data.local

import com.way.foodnutrition.data.local.model.FavoriteEntity
import com.way.foodnutrition.data.local.model.RecipesEntity
import com.way.foodnutrition.data.local.room.RecipesDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao
) {
    suspend fun insertRecipes(recipesEntity: RecipesEntity) =
        recipesDao.insertRecipes(recipesEntity)

    fun readRecipes(): Flow<List<RecipesEntity>> = recipesDao.readRecipes()

    suspend fun insertFavoriteRecipes(favoriteEntity: FavoriteEntity) =
        recipesDao.insertFavoriteRecipes(favoriteEntity)

    fun readFavoriteRecipes(): Flow<List<FavoriteEntity>> = recipesDao.readFavoriteRecipes()

    suspend fun deleteFavoriteRecipes(favoriteEntity: FavoriteEntity) =
        recipesDao.deleteFavoriteRecipes(favoriteEntity)

    suspend fun deleteAllFavoriteRecipes() = recipesDao.deleteAllFavoriteRecipes()
}