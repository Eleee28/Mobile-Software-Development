package com.example.birds

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "birds")
data class Bird(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val imageResId: Int, // You might want to consider using a different approach for images
    val description: String
)
