package com.example.restaurantbooking.data.repository

import com.example.restaurantbooking.data.*
import com.example.restaurantbooking.data.dao.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BookingRepository(
    private val userDao: UserDao,
    private val restaurantDao: RestaurantDao,
    private val bookingDao: BookingDao,
    private val reviewDao: ReviewDao
) {
    // ... existing code ...

    // Review operations
    suspend fun addReview(restaurantId: Int, userId: Int, userName: String, rating: Int, comment: String) {
        val review = Review(restaurantId = restaurantId, userId = userId, userName = userName, rating = rating, comment = comment)
        reviewDao.insert(review)
    }

    suspend fun getReviewsForRestaurant(restaurantId: Int): List<Review> {
        return reviewDao.getReviewsForRestaurant(restaurantId)
    }

    // User operations
    suspend fun registerUser(name: String, email: String, password: String): Boolean {
        if (userDao.getUserByEmail(email) != null) return false
        val user = User(name = name, email = email, password = password, role = "user")
        userDao.insert(user)
        return true
    }

    suspend fun loginUser(email: String, password: String): User? {
        return userDao.login(email, password)
    }

    // Restaurant operations
    suspend fun addRestaurant(name: String, address: String, description: String = "", tableCount: Int = 10, websiteUrl: String = ""): Restaurant {
        val restaurant = Restaurant(name = name, address = address, description = description, tableCount = tableCount, websiteUrl = websiteUrl)
        restaurantDao.insert(restaurant)
        return restaurant
    }

    suspend fun getAllRestaurants(): List<RestaurantWithRating> {
        return restaurantDao.getAllRestaurantsWithRating()
    }

    suspend fun getRestaurantById(restaurantId: Int): Restaurant? {
        return restaurantDao.getRestaurantById(restaurantId)
    }
    

    // Booking operations
    suspend fun createBooking(userId: Int, restaurantId: Int, date: String, time: String, guests: Int): Boolean {
        val restaurant = restaurantDao.getRestaurantById(restaurantId) ?: return false
        val currentBookings = bookingDao.countBookingsByRestaurantAndDateTime(restaurantId, date, time)

        if (currentBookings >= restaurant.tableCount) {
             return false // No tables available
        }

        val booking = Booking(
            userId = userId,
            restaurantId = restaurantId,
            date = date,
            time = time,
            guests = guests
        )
        bookingDao.insert(booking)
        
        return true
    }

    suspend fun getBookingsForRestaurant(restaurantId: Int): List<BookingWithRestaurant> {
        return bookingDao.getBookingsWithRestaurantByRestaurantId(restaurantId)
    }

    suspend fun getAllBookings(): List<BookingWithRestaurant> {
        return bookingDao.getAllBookingsWithRestaurant()
    }

    suspend fun getUserBookings(userId: Int): List<BookingWithRestaurant> {
        return bookingDao.getUserBookings(userId)
    }

    suspend fun deleteBooking(bookingId: Int) {
        bookingDao.deleteBookingById(bookingId)
    }

    // Admin operations
    suspend fun createAdminForRestaurant(name: String, email: String, password: String, restaurantId: Int): Boolean {
        if (userDao.getUserByEmail(email) != null) return false
        val admin = User(
            name = name,
            email = email,
            password = password,
            role = "admin",
            restaurantId = restaurantId
        )
        userDao.insert(admin)
        return true
    }

    suspend fun createSuperAdminIfNotExists() {
        if (userDao.getUserByEmail("admin") == null) {
            val admin = User(
                name = "Super Admin",
                email = "admin",
                password = "123456",
                role = "superadmin"
            )
            userDao.insert(admin)
        }
    }
    
    suspend fun getUserById(userId: Int): User? {
        return userDao.getUserById(userId)
    }
    
    suspend fun updateUser(user: User) {
        userDao.update(user)
    }
}