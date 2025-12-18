package com.example.restaurantbooking.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class Review(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val restaurantId: Int,
    val userId: Int,
    val userName: String,
    val rating: Int, // 1-5
    val comment: String,
    val date: Long = System.currentTimeMillis()
)
