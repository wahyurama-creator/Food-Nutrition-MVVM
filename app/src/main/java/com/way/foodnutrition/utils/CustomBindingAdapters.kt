package com.way.foodnutrition.utils

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
import com.way.foodnutrition.data.local.model.RecipesEntity
import com.way.foodnutrition.data.remote.model.FoodRecipe

object CustomBindingAdapters {

    @BindingAdapter("readApiResponseAnimation", "readDatabaseAnimation", requireAll = true)
    @JvmStatic
    fun errorAnimViewVisibility(
        view: LottieAnimationView,
        apiResponse: NetworkResult<FoodRecipe>?,
        database: List<RecipesEntity>?
    ) {
        when {
            apiResponse is NetworkResult.Error && database.isNullOrEmpty() -> {
                view.visibility = View.VISIBLE
            }
            apiResponse is NetworkResult.Loading -> {
                view.visibility = View.INVISIBLE
            }
            apiResponse is NetworkResult.Success -> {
                view.visibility = View.INVISIBLE
            }
        }
    }

    @BindingAdapter("readApiResponseTextView", "readDatabaseTextView", requireAll = true)
    @JvmStatic
    fun errorTextVisibility(
        view: TextView,
        apiResponse: NetworkResult<FoodRecipe>?,
        database: List<RecipesEntity>?
    ) {
        when {
            apiResponse is NetworkResult.Error && database.isNullOrEmpty() -> {
                view.visibility = View.VISIBLE
            }
            apiResponse is NetworkResult.Loading -> {
                view.visibility = View.INVISIBLE
            }
            apiResponse is NetworkResult.Success -> {
                view.visibility = View.INVISIBLE
            }
        }
    }
}