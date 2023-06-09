package com.example.gravefinder

import com.google.gson.*
import java.lang.reflect.Type

class UserDeserializer : JsonDeserializer<User> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): User {
        val jsonObject = json?.asJsonObject
        val id = jsonObject?.get("id")?.asInt ?: 0 // Provide a default value for id if it's null
        val firstName = jsonObject?.get("first_name")?.asString
        val lastName = jsonObject?.get("last_name")?.asString
        val email = jsonObject?.get("email")?.asString
        val username = jsonObject?.get("username")?.asString
        val password = jsonObject?.get("password")?.asString
        val salt = jsonObject?.get("salt")?.asString ?: ""

        return User(id, firstName ?: "", lastName ?: "", email ?: "", username ?: "", password ?: "", salt)
    }
}