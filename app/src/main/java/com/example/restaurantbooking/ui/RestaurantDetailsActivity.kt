package com.example.restaurantbooking.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantbooking.R
import com.example.restaurantbooking.data.AppDatabase
import com.example.restaurantbooking.data.repository.BookingRepository
import com.example.restaurantbooking.ui.adapter.ReviewAdapter
import com.example.restaurantbooking.viewmodel.BookingViewModel
import kotlinx.coroutines.launch

class RestaurantDetailsActivity : AppCompatActivity() {

    private lateinit var viewModel: BookingViewModel
    private lateinit var reviewAdapter: ReviewAdapter
    private var userId: Int = 0
    private var restaurantId: Int = 0
    private var restaurantName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_details)

        userId = intent.getIntExtra("USER_ID", 0)
        restaurantId = intent.getIntExtra("RESTAURANT_ID", 0)
        restaurantName = intent.getStringExtra("RESTAURANT_NAME") ?: ""

        val db = AppDatabase.getDatabase(this)
        val repository = BookingRepository(db.userDao(), db.restaurantDao(), db.bookingDao(), db.reviewDao())
        viewModel = BookingViewModel(repository)

        findViewById<TextView>(R.id.restaurantNameTextView).text = restaurantName
        
        lifecycleScope.launch {
            val restaurant = viewModel.getRestaurantById(restaurantId)
            findViewById<TextView>(R.id.restaurantAddressTextView).text = restaurant?.address ?: ""
        }

        findViewById<Button>(R.id.goToBookingButton).setOnClickListener {
            val intent = Intent(this, BookingActivity::class.java)
            intent.putExtra("USER_ID", userId)
            intent.putExtra("RESTAURANT_ID", restaurantId)
            intent.putExtra("RESTAURANT_NAME", restaurantName)
            startActivity(intent)
        }

        // Reviews Setup
        val recyclerView = findViewById<RecyclerView>(R.id.reviewsRecyclerView)
        reviewAdapter = ReviewAdapter { review ->
            lifecycleScope.launch {
                val currentUser = viewModel.getUserById(userId)
                if (currentUser?.role == "superadmin") {
                    androidx.appcompat.app.AlertDialog.Builder(this@RestaurantDetailsActivity)
                        .setTitle("Удаление отзыва")
                        .setMessage("Вы уверены, что хотите удалить этот отзыв?")
                        .setPositiveButton("Удалить") { _, _ ->
                            lifecycleScope.launch {
                                viewModel.deleteReview(review.id)
                                loadReviews()
                                Toast.makeText(this@RestaurantDetailsActivity, "Отзыв удален", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .setNegativeButton("Отмена", null)
                        .show()
                }
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = reviewAdapter

        val toggleFormButton = findViewById<Button>(R.id.toggleReviewFormButton)
        val formLayout = findViewById<View>(R.id.reviewFormLayout)
        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        val commentEditText = findViewById<EditText>(R.id.reviewCommentEditText)
        val submitButton = findViewById<Button>(R.id.submitReviewButton)

        toggleFormButton.setOnClickListener {
            if (formLayout.visibility == View.VISIBLE) {
                formLayout.visibility = View.GONE
                toggleFormButton.text = "Оставить отзыв"
            } else {
                formLayout.visibility = View.VISIBLE
                toggleFormButton.text = "Скрыть форму"
            }
        }

        submitButton.setOnClickListener {
            val rating = ratingBar.rating.toInt()
            val comment = commentEditText.text.toString()

            if (rating == 0) {
                Toast.makeText(this, "Поставьте оценку", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                viewModel.addReview(restaurantId, userId, "User #$userId", rating, comment)
                Toast.makeText(this@RestaurantDetailsActivity, "Отзыв добавлен", Toast.LENGTH_SHORT).show()
                
                ratingBar.rating = 0f
                commentEditText.text.clear()
                formLayout.visibility = View.GONE
                toggleFormButton.text = "Оставить отзыв"
                
                loadReviews()
            }
        }

        loadReviews()
    }

    private fun loadReviews() {
        lifecycleScope.launch {
            val reviews = viewModel.getReviewsForRestaurant(restaurantId)
            reviewAdapter.submitList(reviews)
        }
    }
}
