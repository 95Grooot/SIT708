package com.example.lostandfoundmanager.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lost_found_items")
data class LostFoundItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String, // "Lost" or "Found"
    val name: String,
    val phone: String,
    val description: String,
    val date: String,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long = System.currentTimeMillis()
)