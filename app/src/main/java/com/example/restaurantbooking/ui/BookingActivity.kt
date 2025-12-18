package com.example.restaurantbooking.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.restaurantbooking.R
import com.example.restaurantbooking.data.AppDatabase
import com.example.restaurantbooking.viewmodel.BookingViewModel
import com.example.restaurantbooking.data.repository.BookingRepository
import kotlinx.coroutines.launch
import java.util.*

class BookingActivity : AppCompatActivity() {

    private lateinit var viewModel: BookingViewModel
    private var userId: Int = 0
    private var restaurantId: Int = 0
    private var restaurantName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        userId = intent.getIntExtra("USER_ID", 0)
        restaurantId = intent.getIntExtra("RESTAURANT_ID", 0)
        restaurantName = intent.getStringExtra("RESTAURANT_NAME") ?: ""

        val db = AppDatabase.getDatabase(this)
        val repository = BookingRepository(db.userDao(), db.restaurantDao(), db.bookingDao(), db.reviewDao())
        viewModel = BookingViewModel(repository)

        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        titleTextView.text = "Бронирование в $restaurantName"

        val websiteTextView = findViewById<TextView>(R.id.websiteTextView)
        if (websiteTextView != null) {
             lifecycleScope.launch {
                val r = viewModel.getRestaurantById(restaurantId)
                if (r != null && r.websiteUrl.isNotEmpty()) {
                    websiteTextView.visibility = View.VISIBLE
                    websiteTextView.setOnClickListener {
                        val i = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(r.websiteUrl))
                        startActivity(i)
                    }
                }
             }
        }

        val dateEditText = findViewById<EditText>(R.id.dateEditText)
        val timeEditText = findViewById<EditText>(R.id.timeEditText)
        val guestsNumberPicker = findViewById<NumberPicker>(R.id.guestsNumberPicker)
        val bookButton = findViewById<Button>(R.id.bookButton)

        // Настройка NumberPicker
        guestsNumberPicker.minValue = 1
        guestsNumberPicker.maxValue = 10
        guestsNumberPicker.value = 1

        // Выбор даты
        dateEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    val selectedDate = String.format("%02d.%02d.%d", day, month + 1, year)
                    dateEditText.setText(selectedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Выбор времени
        timeEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(
                this,
                { _, hour, minute ->
                    val selectedTime = String.format("%02d:%02d", hour, minute)
                    timeEditText.setText(selectedTime)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }

        // Рецензии
        val reviewsRecyclerView = findViewById<RecyclerView>(R.id.reviewsRecyclerView)
        val reviewAdapter = com.example.restaurantbooking.ui.adapter.ReviewAdapter()
        reviewsRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        reviewsRecyclerView.adapter = reviewAdapter

        val toggleReviewFormButton = findViewById<Button>(R.id.toggleReviewFormButton)
        val reviewFormLayout = findViewById<LinearLayout>(R.id.reviewFormLayout)
        val submitReviewButton = findViewById<Button>(R.id.submitReviewButton)
        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        val reviewCommentEditText = findViewById<EditText>(R.id.reviewCommentEditText)

        toggleReviewFormButton.setOnClickListener {
            if (reviewFormLayout.visibility == android.view.View.VISIBLE) {
                reviewFormLayout.visibility = android.view.View.GONE
                toggleReviewFormButton.text = "Оставить отзыв"
            } else {
                reviewFormLayout.visibility = android.view.View.VISIBLE
                toggleReviewFormButton.text = "Скрыть форму"
            }
        }

        submitReviewButton.setOnClickListener {
            val rating = ratingBar.rating.toInt()
            val comment = reviewCommentEditText.text.toString()

            if (rating == 0) {
                Toast.makeText(this, "Поставьте оценку", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                // В реальном приложении нужно получить имя пользователя из сессии
                val userName = "User #$userId" 
                viewModel.addReview(restaurantId, userId, userName, rating, comment)
                Toast.makeText(this@BookingActivity, "Отзыв добавлен", Toast.LENGTH_SHORT).show()
                
                // Сброс формы и обновление списка
                ratingBar.rating = 0f
                reviewCommentEditText.text.clear()
                reviewFormLayout.visibility = android.view.View.GONE
                toggleReviewFormButton.text = "Оставить отзыв"
                
                val reviews = viewModel.getReviewsForRestaurant(restaurantId)
                reviewAdapter.submitList(reviews)
            }
        }
        
        // Загрузка отзывов
        lifecycleScope.launch {
            val reviews = viewModel.getReviewsForRestaurant(restaurantId)
            reviewAdapter.submitList(reviews)
        }

        // Бронирование
        bookButton.setOnClickListener {
            val date = dateEditText.text.toString()
            val time = timeEditText.text.toString()
            val guests = guestsNumberPicker.value

            if (date.isEmpty() || time.isEmpty()) {
                Toast.makeText(this, "Выберите дату и время", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val success = viewModel.createBooking(userId, restaurantId, date, time, guests)
                if (success) {
                    Toast.makeText(this@BookingActivity, "Столик успешно забронирован!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@BookingActivity, "Нет свободных столиков на это время", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}