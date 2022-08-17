package com.way.foodnutrition.data.remote.network

import com.way.foodnutrition.BuildConfig
import com.way.foodnutrition.data.remote.model.FoodJoke
import com.way.foodnutrition.data.remote.model.FoodRecipe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface RecipesApi {

    @GET("/recipes/complexSearch")
    suspend fun getRecipes(
        @QueryMap queries: Map<String, String>,
    ): Response<FoodRecipe>

    @GET("/recipes/complexSearch")
    suspend fun searchRecipes(
        @QueryMap searchQuery: Map<String, String>,
    ): Response<FoodRecipe>

    @GET("/food/jokes/random")
    suspend fun getFoodJoke(
        @Query("apiKey") apiKey: String = BuildConfig.apiKey
    ): Response<FoodJoke>
}