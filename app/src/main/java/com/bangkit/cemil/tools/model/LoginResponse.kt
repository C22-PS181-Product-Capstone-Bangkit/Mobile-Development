package com.bangkit.cemil.tools.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("access_token")
	val accessToken: String?,

	@field:SerializedName("message")
	val message: String?

)

data class RegisterResponse(

	@field:SerializedName("access_token")
	val accessToken: String? = null,

	@field:SerializedName("profilePic")
	val profilePic: Any? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("idFriend")
	val idFriend: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("message")
	val message: String?
)

data class AuthenticateResponse(

	@field:SerializedName("profilePic")
	val profilePic: Any? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("idFriend")
	val idFriend: String? = null,

	@field:SerializedName("exp")
	val exp: Int? = null,

	@field:SerializedName("iat")
	val iat: Int? = null,

	@field:SerializedName("email")
	val email: String? = null
)


