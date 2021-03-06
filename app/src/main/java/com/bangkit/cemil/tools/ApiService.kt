package com.bangkit.cemil.tools

import com.bangkit.cemil.tools.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("api/v1/login")
    fun loginRequest(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("api/v1/register")
    fun registerRequest(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @GET("api/v1/profile")
    fun getProfile(
        @Header("Authorization") value: String,
    ): Call<ProfileResponse>

    @GET("api/v1/restaurant/{restaurantId}")
    fun getRestaurantById(
        @Path("restaurantId") restaurantId: String,
    ): Call<RestaurantItem>

    @GET("api/v1/restaurant")
    fun getRestaurant(): Call<List<RestaurantItem>>

    @Multipart
    @PUT("api/v1/full-edit")
    fun putEditProfile(
        @Header("Authorization") value: String,
        @Part file: MultipartBody.Part,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody
    ): Call<EditResponse>

    @FormUrlEncoded
    @PUT("api/v1/reset-password")
    fun putChangePassword(
        @Header("Authorization") value: String,
        @Field("oldPassword") oldPassword: String,
        @Field("password") password: String,
    ): Call<ChangePasswordResponse>

    @GET("api/v1/restaurant")
    fun getRestaurantByCategory(
        @Query("category") category: String,
    ): Call<List<RestaurantItem>>

    @GET("api/v1/restaurant")
    fun getRestaurantByName(
        @Query("q") query: String,
    ): Call<List<RestaurantItem>>

    @FormUrlEncoded
    @POST("api/v1/favorite")
    fun postLike(
        @Header("Authorization") value: String,
        @Field("idRestaurant") idRestaurant: String,
    ): Call<LikeResponse>

    @FormUrlEncoded
    @POST("api/v1/review")
    fun postReview(
        @Header("Authorization") value: String,
        @Field("idRestaurant") idRestaurant: String,
        @Field("rating") rating: Double,
        @Field("description") description: String,
    ): Call<AddReviewResponse>

    @DELETE("api/v1/favorite/{likeId}")
    fun deleteLike(
        @Header("Authorization") value: String,
        @Path("likeId") likeId: String,
    ): Call<LikeDeleteResponse>

    @FormUrlEncoded
    @POST("api/v1/restaurant/list-restaurant")
    fun fetchRestaurantByListIds(
        @Field("idRestaurants") idRestaurants: ArrayList<String>,
    ): Call<List<RestaurantItem>>

    @GET("api/v1/restaurant/recommendation")
    fun fetchRecommendations(
        @Header("Authorization") value: String
    ): Call<List<RestaurantItem>>

    @FormUrlEncoded
    @POST("api/v1/history")
    fun postHistory(
        @Header("Authorization") value: String,
        @Field("idRestaurant") idRestaurant: String,
    ): Call<HistoryResponseItem>
}