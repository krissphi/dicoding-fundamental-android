package com.krissphi.id.dicoding_events_mid_submission_app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_event")
data class FavoriteEventEntity(
    @PrimaryKey val id: Int,
    val summary: String? = null,
    val mediaCover: String? = null,
    val registrants: Int? = null,
    val imageLogo: String? = null,
    val link: String? = null,
    val description: String? = null,
    val ownerName: String? = null,
    val cityName: String? = null,
    val quota: Int? = null,
    val name: String? = null,
    val beginTime: String? = null,
    val endTime: String? = null,
    val category: String? = null
)


