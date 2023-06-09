package com.example.gravefinder

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("access") val accessToken: String,
    @SerializedName("refresh") val refreshToken: String,
    @SerializedName("id") val id: Int,
    @SerializedName("email") val email: String,
    @SerializedName("first_name") val first_name: String,
    @SerializedName("last_name") val last_name: String,
    @SerializedName("username") val username: String
)
