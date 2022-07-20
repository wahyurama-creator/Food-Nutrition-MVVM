package com.way.foodnutrition.data

import com.way.foodnutrition.data.model.FoodRecipe
import com.way.foodnutrition.data.network.RecipesApi
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val recipesApi: RecipesApi,
) {

    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe> =
        recipesApi.getRecipes(queries)

}