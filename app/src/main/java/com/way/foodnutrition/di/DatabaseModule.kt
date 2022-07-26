package com.way.foodnutrition.di

import android.content.Context
import androidx.room.Room
import com.way.foodnutrition.data.local.model.RecipesEntity.Companion.DATABASE_RECIPES
import com.way.foodnutrition.data.local.room.RecipesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        RecipesDatabase::class.java,
        DATABASE_RECIPES
    )
        .build()

    @Provides
    @Singleton
    fun provideDao(database: RecipesDatabase) = database.recipesDao()

}