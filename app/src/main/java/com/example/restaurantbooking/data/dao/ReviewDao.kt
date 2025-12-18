package com.example.restaurantbooking.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.restaurantbooking.data.Review

@Dao
interface ReviewDao {
    @Insert
    suspend fun insert(review: Review)

    @Query("SELECT * FROM reviews WHERE restaurantId = :restaurantId ORDER BY date DESC")
    suspend fun getReviewsForRestaurant(restaurantId: Int): List<Review>
}
