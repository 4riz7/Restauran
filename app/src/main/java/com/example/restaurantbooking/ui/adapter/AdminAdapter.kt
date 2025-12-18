package com.example.restaurantbooking.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantbooking.R
import com.example.restaurantbooking.data.User

class AdminAdapter(
    private val onEditClick: (User) -> Unit,
    private val onDeleteClick: (User) -> Unit
) : ListAdapter<User, AdminAdapter.ViewHolder>(AdminDiffCallback()) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.adminNameTextView)
        val emailTextView: TextView = view.findViewById(R.id.adminEmailTextView)
        val editButton: Button = view.findViewById(R.id.editButton)
        val deleteButton: Button = view.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val admin = getItem(position)
        holder.nameTextView.text = admin.name
        holder.emailTextView.text = admin.email
        
        holder.editButton.setOnClickListener { onEditClick(admin) }
        holder.deleteButton.setOnClickListener { onDeleteClick(admin) }
    }
}

class AdminDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }
}
