package com.way.foodnutrition.di

import android.app.Application
import com.way.foodnutrition.data.FoodRepository
import com.way.foodnutrition.presentation.viewmodel.ViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FactoryModule {

    @Provides
    @Singleton
    @ApplicationContext
    fun provideViewModelFactory(
        repository: FoodRepository,
        app: Application
    ): ViewModelFactory = ViewModelFactory(
        repository, app
    )
}