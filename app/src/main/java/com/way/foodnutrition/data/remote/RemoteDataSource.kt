package com.way.foodnutrition.data.remote

import com.way.foodnutrition.data.remote.model.FoodRecipe
import com.way.foodnutrition.data.remote.network.RecipesApi
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val recipesApi: RecipesApi,
) {

    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe> =
        recipesApi.getRecipes(queries)

    suspend fun searchRecipes(searchQuery: Map<String, String>): Response<FoodRecipe> =
        recipesApi.searchRecipes(searchQuery)

}