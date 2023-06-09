package com.example.gravefinder

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val signUpButton = findViewById<Button>(R.id.buttonSignUp)
        val backButton = findViewById<Button>(R.id.buttonBack)
        signUpButton.setOnClickListener {
            signUp()
        }
        backButton.setOnClickListener {
            redirectToLogin()
        }
    }
    private fun redirectToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Optional: Finish the current activity to prevent going back to it with the back button
    }
    private fun signUp() {
        // Get the user input from EditText fields
        val firstName = findViewById<EditText>(R.id.editTextFirstName).text.toString()
        val lastName = findViewById<EditText>(R.id.editTextLastName).text.toString()
        val email = findViewById<EditText>(R.id.editTextEmail).text.toString()
        val username = findViewById<EditText>(R.id.editTextUsername).text.toString()
        val password = findViewById<EditText>(R.id.editTextPassword).text.toString()

        // Create an instance of the API service using Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val userService = retrofit.create(ApiService::class.java)

        // Make the API call to register the user
        val call: Call<SignUpResponse> = userService.signUp(
            SignUpRequest(firstName, lastName, email, username, password)
        )

        call.enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                if (response.isSuccessful) {
                    val signUpResponse = response.body()
                    if (signUpResponse != null) {
                        // Registration successful
                        Toast.makeText(applicationContext, "Registration successful", Toast.LENGTH_SHORT).show()
                        redirectToLogin()
                    } else {
                        // Handle registration failure
                        Toast.makeText(applicationContext, "Registration failed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Handle API error
                    Toast.makeText(applicationContext, "API error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                // Handle network error
                Toast.makeText(applicationContext, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }


}