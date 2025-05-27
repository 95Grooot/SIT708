package com.example.lostandfoundmanager.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "adverts")
data class AdvertItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String, // "Lost" or "Found"
    val name: String,
    val phone: String,
    val description: String,
    val date: Date,
    val location: String,
    val createdAt: Date = Date()
)