package com.example.gravefinder

data class User(
    val id: Int,
    val first_name: String,
    val last_name: String,
    val email: String,
    val username: String,
    val password: String,
    val salt: String
    )
