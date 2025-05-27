package com.example.task41taskmanagerapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.task41taskmanagerapp.database.Task
import com.example.task41taskmanagerapp.database.TaskDatabase
import com.example.task41taskmanagerapp.database.TaskRepository
import com.example.task41taskmanagerapp.databinding.ActivityTaskDetailBinding
import com.example.task41taskmanagerapp.utils.DateUtils
import kotlinx.coroutines.launch

class TaskDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskDetailBinding
    private lateinit var repository: TaskRepository

    private var currentTask: Task? = null
    private var taskId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        setupDatabase()
        loadTask()
    }

    private fun setupDatabase() {
        val database = TaskDatabase.getDatabase(this)
        repository = TaskRepository(database.taskDao())
    }

    private fun loadTask() {
        taskId = intent.getLongExtra("TASK_ID", -1)
        if (taskId == -1L) {
            finish()
            return
        }

        lifecycleScope.launch {
            currentTask = repository.getTaskById(taskId)
            currentTask?.let { task ->
                displayTask(task)
            } ?: run {
                finish()
            }
        }
    }

    private fun displayTask(task: Task) {
        binding.titleText.text = task.title
        binding.descriptionText.text = task.description
        binding.dueDateText.text = "Due: ${DateUtils.formatDate(task.dueDate)}"
        binding.statusText.text = "Status: ${if (task.isCompleted) getString(R.string.completed) else getString(R.string.pending)}"
        binding.createdAtText.text = "Created: ${DateUtils.formatDateTime(task.createdAt)}"
        binding.updatedAtText.text = "Updated: ${DateUtils.formatDateTime(task.updatedAt)}"

        title = task.title
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.task_detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit -> {
                editTask()
                true
            }
            R.id.action_delete -> {
                showDeleteConfirmationDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun editTask() {
        val intent = Intent(this, AddEditTaskActivity::class.java)
        intent.putExtra("TASK_ID", taskId)
        startActivity(intent)
    }

    private fun showDeleteConfirmationDialog() {
        currentTask?.let { task ->
            AlertDialog.Builder(this)
                .setTitle(R.string.delete_task_title)
                .setMessage("Are you sure you want to delete '${task.title}'?")
                .setPositiveButton(R.string.delete) { _, _ ->
                    deleteTask(task)
                }
                .setNegativeButton(R.string.cancel, null)
                .show()
        }
    }

    private fun deleteTask(task: Task) {
        lifecycleScope.launch {
            repository.deleteTask(task)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        loadTask() // Reload in case task was edited
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Back arrow
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}