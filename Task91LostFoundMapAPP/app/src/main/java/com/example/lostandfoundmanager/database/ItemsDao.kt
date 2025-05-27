package com.example.lostandfoundmanager.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lostandfoundmanager.models.LostFoundItem

@Dao
interface ItemsDao {
    @Query("SELECT * FROM lost_found_items ORDER BY timestamp DESC")
    fun getAllItems(): LiveData<List<LostFoundItem>>

    @Query("SELECT * FROM lost_found_items ORDER BY timestamp DESC")
    suspend fun getAllItemsList(): List<LostFoundItem>

    @Insert
    suspend fun insertItem(item: LostFoundItem): Long

    @Update
    suspend fun updateItem(item: LostFoundItem)

    @Delete
    suspend fun deleteItem(item: LostFoundItem)

    @Query("DELETE FROM lost_found_items")
    suspend fun deleteAllItems()

    @Query("SELECT * FROM lost_found_items WHERE type = :type ORDER BY timestamp DESC")
    suspend fun getItemsByType(type: String): List<LostFoundItem>
}