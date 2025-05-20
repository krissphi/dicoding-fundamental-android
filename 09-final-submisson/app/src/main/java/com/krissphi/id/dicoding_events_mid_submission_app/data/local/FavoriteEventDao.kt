package com.krissphi.id.dicoding_events_mid_submission_app.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy

@Dao
interface FavoriteEventDao {
    @Query("SELECT * FROM favorite_event")
    fun getAllFavorites(): LiveData<List<FavoriteEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(event: FavoriteEventEntity)

    @Delete
    suspend fun deleteFavorite(event: FavoriteEventEntity)

    @Query("SELECT EXISTS(SELECT * FROM favorite_event WHERE id = :id)")
    suspend fun isFavorite(id: Int): Boolean

}