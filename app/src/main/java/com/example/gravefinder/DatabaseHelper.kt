package com.example.gravefinder

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.DriverManager

object DatabaseHelper {
    private var connection: Connection? = null

    suspend fun connect(): Connection? {
        return withContext(Dispatchers.IO) {
            try {
                Class.forName("org.postgresql.Driver")
                connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/gravefinderdb",
                    "postgres",
                    "mypassword"
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            connection
        }
    }

    fun disconnect() {
        try {
            connection?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}