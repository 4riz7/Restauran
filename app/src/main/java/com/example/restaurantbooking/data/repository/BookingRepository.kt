package com.example.restaurantbooking.data.repository

import com.example.restaurantbooking.data.*
import com.example.restaurantbooking.data.dao.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BookingRepository(
    private val userDao: UserDao,
    private val restaurantDao: RestaurantDao,
    private val bookingDao: BookingDao
) {
    // User operations
    suspend fun registerUser(name: String, email: String, password: String): Boolean {
        return try {
            val existingUser = userDao.getUserByEmail(email)
            if (existingUser != null) {
                false
            } else {
                val user = User(name = name, email = email, password = password, role = "user")
                userDao.insert(user)
                true
            }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun loginUser(email: String, password: String): User? {
        return userDao.login(email, password)
    }

    // Restaurant operations
    suspend fun addRestaurant(name: String, address: String, description: String = ""): Restaurant {
        val restaurant = Restaurant(name = name, address = address, description = description)
        restaurantDao.insert(restaurant)
        return restaurant
    }

    suspend fun getAllRestaurants(): List<Restaurant> {
        return restaurantDao.getAllRestaurants()
    }

    // Booking operations
    suspend fun createBooking(userId: Int, restaurantId: Int, date: String, time: String, guests: Int): Boolean {
        return try {
            val booking = Booking(
                userId = userId,
                restaurantId = restaurantId,
                date = date,
                time = time,
                guests = guests
            )
            bookingDao.insert(booking)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getBookingsForRestaurant(restaurantId: Int): List<Booking> {
        return bookingDao.getBookingsByRestaurant(restaurantId)
    }

    suspend fun getAllBookings(): List<Booking> {
        return bookingDao.getAllBookings()
    }

    suspend fun getUserBookings(userId: Int): List<Booking> {
        return bookingDao.getUserBookings(userId)
    }

    // Admin operations
    suspend fun createAdminForRestaurant(name: String, email: String, password: String, restaurantId: Int): Boolean {
        return try {
            val existingUser = userDao.getUserByEmail(email)
            if (existingUser != null) {
                false
            } else {
                val admin = User(
                    name = name,
                    email = email,
                    password = password,
                    role = "admin",
                    restaurantId = restaurantId
                )
                userDao.insert(admin)
                true
            }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun createSuperAdminIfNotExists() {
        val superAdmin = userDao.getSuperAdmin()
        if (superAdmin == null) {
            val admin = User(
                name = "Super Admin",
                email = "superadmin@admin.com",
                password = "admin123",
                role = "superadmin"
            )
            userDao.insert(admin)
        }
    }
}