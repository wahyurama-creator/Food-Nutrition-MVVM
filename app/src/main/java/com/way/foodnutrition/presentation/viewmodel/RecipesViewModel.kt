package com.way.foodnutrition.presentation.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.way.foodnutrition.BuildConfig
import com.way.foodnutrition.data.DataStoreRepository
import com.way.foodnutrition.data.DataStoreRepository.MealAndDietType
import com.way.foodnutrition.presentation.ui.home.RecipesFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    app: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(app) {

    private lateinit var mealAndDiet: MealAndDietType

    var networkStatus = false
    var isBackOnline = false

    val readMealAndDietType = dataStoreRepository.readMealAndDietType
    val readIsBackOnline = dataStoreRepository.readBackOnline

    fun saveMealAndDietType() =
        viewModelScope.launch(Dispatchers.IO) {
            if (::mealAndDiet.isInitialized) {
                dataStoreRepository.saveMealAndDietType(
                    mealAndDiet.selectedMealType,
                    mealAndDiet.selectedMealTypeId,
                    mealAndDiet.selectedDietType,
                    mealAndDiet.selectedDietTypeId
                )
            }
        }

    fun saveMealAndDietTypeTemp(
        mealType: String,
        mealTypeId: Int,
        dietType: String,
        dietTypeId: Int
    ) {
        mealAndDiet = MealAndDietType(
            mealType, mealTypeId, dietType, dietTypeId
        )
    }

    private fun saveIsBackOnline(backOnline: Boolean) =
        viewModelScope.launch {
            dataStoreRepository.saveBackOnline(backOnline)
        }

    fun setQueriesPathForRecipesApi(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[RecipesFragment.NUMBER] = DEFAULT_NUMBER
        queries[RecipesFragment.APIKEY] = BuildConfig.apiKey
        if (::mealAndDiet.isInitialized) {
            queries[RecipesFragment.TYPE] = mealAndDiet.selectedMealType
            queries[RecipesFragment.DIET] = mealAndDiet.selectedDietType
        } else {
            queries[RecipesFragment.TYPE] = DEFAULT_TYPE
            queries[RecipesFragment.DIET] = DEFAULT_DIET
        }
        queries[RecipesFragment.ADD_RECIPE_INFO] = "true"
        queries[RecipesFragment.FILL_INGREDIENTS] = "true"
        return queries
    }

    fun setQueriesPathForSearchApi(searchQuery: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[RecipesFragment.SEARCH_QUERY] = searchQuery
        queries[RecipesFragment.NUMBER] = DEFAULT_NUMBER
        queries[RecipesFragment.APIKEY] = BuildConfig.apiKey
        queries[RecipesFragment.ADD_RECIPE_INFO] = "true"
        queries[RecipesFragment.FILL_INGREDIENTS] = "true"
        return queries
    }

    fun showNetworkStatus() {
        if (!networkStatus) {
            Toast.makeText(getApplication(), "No Internet Connection.", Toast.LENGTH_SHORT).show()
            saveIsBackOnline(true)
        } else if (networkStatus) {
            if (isBackOnline) {
                Toast.makeText(getApplication(), "We're back online.", Toast.LENGTH_SHORT).show()
                saveIsBackOnline(false)
            }
        }
    }

    companion object {
        const val DEFAULT_NUMBER = "50"
        const val DEFAULT_TYPE = "main course"
        const val DEFAULT_DIET = "gluten free"
    }
}