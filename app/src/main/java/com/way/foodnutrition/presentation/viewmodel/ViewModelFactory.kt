package com.way.foodnutrition.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.way.foodnutrition.data.DataStoreRepository
import com.way.foodnutrition.data.FoodRepository
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
    private val repository: FoodRepository,
    private val dataStoreRepository: DataStoreRepository,
    val app: Application
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository, app) as T
            }
            modelClass.isAssignableFrom(RecipesViewModel::class.java) -> {
                RecipesViewModel(app, dataStoreRepository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository, app) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel Class")
            }
        }
    }

}