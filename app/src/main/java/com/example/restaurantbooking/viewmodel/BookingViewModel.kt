package com.example.restaurantbooking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurantbooking.data.repository.BookingRepository
import kotlinx.coroutines.launch

class BookingViewModel(private val repository: BookingRepository) : ViewModel() {

    init {
        viewModelScope.launch {
            repository.createSuperAdminIfNotExists()
        }
    }

    suspend fun registerUser(name: String, email: String, password: String): Boolean {
        return repository.registerUser(name, email, password)
    }

    suspend fun loginUser(email: String, password: String) = repository.loginUser(email, password)

    suspend fun addRestaurant(name: String, address: String, description: String = "", tableCount: Int = 10, websiteUrl: String = "") =
        repository.addRestaurant(name, address, description, tableCount, websiteUrl)

    suspend fun getAllRestaurants(): List<com.example.restaurantbooking.data.RestaurantWithRating> = repository.getAllRestaurants()

    suspend fun getRestaurantById(restaurantId: Int) = repository.getRestaurantById(restaurantId)

    suspend fun createBooking(userId: Int, restaurantId: Int, date: String, time: String, guests: Int): Boolean {
        return repository.createBooking(userId, restaurantId, date, time, guests)
    }

    suspend fun getBookingsForRestaurant(restaurantId: Int) = repository.getBookingsForRestaurant(restaurantId)

    suspend fun getAllBookings() = repository.getAllBookings()

    suspend fun createAdminForRestaurant(name: String, email: String, password: String, restaurantId: Int): Boolean {
        return repository.createAdminForRestaurant(name, email, password, restaurantId)
    }

    suspend fun getUserBookings(userId: Int) = repository.getUserBookings(userId)

    suspend fun deleteBooking(bookingId: Int) = repository.deleteBooking(bookingId)

    suspend fun getAdminsByRestaurant(restaurantId: Int) = repository.getAdminsByRestaurant(restaurantId)

    suspend fun deleteUser(user: com.example.restaurantbooking.data.User) = repository.deleteUser(user)

    suspend fun updateUser(user: com.example.restaurantbooking.data.User) = repository.updateUser(user)

    suspend fun addReview(restaurantId: Int, userId: Int, userName: String, rating: Int, comment: String) {
        repository.addReview(restaurantId, userId, userName, rating, comment)
    }

    suspend fun getReviewsForRestaurant(restaurantId: Int) = repository.getReviewsForRestaurant(restaurantId)
}