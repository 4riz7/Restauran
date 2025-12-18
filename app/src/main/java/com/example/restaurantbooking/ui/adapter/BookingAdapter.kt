package com.example.restaurantbooking.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantbooking.R
import com.example.restaurantbooking.data.Booking

class BookingAdapter : ListAdapter<Booking, BookingAdapter.ViewHolder>(BookingDiffCallback()) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val dateTextView: TextView = view.findViewById(R.id.dateTextView)
        private val timeTextView: TextView = view.findViewById(R.id.timeTextView)
        private val guestsTextView: TextView = view.findViewById(R.id.guestsTextView)
        private val bookingIdTextView: TextView = view.findViewById(R.id.bookingIdTextView)

        fun bind(booking: Booking) {
            dateTextView.text = booking.date
            timeTextView.text = booking.time
            guestsTextView.text = "Гостей: ${booking.guests}"
            bookingIdTextView.text = String.format("[%04d]", booking.id)
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

class BookingDiffCallback : DiffUtil.ItemCallback<Booking>() {
    override fun areItemsTheSame(oldItem: Booking, newItem: Booking): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Booking, newItem: Booking): Boolean {
        return oldItem == newItem
    }
}