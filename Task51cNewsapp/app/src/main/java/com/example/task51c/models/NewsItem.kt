package com.example.task51c.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsItem(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String,
    val category: String
) : Parcelable