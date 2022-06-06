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
    fun putEditProfile(
        @Header("Authorization")value : String,
        @Field("name")name: String,
        @Field("phone")phone: String,
        @Field("email")email: String
    ): Call<EditResponse>

    @FormUrlEncoded
    @PUT("api/v1/reset-password")
    fun putChangePassword(
        @Header("Authorization")value : String,
        @Field("oldPassword")oldPassword: String,
        @Field("password")password: String,
    ): Call<ChangePasswordResponse>

    @FormUrlEncoded
    @POST("api/v1/upload-profile")
    fun postUploadPhoto(
        @Header("Authorization")value : String,
        @Field("oldPassword")oldPassword: String
    ): Call<ChangePasswordResponse>

    @GET("api/v1/restaurant/{restaurantId}")
    fun getRestaurantByCategory(
        @Path("restaurantId")restaurantId : String,
    ): Call<List<RestaurantItem>>

    @GET("api/v1/restaurant/{restaurantId}")
    fun getRestaurantByName(
        @Path("restaurantId")restaurantId : String,
    ): Call<List<RestaurantItem>>

    @FormUrlEncoded
    @POST("api/v1/favorite")
    fun postLike(
        @Header("Authorization")value : String,
        @Field("idRestaurant")idRestaurant: String,
    ): Call<LikeResponse>

    @FormUrlEncoded
    @POST("api/v1/review")
    fun postReview(
        @Header("Authorization")value : String,
        @Field("idRestaurant")idRestaurant: String,
        @Field("rating")rating: Double,
        @Field("description")description: String,
    ): Call<AddReviewResponse>

}