package com.bangkit.cemil.tools

import com.bangkit.cemil.tools.model.LoginResponse
import com.bangkit.cemil.tools.model.ProfileResponse
import com.bangkit.cemil.tools.model.RegisterResponse
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

}