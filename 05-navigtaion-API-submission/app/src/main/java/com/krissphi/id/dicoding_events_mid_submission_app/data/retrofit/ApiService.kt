package com.krissphi.id.dicoding_events_mid_submission_app.data.retrofit

import com.krissphi.id.dicoding_events_mid_submission_app.data.response.EventsResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("events")
    fun getListEvents(
        @Query("active") active: Int = -1,
        @Query("q") query: String? = null,
        @Query("limit") limit: Int? = null,
    ): Call<EventsResponse>


}