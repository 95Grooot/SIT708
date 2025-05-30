package com.example.task51c.repository

import androidx.lifecycle.LiveData
import com.example.task51c.database.VideoDao
import com.example.task51c.models.VideoItem

class VideoRepository(private val videoDao: VideoDao) {

    fun getAllVideos(): LiveData<List<VideoItem>> {
        return videoDao.getAllVideos()
    }

    suspend fun insertVideo(video: VideoItem) {
        videoDao.insertVideo(video)
    }

    suspend fun deleteVideo(video: VideoItem) {
        videoDao.deleteVideo(video)
    }

    suspend fun deleteVideoById(videoId: Long) {
        videoDao.deleteVideoById(videoId)
    }

    suspend fun getVideoById(videoId: Long): VideoItem? {
        return videoDao.getVideoById(videoId)
    }
}