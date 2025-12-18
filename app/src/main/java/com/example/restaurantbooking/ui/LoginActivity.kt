package com.example.restaurantbooking.ui

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.restaurantbooking.R
import com.example.restaurantbooking.data.AppDatabase
import com.example.restaurantbooking.viewmodel.BookingViewModel
import com.example.restaurantbooking.data.repository.BookingRepository
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: BookingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val db = AppDatabase.getDatabase(this)
        val repository = BookingRepository(db.userDao(), db.restaurantDao(), db.bookingDao())
        viewModel = BookingViewModel(repository)

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerLink = findViewById<TextView>(R.id.registerLink)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val user = viewModel.loginUser(email, password)
                if (user != null) {
                    when (user.role) {
                        "user" -> {
                            val intent = Intent(this@LoginActivity, UserRestaurantsActivity::class.java)
                            intent.putExtra("USER_ID", user.id)
                            startActivity(intent)
                        }
                        "admin" -> {
                            val intent = Intent(this@LoginActivity, AdminPanelActivity::class.java)
                            intent.putExtra("USER_ID", user.id)
                            intent.putExtra("RESTAURANT_ID", user.restaurantId)
                            startActivity(intent)
                        }
                        "superadmin" -> {
                            val intent = Intent(this@LoginActivity, SuperAdminActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Неверный email или пароль", Toast.LENGTH_SHORT).show()
                }
            }
        }

        registerLink.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }
}