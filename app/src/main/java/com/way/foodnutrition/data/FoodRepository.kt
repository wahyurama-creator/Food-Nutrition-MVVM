package com.way.foodnutrition.data

import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class FoodRepository @Inject constructor(
    val remoteDataSource: RemoteDataSource
) {  }