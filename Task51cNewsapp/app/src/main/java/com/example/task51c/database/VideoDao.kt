package com.example.task51c.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.task51c.models.VideoItem

@Dao
interface VideoDao {
    @Query("SELECT * FROM videos ORDER BY id DESC")
    fun getAllVideos(): LiveData<List<VideoItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: VideoItem)

    @Delete
    suspend fun deleteVideo(video: VideoItem)

    @Query("DELETE FROM videos WHERE id = :videoId")
    suspend fun deleteVideoById(videoId: Long)

    @Query("SELECT * FROM videos WHERE id = :videoId")
    suspend fun getVideoById(videoId: Long): VideoItem?
}