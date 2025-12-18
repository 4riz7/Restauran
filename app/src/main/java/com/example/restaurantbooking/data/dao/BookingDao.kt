package com.example.restaurantbooking.data.dao

import androidx.room.*
import com.example.restaurantbooking.data.Booking

@Dao
interface BookingDao {
    @Insert
    suspend fun insert(booking: Booking)

    @Query("SELECT * FROM bookings WHERE restaurantId = :restaurantId ORDER BY date, time")
    suspend fun getBookingsByRestaurant(restaurantId: Int): List<Booking>

    @Query("SELECT * FROM bookings ORDER BY date, time")
    suspend fun getAllBookings(): List<Booking>

    @Query("SELECT * FROM bookings WHERE userId = :userId ORDER BY date, time")
    suspend fun getUserBookings(userId: Int): List<Booking>

    @Delete
    suspend fun delete(booking: Booking)
}