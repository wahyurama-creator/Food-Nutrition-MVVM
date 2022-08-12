package com.way.foodnutrition.di

import android.content.Context
import com.way.foodnutrition.presentation.ui.adapter.IngredientsAdapter
import com.way.foodnutrition.presentation.ui.adapter.RecipesAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AdapterModule {

    @Provides
    @Singleton
    fun provideAdapter(): RecipesAdapter = RecipesAdapter()

    @Provides
    @Singleton
    fun provideIngredientsAdapter(
        @ApplicationContext context: Context
    ): IngredientsAdapter = IngredientsAdapter(context)
}