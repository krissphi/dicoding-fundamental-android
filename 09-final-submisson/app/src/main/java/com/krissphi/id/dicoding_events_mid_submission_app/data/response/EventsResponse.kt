package com.krissphi.id.dicoding_events_mid_submission_app.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class EventsResponse(
	@field:SerializedName("listEvents")
	val listEvents: List<ListEventsItem> = listOf(),
	val error: Boolean? = null,
	val message: String? = null
)

@Parcelize
data class ListEventsItem(
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
	val id: Int? = null,
	val beginTime: String? = null,
	val endTime: String? = null,
	val category: String? = null
) : Parcelable



