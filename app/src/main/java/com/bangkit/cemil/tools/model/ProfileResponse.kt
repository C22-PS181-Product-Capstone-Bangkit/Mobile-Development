package com.bangkit.cemil.tools.model

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("review")
	val review: List<ReviewItem>? = null,

	@field:SerializedName("history")
	val history: List<HistoryItem>? = null,

	@field:SerializedName("user")
	val user: User?,

	@field:SerializedName("likes")
	val likes: List<LikesItem>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("data")
	val data: String? = null
)

data class User(

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("profilePic")
	val profilePic: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("idFriend")
	val idFriend: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)

data class ReviewItem(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("restaurant")
	val restaurant: Restaurant? = null,

	@field:SerializedName("rating")
	val rating: Int? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)

data class Restaurant(

	@field:SerializedName("countReview")
	val countReview: Int? = null,

	@field:SerializedName("profilePic")
	val profilePic: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("rating")
	val rating: Double? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("category")
	val category: String? = null
)

data class HistoryItem(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("restaurant")
	val restaurant: Restaurant? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)

data class LikesItem(

	@field:SerializedName("restaurant")
	val restaurant: Restaurant? = null,

	@field:SerializedName("id")
	val id: String? = null
)

//data class LikedRestaurant(
//
//	@field:SerializedName("countReview")
//	val countReview: Int? = null,
//
//	@field:SerializedName("profilePic")
//	val profilePic: String? = null,
//
//	@field:SerializedName("name")
//	val name: String? = null,
//
//	@field:SerializedName("category")
//	val category: String? = null,
//
//	@field:SerializedName("rating")
//	val rating: Double? = null,
//
//	@field:SerializedName("id")
//	val id: String? = null
//)

data class EditResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String?

)

data class UploadPictureResponse(

	@field:SerializedName("user")
	val user: User? = null
)



