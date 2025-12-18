package com.example.restaurantbooking.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantbooking.R
import com.example.restaurantbooking.data.AppDatabase
import com.example.restaurantbooking.data.repository.BookingRepository
import com.example.restaurantbooking.ui.adapter.BookingAdapter
import com.example.restaurantbooking.viewmodel.BookingViewModel
import kotlinx.coroutines.launch

class AdminPanelActivity : AppCompatActivity() {

    private lateinit var viewModel: BookingViewModel
    private lateinit var adapter: BookingAdapter
    private var restaurantId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)

        restaurantId = intent.getIntExtra("RESTAURANT_ID", 0)

        val db = AppDatabase.getDatabase(this)
        val repository = BookingRepository(db.userDao(), db.restaurantDao(), db.bookingDao())
        viewModel = BookingViewModel(repository)

        val recyclerView = findViewById<RecyclerView>(R.id.bookingsRecyclerView)
        adapter = BookingAdapter()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        loadBookings()
    }

    private fun loadBookings() {
        lifecycleScope.launch {
            val bookings = viewModel.getBookingsForRestaurant(restaurantId)
            adapter.submitList(bookings)
        }
    }
}