package com.way.foodnutrition.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.way.foodnutrition.BuildConfig
import com.way.foodnutrition.presentation.ui.home.RecipesFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {

    fun setQueriesPathApi(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[RecipesFragment.NUMBER] = "50"
        queries[RecipesFragment.APIKEY] = BuildConfig.apiKey
        queries[RecipesFragment.TYPE] = "snack"
        queries[RecipesFragment.DIET] = "vegan"
        queries[RecipesFragment.ADD_RECIPE_INFO] = "true"
        queries[RecipesFragment.FILL_INGREDIENTS] = "true"
        return queries
    }
}