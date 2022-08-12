package com.way.foodnutrition.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.way.foodnutrition.data.FoodRepository
import com.way.foodnutrition.data.local.model.FavoriteEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: FoodRepository,
    app: Application
) : AndroidViewModel(app) {

    val readFavoriteRecipes: LiveData<List<FavoriteEntity>> =
        repository.localDataSource.readFavoriteRecipes().asLiveData()

    private fun insertFavoriteRecipes(favoriteEntity: FavoriteEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.localDataSource.insertFavoriteRecipes(favoriteEntity)
        }

    private fun deleteFavoriteRecipes(favoriteEntity: FavoriteEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.localDataSource.deleteFavoriteRecipes(favoriteEntity)
        }

    private fun deleteAllFavoriteRecipes() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.localDataSource.deleteAllFavoriteRecipes()
        }
}