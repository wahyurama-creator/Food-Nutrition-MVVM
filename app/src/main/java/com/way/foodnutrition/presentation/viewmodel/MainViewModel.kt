package com.way.foodnutrition.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import android.util.Log
import androidx.lifecycle.*
import com.way.foodnutrition.R
import com.way.foodnutrition.data.FoodRepository
import com.way.foodnutrition.data.local.model.FoodJokeEntity
import com.way.foodnutrition.data.local.model.RecipesEntity
import com.way.foodnutrition.data.remote.model.FoodJoke
import com.way.foodnutrition.data.remote.model.FoodRecipe
import com.way.foodnutrition.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    val app: Application
) : AndroidViewModel(app) {

    // Local
    val readRecipes: LiveData<List<RecipesEntity>> =
        foodRepository.localDataSource.readRecipes().asLiveData()
    val readFoodJoke: LiveData<List<FoodJokeEntity>> =
        foodRepository.localDataSource.readFoodJokes().asLiveData()

    private fun insertRecipes(recipesEntity: RecipesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            foodRepository.localDataSource.insertRecipes(recipesEntity)
        }

    private fun insertFoodJokes(foodJokeEntity: FoodJokeEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            foodRepository.localDataSource.insertFoodJokes(foodJokeEntity)
        }

    private fun offlineCacheRecipes(foodRecipe: FoodRecipe) {
        val recipesEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipesEntity)
    }

    private fun offlineCacheFoodJoke(foodJoke: FoodJoke) {
        val foodJokeEntity = FoodJokeEntity(foodJoke)
        insertFoodJokes(foodJokeEntity)
    }

    // Remote
    var recipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var searchRecipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var foodJokesResponse: MutableLiveData<NetworkResult<FoodJoke>> = MutableLiveData()

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.postValue(NetworkResult.Loading())
        if (hasInternetConnection()) {
            try {
                val response = foodRepository.remoteDataSource.getRecipes(queries)
                recipesResponse.value = handleRecipesResponse(response)

                val foodRecipe = recipesResponse.value?.data
                if (foodRecipe != null) {
                    Log.d(
                        MainViewModel::class.simpleName,
                        "Offline Cache Called, ${foodRecipe.results[0].title}"
                    )
                    offlineCacheRecipes(foodRecipe)
                }
            } catch (e: Exception) {
                recipesResponse.postValue(NetworkResult.Error(e.message.toString()))
            }
        } else {
            recipesResponse.postValue(NetworkResult.Error(app.getString(R.string.no_internet)))
        }
    }

    fun searchRecipes(searchQuery: Map<String, String>) = viewModelScope.launch {
        searchRecipesSafeCall(searchQuery)
    }

    private suspend fun searchRecipesSafeCall(searchQuery: Map<String, String>) {
        searchRecipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = foodRepository.remoteDataSource.searchRecipes(searchQuery)
                searchRecipesResponse.value = handleRecipesResponse(response)
            } catch (e: Exception) {
                searchRecipesResponse.value = NetworkResult.Error(e.message.toString())
            }
        } else {
            searchRecipesResponse.postValue(NetworkResult.Error(app.getString(R.string.no_internet_connection)))
        }
    }

    fun getFoodJokes() = viewModelScope.launch {
        foodJokesSafeCall()
    }

    private suspend fun foodJokesSafeCall() {
        foodJokesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = foodRepository.remoteDataSource.getFoodJokes()
                foodJokesResponse.value = handleFoodJokesResponse(response)

                val foodJoke = foodJokesResponse.value?.data
                if (foodJoke != null) {
                    offlineCacheFoodJoke(foodJoke)
                }
            } catch (e: Exception) {
                foodJokesResponse.value = NetworkResult.Error(e.message.toString())
            }
        } else {
            foodJokesResponse.value =
                NetworkResult.Error(app.getString(R.string.no_internet_connection))
        }
    }

    private fun handleFoodJokesResponse(response: Response<FoodJoke>): NetworkResult<FoodJoke> {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error(app.getString(R.string.timeout))
            }
            response.code() == ERROR_402 -> {
                return NetworkResult.Error(app.getString(R.string.api_key_limited))
            }
            response.isSuccessful -> {
                val foodJokes = response.body()
                return if (foodJokes != null) {
                    NetworkResult.Success(foodJokes)
                } else {
                    NetworkResult.Error(response.message())
                }
            }
            else -> return NetworkResult.Error(response.message())
        }
    }

    private fun handleRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe> {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error(app.getString(R.string.timeout))
            }
            response.code() == ERROR_402 -> {
                return NetworkResult.Error(app.getString(R.string.api_key_limited))
            }
            response.body()!!.results.isEmpty() -> {
                return NetworkResult.Error(app.getString(R.string.recipes_not_found))
            }
            response.isSuccessful -> {
                val recipes = response.body()
                return if (recipes != null) {
                    NetworkResult.Success(recipes)
                } else {
                    NetworkResult.Error(response.message())
                }
            }
            else -> return NetworkResult.Error(response.message())
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(TRANSPORT_WIFI) -> true
            capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    companion object {
        const val ERROR_402 = 402
    }
}