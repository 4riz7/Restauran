package com.example.restaurantbooking.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurants")
data class Restaurant(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val address: String,
    val description: String = "",
    val tableCount: Int = 10,
    val websiteUrl: String = ""
)