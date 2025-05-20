package com.krissphi.id.dicoding_events_mid_submission_app.data.repository

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.krissphi.id.dicoding_events_mid_submission_app.ui.favorites.FavoritesViewModel

class ViewModelFactory(private val repository: EventRepository) : ViewModelProvider.Factory  {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(application: Application): ViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(EventRepository.Injection.provideRepository(application)).also {
                    instance = it
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            return FavoritesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
