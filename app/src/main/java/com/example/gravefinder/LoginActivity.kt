package com.example.gravefinder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec


// Function to hash the password using PBKDF2 SHA256
fun hashPassword(password: String): String {
    val iterations = 600000
    val keyLength = 256
    val saltLength = 32

    // Generate a random salt
    val secureRandom = SecureRandom()
    val salt = ByteArray(saltLength)
    secureRandom.nextBytes(salt)

    val spec = PBEKeySpec(password.toCharArray(), salt, iterations, keyLength)
    val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
    val hashedPassword = factory.generateSecret(spec).encoded

    // Concatenate the salt and hashed password
    val saltedHash = ByteArray(salt.size + hashedPassword.size)
    System.arraycopy(salt, 0, saltedHash, 0, salt.size)
    System.arraycopy(hashedPassword, 0, saltedHash, salt.size, hashedPassword.size)

    return Base64.encodeToString(saltedHash, Base64.DEFAULT)
}

const val BASE_URL = "http://192.168.0.120:8000/"

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val loginButton = findViewById<Button>(R.id.buttonLogin)

        loginButton.setOnClickListener {
            fetchUsers()
        }
    }
    public fun openRegistrationActivity(view: View) {
        val intent = Intent(this, RegistrationActivity::class.java)

        startActivity(intent)
    }
    private fun showDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish() // Optional: Finish the current activity to prevent going back to it with the back button
    }
    private fun fetchUsers() {
        val enteredUsername = findViewById<EditText>(R.id.editTextUsername).text.toString()
        val enteredPassword = findViewById<EditText>(R.id.editTextPassword).text.toString()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val userService = retrofit.create(ApiService::class.java)
        val call: Call<LoginResponse> = userService.login(LoginRequest(enteredUsername, enteredPassword))


        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        // Login successful
                        Toast.makeText(applicationContext, "Login successful", Toast.LENGTH_SHORT).show()
                        val firstname = loginResponse.first_name

                        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("firstname", firstname)
                        editor.apply()
                        showDashboard()
                    } else {
                        // Invalid credentials
                        Toast.makeText(applicationContext, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Handle API error
                    Toast.makeText(applicationContext, "API error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Handle network error
                Toast.makeText(applicationContext, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
