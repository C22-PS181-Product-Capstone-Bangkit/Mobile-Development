package com.bangkit.cemil

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
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

    suspend fun saveAuthorization(isAuthorized: Boolean){
        dataStore.edit{
            it[AUTHORIZED_KEY] = isAuthorized
        }
    }

    suspend fun saveAuthorizationToken(token: String){
        dataStore.edit{
            it[AUTHORIZATION_TOKEN_KEY] = token
        }
    }

    suspend fun deleteAuthorizationToken(){
        dataStore.edit {
            it.remove(AUTHORIZATION_TOKEN_KEY)
        }
    }

    suspend fun saveLocation(location: String){
        dataStore.edit {
            it[LOCATION_KEY] = location
        }
    }

    suspend fun saveLatitudeLongitude(latitude: String, longitude: String){
        dataStore.edit{
            it[LATITUDE_KEY] = latitude
            it[LONGITUDE_KEY] = longitude
        }
    }

    suspend fun deleteLocation(){
        dataStore.edit {
            it.remove(LOCATION_KEY)
            it.remove(LATITUDE_KEY)
            it.remove(LONGITUDE_KEY)
        }
    }

    suspend fun saveRecentSearch(searchQuerySet: Set<String>){
        dataStore.edit {
            it[RECENT_SEARCH_KEY] = searchQuerySet
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: SettingPreferences? = null

        val THEME_KEY = booleanPreferencesKey("theme_mode")
        val LANDING_KEY = booleanPreferencesKey("first_time_landing")
        val AUTHORIZED_KEY = booleanPreferencesKey("is_authorized")
        val LOCATION_KEY = stringPreferencesKey("location")
        val AUTHORIZATION_TOKEN_KEY = stringPreferencesKey("authorization_token")
        val LATITUDE_KEY = stringPreferencesKey("latitude")
        val LONGITUDE_KEY = stringPreferencesKey("longitude")
        val RECENT_SEARCH_KEY = stringSetPreferencesKey("recent_searches")

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}