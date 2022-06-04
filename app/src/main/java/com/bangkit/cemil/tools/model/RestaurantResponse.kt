package com.bangkit.cemil.tools.model

import com.google.gson.annotations.SerializedName

data class RestaurantResponse(

	@field:SerializedName("RestaurantResponse")
	val restaurantResponse: List<RestaurantItem?>? = null
)

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
	val rating: String? = null,

	@field:SerializedName("openHour")
	val openHour: String? = null,

	@field:SerializedName("price")
	val price: String? = null,

	@field:SerializedName("location")
	val location: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("countReview")
	val countReview: Int = 0,

	@field:SerializedName("review")
	val review: List<ReviewItem>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	var distance: String? = null
)

