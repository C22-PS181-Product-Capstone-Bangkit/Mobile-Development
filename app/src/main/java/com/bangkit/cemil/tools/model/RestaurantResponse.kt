package com.bangkit.cemil.tools.model

import com.google.gson.annotations.SerializedName

data class RestaurantItem(

	@field:SerializedName("photoPlaces")
	val photoPlaces: String? = null,

	@field:SerializedName("profilePic")
	val profilePic: String? = null,

	@field:SerializedName("contact")
	val contact: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("rating")
	val rating: String,

	@field:SerializedName("openHour")
	val openHour: String? = null,

	@field:SerializedName("price")
	val price: String,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("person")
	val person: Int,

	@field:SerializedName("countReview")
	val countReview: Int = 0,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("review")
	val review: List<RestaurantReviewItem>? = null,

	var distance: String

)

data class RestaurantReviewItem(

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("profilePic")
	val profilePic: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("rating")
	val rating: Int? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)


