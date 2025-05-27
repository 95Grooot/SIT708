package com.example.lostandfoundmanager.database

import android.content.Context
import androidx.room.*
import java.util.*

@Database(
    entities = [AdvertItem::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AdvertDatabase : RoomDatabase() {
    abstract fun advertDao(): AdvertDao

    companion object {
        @Volatile
        private var INSTANCE: AdvertDatabase? = null

        fun getDatabase(context: Context): AdvertDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AdvertDatabase::class.java,
                    "advert_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}