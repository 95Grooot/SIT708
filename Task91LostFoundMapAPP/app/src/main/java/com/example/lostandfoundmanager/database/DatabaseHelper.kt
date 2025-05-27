package com.example.lostandfoundmanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lostandfoundmanager.models.LostFoundItem

@Database(
    entities = [LostFoundItem::class],
    version = 1,
    exportSchema = false
)
abstract class DatabaseHelper : RoomDatabase() {
    abstract fun itemsDao(): ItemsDao

    companion object {
        @Volatile
        private var INSTANCE: DatabaseHelper? = null

        fun getDatabase(context: Context): DatabaseHelper {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseHelper::class.java,
                    "lost_found_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}