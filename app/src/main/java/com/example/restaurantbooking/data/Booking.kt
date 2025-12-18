package com.example.restaurantbooking.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "bookings")
data class Booking(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val restaurantId: Int,
    val date: String, // Format: "15.12.2024"
    val time: String, // Format: "19:00"
    val guests: Int,
    val createdAt: Long = System.currentTimeMillis()
)