package com.bangkit.cemil

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun getPreferences() : Preferences{
        return dataStore.data.first()
    }

    suspend fun saveThemePreference(themeMode: Boolean){
        dataStore.edit {
            it[THEME_KEY] = themeMode
        }
    }

    suspend fun deleteThemePreference(){
        dataStore.edit{
            it.remove(THEME_KEY)
        }
    }

    suspend fun saveFirstTimeLanding(firstTimeLanding: Boolean){
        dataStore.edit{
            it[LANDING_KEY] = firstTimeLanding
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: SettingPreferences? = null

        val THEME_KEY = booleanPreferencesKey("theme_mode")
        val LANDING_KEY = booleanPreferencesKey("first_time_landing")

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}