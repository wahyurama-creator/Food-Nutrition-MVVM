package com.way.foodnutrition.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.way.foodnutrition.data.DataStoreRepository.Companion.PREFERENCES_KEY
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(PREFERENCES_KEY)

@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {
    private object PreferencesKey {
        val selectedMealType = stringPreferencesKey(MEAL_TYPE)
        val selectedMealTypeId = intPreferencesKey(MEAL_TYPE_ID)
        val selectedDietType = stringPreferencesKey(DIET_TYPE)
        val selectedDietTypeId = intPreferencesKey(DIET_TYPE_ID)
        val isBackOnline = booleanPreferencesKey(BACK_ONLINE)
    }

    private val dataStore = context.dataStore

    val readMealAndDietType: Flow<MealAndDietType> = dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }.map { preferences ->
            val selectedMealType =
                preferences[PreferencesKey.selectedMealType] ?: DEFAULT_MEAL_MAIN_COURSE
            val selectedMealTypeId = preferences[PreferencesKey.selectedMealTypeId] ?: DEFAULT_ID
            val selectedDietType =
                preferences[PreferencesKey.selectedDietType] ?: DEFAULT_DIET_GLUTEN_FREE
            val selectedDietTypeId = preferences[PreferencesKey.selectedDietTypeId] ?: DEFAULT_ID
            MealAndDietType(
                selectedMealType, selectedMealTypeId, selectedDietType, selectedDietTypeId
            )
        }

    suspend fun saveMealAndDietType(
        mealType: String,
        mealTypeId: Int,
        dietType: String,
        dietTypeId: Int
    ) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.selectedMealType] = mealType
            preferences[PreferencesKey.selectedMealTypeId] = mealTypeId
            preferences[PreferencesKey.selectedDietType] = dietType
            preferences[PreferencesKey.selectedDietTypeId] = dietTypeId
        }
    }

    suspend fun saveBackOnline(isBackOnline: Boolean) = dataStore.edit {
        it[PreferencesKey.isBackOnline] = isBackOnline
    }

    val readBackOnline: Flow<Boolean> = dataStore.data.catch { exception ->
        if (exception is IOException) emit(emptyPreferences()) else throw exception
    }.map { preferences ->
        val isBackOnline = preferences[PreferencesKey.isBackOnline] ?: false
        isBackOnline
    }

    data class MealAndDietType(
        val selectedMealType: String,
        val selectedMealTypeId: Int,
        val selectedDietType: String,
        val selectedDietTypeId: Int
    )

    companion object {
        const val PREFERENCES_KEY = "food_preferences"
        const val MEAL_TYPE = "mealType"
        const val MEAL_TYPE_ID = "mealTypeId"
        const val DIET_TYPE = "dietType"
        const val DIET_TYPE_ID = "dietTypeId"
        const val BACK_ONLINE = "backOnline"
        private const val DEFAULT_MEAL_MAIN_COURSE = "main course"
        private const val DEFAULT_DIET_GLUTEN_FREE = "gluten free"
        private const val DEFAULT_ID = 0
    }
}