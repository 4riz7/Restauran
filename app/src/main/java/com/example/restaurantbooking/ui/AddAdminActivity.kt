package com.example.restaurantbooking.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.restaurantbooking.R
import com.example.restaurantbooking.data.AppDatabase
import com.example.restaurantbooking.viewmodel.BookingViewModel
import com.example.restaurantbooking.data.repository.BookingRepository
import kotlinx.coroutines.launch

class AddAdminActivity : AppCompatActivity() {

    private lateinit var viewModel: BookingViewModel
    private var restaurantId: Int = 0
    private var restaurantName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_admin)

        restaurantId = intent.getIntExtra("RESTAURANT_ID", 0)
        restaurantName = intent.getStringExtra("RESTAURANT_NAME") ?: ""

        val db = AppDatabase.getDatabase(this)
        val repository = BookingRepository(db.userDao(), db.restaurantDao(), db.bookingDao())
        viewModel = BookingViewModel(repository)

        val restaurantNameTextView = findViewById<TextView>(R.id.restaurantNameTextView)
        restaurantNameTextView.text = "Ресторан: $restaurantName"

        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val addButton = findViewById<Button>(R.id.addButton)

        addButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val success = viewModel.createAdminForRestaurant(name, email, password, restaurantId)
                if (success) {
                    Toast.makeText(this@AddAdminActivity, "Администратор добавлен", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@AddAdminActivity, "Пользователь с таким email уже существует", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}