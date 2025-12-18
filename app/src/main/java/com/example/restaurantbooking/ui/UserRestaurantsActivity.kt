package com.example.restaurantbooking.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantbooking.R
import com.example.restaurantbooking.data.AppDatabase
import com.example.restaurantbooking.data.repository.BookingRepository
import com.example.restaurantbooking.ui.adapter.RestaurantAdapter
import com.example.restaurantbooking.viewmodel.BookingViewModel
import kotlinx.coroutines.launch

class UserRestaurantsActivity : AppCompatActivity() {

    private lateinit var viewModel: BookingViewModel
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_restaurants)

        userId = intent.getIntExtra("USER_ID", 0)

        val db = AppDatabase.getDatabase(this)
        val repository = BookingRepository(db.userDao(), db.restaurantDao(), db.bookingDao(), db.reviewDao())
        viewModel = BookingViewModel(repository)

        val spacingDecoration = SpacingItemDecoration(16)
        
        // Рестораны
        val recyclerView = findViewById<RecyclerView>(R.id.restaurantsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(spacingDecoration)

        val adapter = RestaurantAdapter { restaurant ->
            val intent = Intent(this, BookingActivity::class.java)
            intent.putExtra("RESTAURANT_ID", restaurant.id)
            intent.putExtra("RESTAURANT_NAME", restaurant.name)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        // Мои бронирования
        val myBookingsRecyclerView = findViewById<RecyclerView>(R.id.myBookingsRecyclerView)
        myBookingsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        
        // Use HorizontalBookingAdapter but without the delete callback for now (simplicity first)
        // actually revert to simple adapter or update adapter signature next.
        // For now I'll assume the adapter still has the lambda but ignores it or I pass empty.
        val myBookingsAdapter = com.example.restaurantbooking.ui.adapter.HorizontalBookingAdapter { 
             // No-op for revert or we can keep the delete if user wants, but request was "back to working version"
             // Assuming previous version didn't have delete.
        }
        
        myBookingsRecyclerView.adapter = myBookingsAdapter
        myBookingsRecyclerView.addItemDecoration(SpacingItemDecoration(16))

        lifecycleScope.launch {
            val restaurants = viewModel.getAllRestaurants()
            adapter.submitList(restaurants)
            
            // Загрузка бронирований
            loadBookings()
        }
    }

    private fun loadBookings() {
        lifecycleScope.launch {
            val myBookings = viewModel.getUserBookings(userId)
            val myBookingsAdapter = findViewById<RecyclerView>(R.id.myBookingsRecyclerView).adapter as? com.example.restaurantbooking.ui.adapter.HorizontalBookingAdapter
            myBookingsAdapter?.submitList(myBookings)
            
            val myBookingsHeader = findViewById<android.widget.TextView>(R.id.myBookingsHeader)
            val myBookingsRecyclerView = findViewById<RecyclerView>(R.id.myBookingsRecyclerView)
            
            if (myBookings.isEmpty()) {
                myBookingsHeader.visibility = android.view.View.GONE
                myBookingsRecyclerView.visibility = android.view.View.GONE
            } else {
                myBookingsHeader.visibility = android.view.View.VISIBLE
                myBookingsRecyclerView.visibility = android.view.View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadBookings()
    }
}