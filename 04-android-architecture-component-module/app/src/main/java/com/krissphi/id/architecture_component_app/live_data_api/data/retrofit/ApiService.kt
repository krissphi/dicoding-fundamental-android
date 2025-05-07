package com.krissphi.id.architecture_component_app.live_data_api.data.retrofit

import com.krissphi.id.architecture_component_app.live_data_api.data.response.PostReviewResponse
import com.krissphi.id.architecture_component_app.live_data_api.data.response.RestaurantResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("detail/{id}")
    fun getRestaurant(
        @Path("id") id: String
    ): Call<RestaurantResponse>

    @FormUrlEncoded
    @Headers("Authorization: token 12345")
    @POST("review")
    fun postReview(
        @Field("id") id: String,
        @Field("name") name: String,
        @Field("review") review: String
    ): Call<PostReviewResponse>
}