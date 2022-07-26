package com.way.foodnutrition.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import androidx.lifecycle.*
import com.way.foodnutrition.R
import com.way.foodnutrition.data.FoodRepository
import com.way.foodnutrition.data.local.model.RecipesEntity
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

    private fun insertRecipes(recipesEntity: RecipesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            foodRepository.localDataSource.insertRecipes(recipesEntity)
        }

    // Remote
    var recipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.postValue(NetworkResult.Loading())
        if (hasInternetConnection()) {
            try {
                val response = foodRepository.remoteDataSource.getRecipes(queries)
                recipesResponse.postValue(handleRecipesResponse(response))

                val foodRecipe = recipesResponse.value?.data
                if (foodRecipe != null) {
                    offlineCacheRecipes(foodRecipe)
                }
            } catch (e: Exception) {
                recipesResponse.postValue(NetworkResult.Error(e.message.toString()))
            }
        } else {
            recipesResponse.postValue(NetworkResult.Error(app.getString(R.string.no_internet)))
        }
    }

    private fun offlineCacheRecipes(foodRecipe: FoodRecipe) {
        val recipesEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipesEntity)
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