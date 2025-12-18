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
    private lateinit var adapter: RestaurantAdapter
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_restaurants)

        userId = intent.getIntExtra("USER_ID", 0)

        val db = AppDatabase.getDatabase(this)
        val repository = BookingRepository(db.userDao(), db.restaurantDao(), db.bookingDao())
        viewModel = BookingViewModel(repository)

        val recyclerView = findViewById<RecyclerView>(R.id.restaurantsRecyclerView)
        adapter = RestaurantAdapter { restaurant ->
            val intent = Intent(this, BookingActivity::class.java)
            intent.putExtra("USER_ID", userId)
            intent.putExtra("RESTAURANT_ID", restaurant.id)
            intent.putExtra("RESTAURANT_NAME", restaurant.name)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            val restaurants = viewModel.getAllRestaurants()
            adapter.submitList(restaurants)
        }
    }
}