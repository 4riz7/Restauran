package com.example.restaurantbooking.data.dao

import androidx.room.*
import com.example.restaurantbooking.data.Restaurant
import com.example.restaurantbooking.data.RestaurantWithRating

@Dao
interface RestaurantDao {
    @Insert
    suspend fun insert(restaurant: Restaurant)

    @Query("SELECT * FROM restaurants")
    suspend fun getAllRestaurants(): List<Restaurant>

    @Query("SELECT restaurants.*, AVG(reviews.rating) as rating " +
            "FROM restaurants " +
            "LEFT JOIN reviews ON restaurants.id = reviews.restaurantId " +
            "GROUP BY restaurants.id")
    suspend fun getAllRestaurantsWithRating(): List<RestaurantWithRating>

    @Query("SELECT * FROM restaurants WHERE id = :id")
    suspend fun getRestaurantById(id: Int): Restaurant?

    @Delete
    suspend fun delete(restaurant: Restaurant)
}