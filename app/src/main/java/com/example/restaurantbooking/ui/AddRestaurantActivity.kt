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
        val repository = BookingRepository(db.userDao(), db.restaurantDao(), db.bookingDao(), db.reviewDao())
        viewModel = BookingViewModel(repository)

        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val addressEditText = findViewById<EditText>(R.id.addressEditText)
        val descriptionEditText = findViewById<EditText>(R.id.descriptionEditText)
        val tableCountEditText = findViewById<EditText>(R.id.tableCountEditText)
        val websiteEditText = findViewById<EditText>(R.id.websiteEditText) // New field
        val saveButton = findViewById<Button>(R.id.saveButton)

        saveButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val address = addressEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val tableCountStr = tableCountEditText.text.toString()
            val websiteUrl = websiteEditText.text.toString()

            if (name.isEmpty() || address.isEmpty() || tableCountStr.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val tableCount = tableCountStr.toIntOrNull() ?: 10

            lifecycleScope.launch {
                viewModel.addRestaurant(name, address, description, tableCount, websiteUrl)
                Toast.makeText(this@AddRestaurantActivity, "Ресторан добавлен", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
