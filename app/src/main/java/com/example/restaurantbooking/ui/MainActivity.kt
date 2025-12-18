package com.example.restaurantbooking.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.restaurantbooking.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Seed Data
        val db = com.example.restaurantbooking.data.AppDatabase.getDatabase(this)
        val repo = com.example.restaurantbooking.data.repository.BookingRepository(db.userDao(), db.restaurantDao(), db.bookingDao(), db.reviewDao())
        val seeder = com.example.restaurantbooking.data.TestDataSeeder(repo)
        
        lifecycleScope.launch {
            seeder.seedData()
        }

        // Проверяем, есть ли сохраненная сессия пользователя
        // Если нет - показываем кнопки входа/регистрации

        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.registerButton)

        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        registerButton.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }
}