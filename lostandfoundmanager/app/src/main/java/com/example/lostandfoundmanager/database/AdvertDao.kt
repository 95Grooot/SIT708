package com.example.lostandfoundmanager.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AdvertDao {
    @Query("SELECT * FROM adverts ORDER BY createdAt DESC")
    fun getAllAdverts(): LiveData<List<AdvertItem>>

    @Query("SELECT * FROM adverts WHERE id = :id")
    suspend fun getAdvertById(id: Long): AdvertItem?

    @Insert
    suspend fun insertAdvert(advert: AdvertItem): Long

    @Update
    suspend fun updateAdvert(advert: AdvertItem)

    @Delete
    suspend fun deleteAdvert(advert: AdvertItem)
}