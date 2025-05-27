package com.example.task41taskmanagerapp.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    fun formatDate(date: Date): String {
        return dateFormat.format(date)
    }

    fun formatDateTime(date: Date): String {
        return dateTimeFormat.format(date)
    }

    fun parseDate(dateString: String): Date? {
        return try {
            dateFormat.parse(dateString)
        } catch (e: Exception) {
            null
        }
    }

    fun isToday(date: Date): Boolean {
        val today = Calendar.getInstance()
        val checkDate = Calendar.getInstance()
        checkDate.time = date

        return today.get(Calendar.YEAR) == checkDate.get(Calendar.YEAR) &&
                today.get(Calendar.DAY_OF_YEAR) == checkDate.get(Calendar.DAY_OF_YEAR)
    }

    fun isOverdue(date: Date): Boolean {
        return date.before(Date()) && !isToday(date)
    }

    fun getDaysUntilDue(date: Date): Long {
        val today = Calendar.getInstance()
        val dueDate = Calendar.getInstance()
        dueDate.time = date

        val diffInMs = dueDate.timeInMillis - today.timeInMillis
        return diffInMs / (1000 * 60 * 60 * 24)
    }
}