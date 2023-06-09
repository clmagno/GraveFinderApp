package com.example.gravefinder

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
        @POST("api/token/") // Replace "login" with the actual login endpoint URL
        fun login(@Body request: LoginRequest): Call<LoginResponse>
        @POST("api/register/") // Replace "register" with the actual register endpoint URL
        fun signUp(@Body request: SignUpRequest): Call<SignUpResponse>
}





