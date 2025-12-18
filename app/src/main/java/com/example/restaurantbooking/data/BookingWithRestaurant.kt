package com.example.restaurantbooking.data

import androidx.room.Embedded

data class BookingWithRestaurant(
    @Embedded val booking: Booking,
    val restaurantName: String,
    val userName: String?,
    val userEmail: String?
)
