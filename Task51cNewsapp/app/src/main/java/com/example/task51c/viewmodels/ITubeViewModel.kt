package com.example.task51c.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.task51c.database.AppDatabase
import com.example.task51c.models.VideoItem
import com.example.task51c.repository.VideoRepository

class ITubeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: VideoRepository
    val allVideos: LiveData<List<VideoItem>>

    init {
        val videoDao = AppDatabase.getDatabase(application).videoDao()
        repository = VideoRepository(videoDao)
        allVideos = repository.getAllVideos()
    }

    fun insertVideo(video: VideoItem) {
        viewModelScope.launch {
            repository.insertVideo(video)
        }
    }

    fun deleteVideo(video: VideoItem) {
        viewModelScope.launch {
            repository.deleteVideo(video)
        }
    }

    fun deleteVideoById(videoId: Long) {
        viewModelScope.launch {
            repository.deleteVideoById(videoId)
        }
    }

    suspend fun getVideoById(videoId: Long): VideoItem? {
        return repository.getVideoById(videoId)
    }
}