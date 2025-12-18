package com.example.restaurantbooking.data.dao

import androidx.room.*
import com.example.restaurantbooking.data.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend fun login(email: String, password: String): User?

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Int): User?

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE role = 'admin' AND restaurantId = :restaurantId")
    suspend fun getAdminByRestaurant(restaurantId: Int): User?

    @Query("SELECT * FROM users WHERE role = 'superadmin'")
    suspend fun getSuperAdmin(): User?

    @Query("SELECT * FROM users WHERE role = 'user'")
    suspend fun getAllUsers(): List<User>
}