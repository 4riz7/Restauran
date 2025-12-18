package com.example.restaurantbooking.data.dao

import androidx.room.*
import com.example.restaurantbooking.data.Restaurant

@Dao
interface RestaurantDao {
    @Insert
    suspend fun insert(restaurant: Restaurant)

    @Query("SELECT * FROM restaurants")
    suspend fun getAllRestaurants(): List<Restaurant>

    @Query("SELECT * FROM restaurants WHERE id = :id")
    suspend fun getRestaurantById(id: Int): Restaurant?

    @Delete
    suspend fun delete(restaurant: Restaurant)
}