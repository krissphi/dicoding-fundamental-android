package com.krissphi.id.architecture_component_app.live_data_api.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.krissphi.id.architecture_component_app.live_data_api.RetrofitActivity
import com.krissphi.id.architecture_component_app.live_data_api.RetrofitActivity.Companion
import com.krissphi.id.architecture_component_app.live_data_api.data.response.CustomerReviewsItem
import com.krissphi.id.architecture_component_app.live_data_api.data.response.PostReviewResponse
import com.krissphi.id.architecture_component_app.live_data_api.data.response.Restaurant
import com.krissphi.id.architecture_component_app.live_data_api.data.response.RestaurantResponse
import com.krissphi.id.architecture_component_app.live_data_api.data.retrofit.ApiConfig
import com.krissphi.id.architecture_component_app.live_data_api.util.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LDViewModel : ViewModel() {
    private val _restaurant = MutableLiveData<Restaurant>()
    val restaurant: LiveData<Restaurant> = _restaurant
    private val _listReview = MutableLiveData<List<CustomerReviewsItem>>()
    val listReview: LiveData<List<CustomerReviewsItem>> = _listReview
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


//    live_data single event
//    private val _snackbarText = MutableLiveData<String>()
//    val snackbarText: LiveData<String> = _snackbarText

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    companion object{
        private const val TAG = "MainViewModel"
        private const val RESTAURANT_ID = "uewq1zg2zlskfw1e867"
    }

    init {
        findRestaurant()
    }

    private fun findRestaurant() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getRestaurant(RESTAURANT_ID)
        client.enqueue(object : Callback<RestaurantResponse> {
            override fun onResponse(
                call: Call<RestaurantResponse>,
                response: Response<RestaurantResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _restaurant.value = response.body()?.restaurant
                    _listReview.value = response.body()?.restaurant?.customerReviews
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<RestaurantResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun postReview(review: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().postReview(RESTAURANT_ID, "Dicoding", review)
        client.enqueue(object : Callback<PostReviewResponse> {
            override fun onResponse(call: Call<PostReviewResponse>, response: Response<PostReviewResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listReview.value = response.body()?.customerReviews

//                    live_data single event
//                    _snackbarText.value = response.body()?.message
                    _snackbarText.value = Event(response.body()?.message.toString())


                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<PostReviewResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}