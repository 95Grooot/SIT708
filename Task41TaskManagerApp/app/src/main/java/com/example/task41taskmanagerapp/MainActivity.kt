package com.example.task41taskmanagerapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task41taskmanagerapp.adapter.TaskAdapter
import com.example.task41taskmanagerapp.database.Task
import com.example.task41taskmanagerapp.database.TaskDatabase
import com.example.task41taskmanagerapp.database.TaskRepository
import com.example.task41taskmanagerapp.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var repository: TaskRepository
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupDatabase()
        setupRecyclerView()
        setupFab()
        observeTasks()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun setupDatabase() {
        val database = TaskDatabase.getDatabase(this)
        repository = TaskRepository(database.taskDao())
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(
            onTaskClick = { task ->
                val intent = Intent(this, TaskDetailActivity::class.java)
                intent.putExtra("TASK_ID", task.id)
                startActivity(intent)
            },
            onTaskLongClick = { task ->
                showDeleteConfirmationDialog(task)
            },
            onTaskStatusChange = { task, isCompleted ->
                updateTaskStatus(task, isCompleted)
            }
        )

        binding.recyclerView.apply {
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun setupFab() {
        binding.fabAddTask.setOnClickListener {
            startActivity(Intent(this, AddEditTaskActivity::class.java))
        }
    }

    private fun observeTasks() {
        repository.getAllTasks().observe(this) { tasks ->
            if (tasks.isEmpty()) {
                binding.emptyStateLayout.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.emptyStateLayout.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
            taskAdapter.submitList(tasks)
        }
    }

    private fun updateTaskStatus(task: Task, isCompleted: Boolean) {
        lifecycleScope.launch {
            val updatedTask = task.copy(
                isCompleted = isCompleted,
                updatedAt = Date()
            )
            repository.updateTask(updatedTask)

            val message = if (isCompleted)
                getString(R.string.task_completed)
            else
                getString(R.string.task_pending)
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDeleteConfirmationDialog(task: Task) {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_task_title)
            .setMessage("Are you sure you want to delete '${task.title}'?")
            .setPositiveButton(R.string.delete) { _, _ ->
                deleteTask(task)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun deleteTask(task: Task) {
        lifecycleScope.launch {
            repository.deleteTask(task)
            Toast.makeText(this@MainActivity, R.string.task_deleted, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_show_all -> {
                repository.getAllTasks().observe(this) { tasks ->
                    taskAdapter.submitList(tasks)
                }
                true
            }
            R.id.action_show_incomplete -> {
                repository.getIncompleteTasks().observe(this) { tasks ->
                    taskAdapter.submitList(tasks)
                }
                true
            }
            R.id.action_show_completed -> {
                repository.getCompletedTasks().observe(this) { tasks ->
                    taskAdapter.submitList(tasks)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}