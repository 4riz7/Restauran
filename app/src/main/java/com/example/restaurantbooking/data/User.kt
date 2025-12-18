package com.example.restaurantbooking.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String,
    val password: String,
    val role: String = "user", // "user", "admin", "superadmin"
    val restaurantId: Int? = null // null для users, restaurantId для админов ресторана
)