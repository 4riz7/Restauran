package com.example.restaurantbooking.data.dao

import androidx.room.*
import com.example.restaurantbooking.data.Booking
import com.example.restaurantbooking.data.BookingWithRestaurant

@Dao
interface BookingDao {
    @Insert
    suspend fun insert(booking: Booking)

    @Query("SELECT * FROM bookings WHERE restaurantId = :restaurantId ORDER BY date, time")
    suspend fun getBookingsByRestaurant(restaurantId: Int): List<Booking>

    @Query("SELECT * FROM bookings ORDER BY date, time")
    suspend fun getAllBookings(): List<Booking>

    @Query("SELECT bookings.*, restaurants.name as restaurantName, users.name as userName, users.email as userEmail FROM bookings INNER JOIN restaurants ON bookings.restaurantId = restaurants.id INNER JOIN users ON bookings.userId = users.id WHERE bookings.userId = :userId ORDER BY date, time")
    suspend fun getUserBookings(userId: Int): List<BookingWithRestaurant>

    @Query("SELECT bookings.*, restaurants.name as restaurantName, users.name as userName, users.email as userEmail FROM bookings INNER JOIN restaurants ON bookings.restaurantId = restaurants.id INNER JOIN users ON bookings.userId = users.id WHERE bookings.restaurantId = :restaurantId ORDER BY date, time")
    suspend fun getBookingsWithRestaurantByRestaurantId(restaurantId: Int): List<BookingWithRestaurant>

    @Query("SELECT bookings.*, restaurants.name as restaurantName, users.name as userName, users.email as userEmail FROM bookings INNER JOIN restaurants ON bookings.restaurantId = restaurants.id INNER JOIN users ON bookings.userId = users.id ORDER BY date, time")
    suspend fun getAllBookingsWithRestaurant(): List<BookingWithRestaurant>

    @Query("SELECT COUNT(*) FROM bookings WHERE restaurantId = :restaurantId AND date = :date AND time = :time")
    suspend fun countBookingsByRestaurantAndDateTime(restaurantId: Int, date: String, time: String): Int

    @Delete
    suspend fun delete(booking: Booking)

    @Query("DELETE FROM bookings WHERE id = :bookingId")
    suspend fun deleteBookingById(bookingId: Int)
}