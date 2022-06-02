package com.bangkit.cemil.tools.model

import com.google.gson.annotations.SerializedName

data class RestaurantResponse(

	@field:SerializedName("RestaurantResponse")
	val restaurantResponse: List<RestaurantItem?>? = null
)

data class RestaurantItem(

	@field:SerializedName("photoPlaces")
	val photoPlaces: Any? = null,

	@field:SerializedName("contact")
	val contact: Any? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("rating")
	val rating: Int? = null,

	@field:SerializedName("openHour")
	val openHour: Any? = null,

	@field:SerializedName("location")
	val location: Any? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("category")
	val category: String? = null
)

