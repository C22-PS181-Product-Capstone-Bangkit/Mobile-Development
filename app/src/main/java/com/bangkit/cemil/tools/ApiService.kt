package com.bangkit.cemil.tools

import com.bangkit.cemil.tools.model.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("api/v1/login")
    fun loginRequest(
        @Field("email")email : String,
        @Field("password")password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("api/v1/register")
    fun registerRequest(
        @Field("name")name: String,
        @Field("email")email: String,
        @Field("password")password: String
    ): Call<RegisterResponse>

    @GET("api/v1/profile")
    fun getProfile(
        @Header("Authorization")value : String,
    ): Call<ProfileResponse>

    @GET("api/v1/restaurant/{restaurantId}")
    fun getRestaurantById(
        @Path("restaurantId")restaurantId : String,
    ): Call<RestaurantItem>

    @GET("api/v1/restaurant")
    fun getRestaurant(): Call<List<RestaurantItem>>

    @FormUrlEncoded
    @PUT("api/v1/edit-profile")
    fun postEditProfile(
        @Header("Authorization")value : String,
        @Field("name")name: String,
        @Field("email")email: String,
        @Field("phone")phone: String
    ): Call<EditResponse>
}