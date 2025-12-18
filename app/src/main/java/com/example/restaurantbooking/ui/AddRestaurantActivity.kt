package com.example.restaurantbooking.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.restaurantbooking.R
import com.example.restaurantbooking.data.AppDatabase
import com.example.restaurantbooking.viewmodel.BookingViewModel
import com.example.restaurantbooking.data.repository.BookingRepository
import kotlinx.coroutines.launch

class AddRestaurantActivity : AppCompatActivity() {

    private lateinit var viewModel: BookingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_restaurant)

        val db = AppDatabase.getDatabase(this)
        val repository = BookingRepository(db.userDao(), db.restaurantDao(), db.bookingDao())
        viewModel = BookingViewModel(repository)

        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val addressEditText = findViewById<EditText>(R.id.addressEditText)
        val descriptionEditText = findViewById<EditText>(R.id.descriptionEditText)
        val addButton = findViewById<Button>(R.id.addButton)

        addButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val address = addressEditText.text.toString()
            val description = descriptionEditText.text.toString()

            if (name.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Заполните название и адрес", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                viewModel.addRestaurant(name, address, description)
                Toast.makeText(this@AddRestaurantActivity, "Ресторан добавлен", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}