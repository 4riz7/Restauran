package com.example.restaurantbooking.data

import androidx.room.Embedded

data class RestaurantWithRating(
    @Embedded val restaurant: Restaurant,
    val rating: Double?
)
