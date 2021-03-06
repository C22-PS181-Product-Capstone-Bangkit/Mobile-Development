package com.bangkit.cemil.tools.model

import com.google.gson.annotations.SerializedName

data class HistoryResponseItem(

	@field:SerializedName("idAccount")
	val idAccount: String? = null,

	@field:SerializedName("createdAt")
	val createdAt: String? = null,

	@field:SerializedName("idRestaurant")
	val idRestaurant: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("updatedAt")
	val updatedAt: String? = null
)
