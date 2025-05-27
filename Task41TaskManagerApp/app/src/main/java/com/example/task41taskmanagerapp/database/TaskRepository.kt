package com.example.task41taskmanagerapp.database

import androidx.lifecycle.LiveData

class TaskRepository(private val taskDao: TaskDao) {

    fun getAllTasks(): LiveData<List<Task>> = taskDao.getAllTasks()

    fun getIncompleteTasks(): LiveData<List<Task>> = taskDao.getIncompleteTasks()

    fun getCompletedTasks(): LiveData<List<Task>> = taskDao.getCompletedTasks()

    suspend fun getTaskById(id: Long): Task? = taskDao.getTaskById(id)

    suspend fun insertTask(task: Task): Long = taskDao.insertTask(task)

    suspend fun updateTask(task: Task) = taskDao.updateTask(task)

    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)

    suspend fun deleteTaskById(id: Long) = taskDao.deleteTaskById(id)

    suspend fun getTaskCount(): Int = taskDao.getTaskCount()
}