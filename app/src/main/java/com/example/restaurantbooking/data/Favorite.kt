package com.example.restaurantbooking.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "favorites",
    primaryKeys = ["userId", "restaurantId"],
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Restaurant::class, parentColumns = ["id"], childColumns = ["restaurantId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("userId"), Index("restaurantId")]
)
data class Favorite(
    val userId: Int,
    val restaurantId: Int
)
