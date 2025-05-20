package com.krissphi.id.dicoding_events_mid_submission_app.ui.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.reminderDataStore: DataStore<Preferences> by preferencesDataStore(name = "reminder_settings")

class ReminderPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    fun getReminderSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[REMINDER_KEY] ?: false
        }
    }

    suspend fun saveReminderSetting(isReminderActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[REMINDER_KEY] = isReminderActive
        }
    }

    companion object {
        private val REMINDER_KEY = booleanPreferencesKey("isReminder")

        @Volatile
        private var INSTANCE: ReminderPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): ReminderPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = ReminderPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}