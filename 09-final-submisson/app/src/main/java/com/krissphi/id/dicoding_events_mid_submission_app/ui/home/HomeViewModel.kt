package com.krissphi.id.dicoding_events_mid_submission_app.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.krissphi.id.dicoding_events_mid_submission_app.data.response.EventsResponse
import com.krissphi.id.dicoding_events_mid_submission_app.data.response.ListEventsItem
import com.krissphi.id.dicoding_events_mid_submission_app.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val _upcomingEvents = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvents: LiveData<List<ListEventsItem>> = _upcomingEvents

    private val _finishedEvents = MutableLiveData<List<ListEventsItem>>()
    val finishedEvents: LiveData<List<ListEventsItem>> = _finishedEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        fetchUpcomingEvents()
        fetchFinishedEvents()
    }

    companion object {
        private const val TAG = "EventsViewModel"
    }

    private fun fetchUpcomingEvents() {
        _isLoading.value = true
        _errorMessage.value = null
        val client = ApiConfig.getApiService().getListEvents(1, null, 5)
        client.enqueue(object : Callback<EventsResponse> {
            override fun onResponse(
                call: Call<EventsResponse>,
                response: Response<EventsResponse>
            ) {
                if (response.isSuccessful) {
                    val events = response.body()?.listEvents ?: emptyList()
                    _upcomingEvents.value = events
                    if (events.isEmpty()) {
                        _errorMessage.value = "Belum ada event mendatang yang tersedia."
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _upcomingEvents.value = emptyList()
                    _errorMessage.value = "Gagal memuat event mendatang: ${response.message()}"
                }
                checkLoadingState()
            }

            override fun onFailure(call: Call<EventsResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                _upcomingEvents.value = emptyList()
                _errorMessage.value = "Terjadi kesalahan saat memuat event mendatang: ${t.localizedMessage}"
                checkLoadingState()
            }
        })
    }

    private fun fetchFinishedEvents() {
        _isLoading.value = true
        _errorMessage.value = null
        val client = ApiConfig.getApiService().getListEvents(0, null, 5)
        client.enqueue(object : Callback<EventsResponse> {
            override fun onResponse(
                call: Call<EventsResponse>,
                response: Response<EventsResponse>
            ) {
                if (response.isSuccessful) {
                    val events = response.body()?.listEvents ?: emptyList()
                    _finishedEvents.value = events
                    if (events.isEmpty()) {
                        _errorMessage.value = "Tidak ada event yang telah selesai."
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _finishedEvents.value = emptyList()
                    _errorMessage.value = "Gagal memuat event yang telah selesai: ${response.message()}"
                }
                checkLoadingState()
            }

            override fun onFailure(call: Call<EventsResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                _finishedEvents.value = emptyList()
                _errorMessage.value = "Terjadi kesalahan saat memuat event yang telah selesai: ${t.localizedMessage}"
                checkLoadingState()
            }
        })
    }

    private fun checkLoadingState() {
        if (_upcomingEvents.value != null && _finishedEvents.value != null) {
            _isLoading.value = false
        }
    }


    fun retryFetch() {
        fetchUpcomingEvents()
        fetchFinishedEvents()
    }
}
