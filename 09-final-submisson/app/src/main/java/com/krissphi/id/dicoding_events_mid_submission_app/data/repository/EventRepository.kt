package com.krissphi.id.dicoding_events_mid_submission_app.data.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import com.krissphi.id.dicoding_events_mid_submission_app.data.local.FavoriteEventDao
import com.krissphi.id.dicoding_events_mid_submission_app.data.local.FavoriteEventEntity
import com.krissphi.id.dicoding_events_mid_submission_app.data.local.AppDatabase

class EventRepository(private val favoriteEventDao: FavoriteEventDao) {

    fun getAllFavoriteEvents(): LiveData<List<FavoriteEventEntity>> {
        return favoriteEventDao.getAllFavorites()
    }

    suspend fun insertFavorite(event: FavoriteEventEntity) {
        try {
            favoriteEventDao.insertFavorite(event)
        } catch (e: Exception) {
            Log.e("EventRepository", "Error inserting favorite event: ${e.message}")
            throw e
        }
    }

    suspend fun deleteFavorite(event: FavoriteEventEntity) {
        try {
            favoriteEventDao.deleteFavorite(event)
        } catch (e: Exception) {
            Log.e("EventRepository", "Error deleting favorite event: ${e.message}")
            throw e
        }
    }

    suspend fun isFavorite(eventId: Int): Boolean {
        return favoriteEventDao.isFavorite(eventId)
    }

    object Injection {
        fun provideRepository(application: Application): EventRepository {
            val database = AppDatabase.getDatabase(application)
            val eventDao = database.favoriteEventDao()
            return EventRepository(eventDao)
        }
    }
}