package com.bangkit.cemil.tools.model

import com.google.gson.annotations.SerializedName

data class LikeResponse(

	@field:SerializedName("idAccount")
	val idAccount: String? = null,

	@field:SerializedName("idRestaurant")
	val idRestaurant: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
