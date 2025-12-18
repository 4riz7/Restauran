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
        val repository = BookingRepository(db.userDao(), db.restaurantDao(), db.bookingDao(), db.reviewDao())
        viewModel = BookingViewModel(repository)

        val addRestaurantButton = findViewById<Button>(R.id.addRestaurantButton)
        val allBookingsRecyclerView = findViewById<RecyclerView>(R.id.allBookingsRecyclerView)
        val allRestaurantsRecyclerView = findViewById<RecyclerView>(R.id.allRestaurantsRecyclerView)

        // Настройка адаптера для бронирований
        bookingsAdapter = BookingAdapter { item ->
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Удаление бронирования")
                .setMessage("Удалить бронирование #${item.booking.id}?")
                .setPositiveButton("Удалить") { _, _ ->
                    lifecycleScope.launch {
                        viewModel.deleteBookingById(item.booking.id)
                        loadData()
                    }
                }
                .setNegativeButton("Отмена", null)
                .show()
        }
        allBookingsRecyclerView.layoutManager = LinearLayoutManager(this)
        allBookingsRecyclerView.adapter = bookingsAdapter
 
        // Настройка адаптера для ресторанов
        restaurantsAdapter = RestaurantAdapter(
            onItemClick = { restaurant ->
                val intent = Intent(this, ManageAdminsActivity::class.java)
                intent.putExtra("RESTAURANT_ID", restaurant.id)
                intent.putExtra("RESTAURANT_NAME", restaurant.name)
                startActivity(intent)
            },
            onLongClick = { restaurant ->
                androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Удаление ресторана")
                    .setMessage("Вы уверены, что хотите удалить ресторан '${restaurant.name}'? Это удалит все связанные данные.")
                    .setPositiveButton("Удалить") { _, _ ->
                        lifecycleScope.launch {
                            viewModel.deleteRestaurant(restaurant)
                            loadData()
                        }
                    }
                    .setNegativeButton("Отмена", null)
                    .show()
            }
        )
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

            val restaurants = viewModel.getAllRestaurants() // 0 or any dummy ID for admin
            restaurantsAdapter.submitList(restaurants)
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }
}