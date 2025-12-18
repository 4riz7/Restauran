package com.example.restaurantbooking.data

import com.example.restaurantbooking.data.repository.BookingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TestDataSeeder(private val repository: BookingRepository) {

    suspend fun seedData() {
        withContext(Dispatchers.IO) {
            // 1. Create Restaurants
            if (repository.getAllRestaurants().isEmpty()) {
                val r1 = repository.addRestaurant(
                    name = "La Piazza",
                    address = "Main Street 1, City Center",
                    description = "Best Italian food in town.",
                    tableCount = 15,
                    websiteUrl = "https://example.com/lapiazza"
                )

                val r2 = repository.addRestaurant(
                    name = "Burger King (Mock)",
                    address = "Mall Avenue 5",
                    description = "Fast food and burgers.",
                    tableCount = 20,
                    websiteUrl = "https://burgerking.com"
                )
                
                val r3 = repository.addRestaurant(
                    name = "Sushi Master",
                    address = "Ocean Dr 12",
                    description = "Fresh sushi and rolls.",
                    tableCount = 8,
                    websiteUrl = "https://sushimaster.com"
                )

                // 2. Create Users
                repository.registerUser("John Doe", "user@test.com", "password")
                repository.registerUser("Alice Smith", "alice@test.com", "password")

                // 3. Create Reviews (Mocking IDs assuming 1-based indexing from room auto-generate)
                // r1 likely has ID 1, r2 has ID 2...
                // Users likely ID 1 (Standard User if created) or 2/3.
                // Note: repository.registerUser returns Mock/Boolean, doesn't return User ID immediately 
                // but we can query or just assume if DB was empty.
                
                // Let's rely on login or just raw insert if we had access, 
                // but repository.addReview takes IDs. 
                // We'll skip adding specific reviews linked to dynamic IDs to avoid crashes 
                // or we fetch them first.
                
                val restaurants = repository.getAllRestaurants()
                if (restaurants.isNotEmpty()) {
                    val r = restaurants[0]
                    repository.addReview(r.restaurant.id, 1, "Anonymous", 5, "Amazing pasta!")
                    repository.addReview(r.restaurant.id, 2, "Test User", 4, "Good service.")
                }
            }
        }
    }
}
