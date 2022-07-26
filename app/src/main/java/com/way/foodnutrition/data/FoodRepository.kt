package com.way.foodnutrition.data

import com.way.foodnutrition.data.local.LocalDataSource
import com.way.foodnutrition.data.remote.RemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class FoodRepository @Inject constructor(
    val remoteDataSource: RemoteDataSource,
    val localDataSource: LocalDataSource
)