package com.krissphi.id.dicoding_events_mid_submission_app.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel(
    private val themePref: SettingPreferences,
    private val reminderPref: ReminderPreferences
) : ViewModel() {

    fun getThemeSettings(): LiveData<Boolean> {
        return themePref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            themePref.saveThemeSetting(isDarkModeActive)
        }
    }

    fun getReminderSettings(): LiveData<Boolean> {
        return reminderPref.getReminderSetting().asLiveData()
    }

    fun saveReminderSetting(isReminderActive: Boolean) {
        viewModelScope.launch {
            reminderPref.saveReminderSetting(isReminderActive)
        }
    }
}