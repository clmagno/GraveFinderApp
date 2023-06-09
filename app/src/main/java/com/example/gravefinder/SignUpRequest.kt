package com.example.gravefinder

import com.google.gson.annotations.SerializedName

data class SignUpRequest(
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    val email: String,
    @SerializedName("username")
    val userName: String,
    val password: String,
    @SerializedName("confirm_password")
    val confirmPassword: String? = null
)

