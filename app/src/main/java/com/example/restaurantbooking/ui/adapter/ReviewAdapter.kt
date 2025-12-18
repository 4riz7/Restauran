package com.example.restaurantbooking.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantbooking.R
import com.example.restaurantbooking.data.Review
import java.text.SimpleDateFormat
import java.util.*

class ReviewAdapter : ListAdapter<Review, ReviewAdapter.ViewHolder>(ReviewDiffCallback()) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val userNameTextView: TextView = view.findViewById(R.id.userNameTextView)
        private val ratingTextView: TextView = view.findViewById(R.id.ratingTextView)
        private val commentTextView: TextView = view.findViewById(R.id.commentTextView)
        private val dateTextView: TextView = view.findViewById(R.id.dateTextView)

        fun bind(review: Review) {
            userNameTextView.text = review.userName
            ratingTextView.text = "${review.rating}/5"
            commentTextView.text = review.comment
            
            val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            dateTextView.text = sdf.format(Date(review.date))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ReviewDiffCallback : DiffUtil.ItemCallback<Review>() {
    override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem == newItem
    }
}
