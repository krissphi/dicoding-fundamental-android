package com.krissphi.id.dicoding_events_mid_submission_app.ui.search

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

class SearchViewModel : ViewModel() {

    private val _searchResults = MutableLiveData<List<ListEventsItem>>()
    val searchResults: LiveData<List<ListEventsItem>> = _searchResults

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _allEvents = MutableLiveData<List<ListEventsItem>>()

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        fetchAllEvents()
    }

    companion object {
        private const val TAG = "SearchViewModel"
    }

    private fun fetchAllEvents() {
        _isLoading.value = true
        _errorMessage.value = null
        val client = ApiConfig.getApiService().getListEvents(-1)
        client.enqueue(object : Callback<EventsResponse> {
            override fun onResponse(call: Call<EventsResponse>, response: Response<EventsResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val events = response.body()?.listEvents ?: emptyList()
                    _allEvents.value = events
                    _errorMessage.value = null
                    resetSearch()
                } else {
                    _searchResults.value = emptyList()
                    _errorMessage.value = "Gagal memuat data: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<EventsResponse>, t: Throwable) {
                _isLoading.value = false
                _searchResults.value = emptyList()
                _errorMessage.value = "Terjadi kesalahan: ${t.localizedMessage}"
            }
        })
    }

    fun searchEvents(query: String) {
        if (query.isBlank()) {
            resetSearch()
            return
        }

        _isLoading.value = true
        _errorMessage.value = null

        ApiConfig.getApiService().getListEvents(-1, query, null)
            .enqueue(object : Callback<EventsResponse> {
                override fun onResponse(call: Call<EventsResponse>, response: Response<EventsResponse>) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _searchResults.value = response.body()?.listEvents ?: emptyList()
                    } else {
                        _searchResults.value = emptyList()
                        _errorMessage.value = "Pencarian gagal: ${response.message()}"
                    }
                }

                override fun onFailure(call: Call<EventsResponse>, t: Throwable) {
                    _isLoading.value = false
                    _searchResults.value = emptyList()
                    _errorMessage.value = "Terjadi kesalahan saat pencarian: ${t.localizedMessage}"
                }
            })
    }


    fun resetSearch() {
        if (_allEvents.value.isNullOrEmpty()) {
            fetchAllEvents()
        } else {
            _searchResults.value = _allEvents.value
            _errorMessage.value = null
        }
    }


    fun retryFetch() {
        fetchAllEvents()
    }
}

