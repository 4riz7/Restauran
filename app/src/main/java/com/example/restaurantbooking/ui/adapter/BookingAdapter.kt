package com.example.restaurantbooking.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantbooking.R
import com.example.restaurantbooking.data.BookingWithRestaurant

class BookingAdapter : ListAdapter<BookingWithRestaurant, BookingAdapter.ViewHolder>(BookingDiffCallback()) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val dateTextView: TextView = view.findViewById(R.id.dateTextView)
        private val timeTextView: TextView = view.findViewById(R.id.timeTextView)
        private val guestsTextView: TextView = view.findViewById(R.id.guestsTextView)
        private val bookingIdTextView: TextView = view.findViewById(R.id.bookingIdTextView)
        private val restaurantNameTextView: TextView = view.findViewById(R.id.restaurantNameTextView)
        private val userInfoTextView: TextView? = view.findViewById(R.id.userInfoTextView)

        fun bind(item: BookingWithRestaurant) {
            val booking = item.booking
            dateTextView.text = booking.date
            timeTextView.text = booking.time
            guestsTextView.text = "Гостей: ${booking.guests}"
            bookingIdTextView.text = String.format("[%04d]", booking.id)
            restaurantNameTextView.text = "Ресторан: ${item.restaurantName}"
            
            if (!item.userName.isNullOrEmpty()) {
                userInfoTextView?.visibility = View.VISIBLE
                userInfoTextView?.text = "Клиент: ${item.userName} (${item.userEmail})"
            } else {
                userInfoTextView?.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_booking, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class BookingDiffCallback : DiffUtil.ItemCallback<BookingWithRestaurant>() {
    override fun areItemsTheSame(oldItem: BookingWithRestaurant, newItem: BookingWithRestaurant): Boolean {
        return oldItem.booking.id == newItem.booking.id
    }

    override fun areContentsTheSame(oldItem: BookingWithRestaurant, newItem: BookingWithRestaurant): Boolean {
        return oldItem == newItem
    }
}