package com.example.restaurantbooking.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantbooking.R
import com.example.restaurantbooking.data.Restaurant
import com.example.restaurantbooking.data.RestaurantWithRating

class RestaurantAdapter(
    private val onItemClick: (Restaurant) -> Unit = {}
) : ListAdapter<RestaurantWithRating, RestaurantAdapter.ViewHolder>(RestaurantDiffCallback()) {

    class ViewHolder(view: View, private val onItemClick: (Restaurant) -> Unit) : RecyclerView.ViewHolder(view) {
        private val nameTextView: TextView = view.findViewById(R.id.restaurantNameTextView)
        private val addressTextView: TextView = view.findViewById(R.id.restaurantAddressTextView)
        private val ratingTextView: TextView = view.findViewById(R.id.restaurantRatingTextView)
        private var currentItem: RestaurantWithRating? = null

        init {
            view.setOnClickListener {
                currentItem?.restaurant?.let { onItemClick(it) }
            }
        }

        fun bind(item: RestaurantWithRating) {
            currentItem = item
            nameTextView.text = item.restaurant.name
            addressTextView.text = item.restaurant.address
            
            val rating = item.rating
            if (rating != null && rating > 0) {
                ratingTextView.visibility = View.VISIBLE
                ratingTextView.text = String.format("Рейтинг: %.1f ★", rating)
            } else {
                ratingTextView.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_restaurant, parent, false)
        return ViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class RestaurantDiffCallback : DiffUtil.ItemCallback<RestaurantWithRating>() {
    override fun areItemsTheSame(oldItem: RestaurantWithRating, newItem: RestaurantWithRating): Boolean {
        return oldItem.restaurant.id == newItem.restaurant.id
    }

    override fun areContentsTheSame(oldItem: RestaurantWithRating, newItem: RestaurantWithRating): Boolean {
        return oldItem == newItem
    }
}