package com.krissphi.id.dicoding_events_mid_submission_app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.krissphi.id.dicoding_events_mid_submission_app.ui.settings.SettingPreferences
import com.krissphi.id.dicoding_events_mid_submission_app.ui.settings.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MainApp : Application() {

    override fun onCreate() {
        setThemeBeforeOnCreate()

        super.onCreate()
    }

    private fun setThemeBeforeOnCreate() {
        runBlocking {
            val pref = SettingPreferences.getInstance(dataStore)
            val isDarkMode = pref.getThemeSetting().first()

            val mode = if (isDarkMode) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
            AppCompatDelegate.setDefaultNightMode(mode)
        }
    }
}