package com.bangkit.cemil.tools.model

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("review")
	val review: List<ReviewItem?>? = null,

	@field:SerializedName("history")
	val history: List<HistoryItem?>? = null,

	@field:SerializedName("user")
	val user: User?,

	@field:SerializedName("likes")
	val likes: List<LikesItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("data")
	val data: String? = null
)

data class LikesItem(

	@field:SerializedName("idAccount")
	val idAccount: String? = null,

	@field:SerializedName("idRestaurant")
	val idRestaurant: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class ReviewItem(

	@field:SerializedName("idAccount")
	val idAccount: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("idRestaurant")
	val idRestaurant: String? = null,

	@field:SerializedName("subject")
	val subject: String? = null,

	@field:SerializedName("rating")
	val rating: Int? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)

data class User(

	@field:SerializedName("profilePic")
	val profilePic: String?,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String?,

	@field:SerializedName("idFriend")
	val idFriend: String?,

	@field:SerializedName("exp")
	val exp: Int?,

	@field:SerializedName("iat")
	val iat: Int,

	@field:SerializedName("email")
	val email: String
)

data class HistoryItem(

	@field:SerializedName("idAccount")
	val idAccount: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("idRestaurant")
	val idRestaurant: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
