package com.krissphi.id.dicoding_events_mid_submission_app.ui.upcoming

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

class UpcomingViewModel : ViewModel() {

    private val _listEvents = MutableLiveData<List<ListEventsItem>>()
    val listEvent: LiveData<List<ListEventsItem>> = _listEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        setupEvents()
    }

    companion object {
        private const val TAG = "EventsViewModel"
    }


    private fun setupEvents() {
        _isLoading.value = true
        _errorMessage.value = null // Reset error message before fetch
        val client = ApiConfig.getApiService().getListEvents(1)
        client.enqueue(object : Callback<EventsResponse> {
            override fun onResponse(
                call: Call<EventsResponse>,
                response: Response<EventsResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val data = response.body()?.listEvents
                    _listEvents.value = data ?: emptyList()
                    if (data.isNullOrEmpty()) {
                        _errorMessage.value = "Tidak ada event tersedia saat ini."
                    }
                } else {
                    _errorMessage.value = "Gagal memuat data: ${response.message()}"
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventsResponse>, t: Throwable) {
                _isLoading.value = false
                _listEvents.value = emptyList()
                _errorMessage.value = "Terjadi kesalahan: ${t.localizedMessage}"
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun retryFetch() {
        setupEvents()
    }

}