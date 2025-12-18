package com.example.restaurantbooking.data.dao

import androidx.room.*
import com.example.restaurantbooking.data.Favorite

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: Favorite)

    @Delete
    suspend fun delete(favorite: Favorite)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE userId = :userId AND restaurantId = :restaurantId)")
    suspend fun isFavorite(userId: Int, restaurantId: Int): Boolean

    @Query("SELECT restaurantId FROM favorites WHERE userId = :userId")
    suspend fun getUserFavorites(userId: Int): List<Int>
}
