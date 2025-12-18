package com.example.restaurantbooking.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantbooking.R
import com.example.restaurantbooking.data.AppDatabase
import com.example.restaurantbooking.data.repository.BookingRepository
import com.example.restaurantbooking.ui.adapter.BookingAdapter
import com.example.restaurantbooking.ui.adapter.RestaurantAdapter
import com.example.restaurantbooking.viewmodel.BookingViewModel
import kotlinx.coroutines.launch

class SuperAdminActivity : AppCompatActivity() {

    private lateinit var viewModel: BookingViewModel
    private lateinit var bookingsAdapter: BookingAdapter
    private lateinit var restaurantsAdapter: RestaurantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_super_admin)

        val db = AppDatabase.getDatabase(this)
        val repository = BookingRepository(db.userDao(), db.restaurantDao(), db.bookingDao())
        viewModel = BookingViewModel(repository)

        val addRestaurantButton = findViewById<Button>(R.id.addRestaurantButton)
        val allBookingsRecyclerView = findViewById<RecyclerView>(R.id.allBookingsRecyclerView)
        val allRestaurantsRecyclerView = findViewById<RecyclerView>(R.id.allRestaurantsRecyclerView)

        // Настройка адаптера для бронирований
        bookingsAdapter = BookingAdapter()
        allBookingsRecyclerView.layoutManager = LinearLayoutManager(this)
        allBookingsRecyclerView.adapter = bookingsAdapter

        // Настройка адаптера для ресторанов
        restaurantsAdapter = RestaurantAdapter { restaurant ->
            val intent = Intent(this, AddAdminActivity::class.java)
            intent.putExtra("RESTAURANT_ID", restaurant.id)
            intent.putExtra("RESTAURANT_NAME", restaurant.name)
            startActivity(intent)
        }
        allRestaurantsRecyclerView.layoutManager = LinearLayoutManager(this)
        allRestaurantsRecyclerView.adapter = restaurantsAdapter

        addRestaurantButton.setOnClickListener {
            val intent = Intent(this, AddRestaurantActivity::class.java)
            startActivity(intent)
        }

        loadData()
    }

    private fun loadData() {
        lifecycleScope.launch {
            val bookings = viewModel.getAllBookings()
            bookingsAdapter.submitList(bookings)

            val restaurants = viewModel.getAllRestaurants()
            restaurantsAdapter.submitList(restaurants)
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }
}