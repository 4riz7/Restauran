package com.example.restaurantbooking.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantbooking.R
import com.example.restaurantbooking.data.AppDatabase
import com.example.restaurantbooking.data.User
import com.example.restaurantbooking.data.repository.BookingRepository
import com.example.restaurantbooking.ui.adapter.AdminAdapter
import com.example.restaurantbooking.viewmodel.BookingViewModel
import kotlinx.coroutines.launch

class ManageAdminsActivity : AppCompatActivity() {

    private lateinit var viewModel: BookingViewModel
    private lateinit var adapter: AdminAdapter
    private var restaurantId: Int = 0
    private var restaurantName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_admins)

        restaurantId = intent.getIntExtra("RESTAURANT_ID", 0)
        restaurantName = intent.getStringExtra("RESTAURANT_NAME") ?: ""

        findViewById<TextView>(R.id.titleTextView).text = "Админы: $restaurantName"

        val db = AppDatabase.getDatabase(this)
        val repository = BookingRepository(db.userDao(), db.restaurantDao(), db.bookingDao(), db.reviewDao())
        viewModel = BookingViewModel(repository)

        val recyclerView = findViewById<RecyclerView>(R.id.adminsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        adapter = AdminAdapter(
            onEditClick = { admin -> showEditAdminDialog(admin) },
            onDeleteClick = { admin -> showDeleteConfirmDialog(admin) }
        )
        recyclerView.adapter = adapter

        findViewById<Button>(R.id.addNewAdminButton).setOnClickListener {
            val intent = Intent(this, AddAdminActivity::class.java)
            intent.putExtra("RESTAURANT_ID", restaurantId)
            intent.putExtra("RESTAURANT_NAME", restaurantName)
            startActivity(intent)
        }

        loadAdmins()
    }

    private fun loadAdmins() {
        lifecycleScope.launch {
            val admins = viewModel.getAdminsByRestaurant(restaurantId)
            adapter.submitList(admins)
        }
    }

    private fun showEditAdminDialog(admin: User) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_admin, null)
        val nameEditText = dialogView.findViewById<EditText>(R.id.editName)
        val emailEditText = dialogView.findViewById<EditText>(R.id.editEmail)
        val passwordEditText = dialogView.findViewById<EditText>(R.id.editPassword)

        nameEditText.setText(admin.name)
        emailEditText.setText(admin.email)
        passwordEditText.setText(admin.password)

        AlertDialog.Builder(this)
            .setTitle("Редактировать админа")
            .setView(dialogView)
            .setPositiveButton("Сохранить") { _, _ ->
                val newName = nameEditText.text.toString()
                val newEmail = emailEditText.text.toString()
                val newPassword = passwordEditText.text.toString()

                if (newName.isEmpty() || newEmail.isEmpty() || newPassword.isEmpty()) {
                    Toast.makeText(this, "Поля не могут быть пустыми", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                lifecycleScope.launch {
                    val updatedUser = admin.copy(name = newName, email = newEmail, password = newPassword)
                    viewModel.updateUser(updatedUser)
                    Toast.makeText(this@ManageAdminsActivity, "Обновлено", Toast.LENGTH_SHORT).show()
                    loadAdmins()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun showDeleteConfirmDialog(admin: User) {
        AlertDialog.Builder(this)
            .setTitle("Удаление")
            .setMessage("Вы уверены, что хотите удалить администратора ${admin.name}?")
            .setPositiveButton("Удалить") { _, _ ->
                lifecycleScope.launch {
                    viewModel.deleteUser(admin)
                    Toast.makeText(this@ManageAdminsActivity, "Администратор удален", Toast.LENGTH_SHORT).show()
                    loadAdmins()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        loadAdmins()
    }
}
