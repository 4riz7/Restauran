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

class RestaurantAdapter(
    private val onItemClick: (Restaurant) -> Unit = {}
) : ListAdapter<Restaurant, RestaurantAdapter.ViewHolder>(RestaurantDiffCallback()) {

    class ViewHolder(view: View, private val onItemClick: (Restaurant) -> Unit) : RecyclerView.ViewHolder(view) {
        private val nameTextView: TextView = view.findViewById(R.id.restaurantNameTextView)
        private val addressTextView: TextView = view.findViewById(R.id.restaurantAddressTextView)
        private var currentRestaurant: Restaurant? = null

        init {
            view.setOnClickListener {
                currentRestaurant?.let { onItemClick(it) }
            }
        }

        fun bind(restaurant: Restaurant) {
            currentRestaurant = restaurant
            nameTextView.text = restaurant.name
            addressTextView.text = restaurant.address
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

class RestaurantDiffCallback : DiffUtil.ItemCallback<Restaurant>() {
    override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
        return oldItem == newItem
    }
}