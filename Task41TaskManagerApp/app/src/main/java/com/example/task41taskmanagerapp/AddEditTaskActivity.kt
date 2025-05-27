package com.example.task41taskmanagerapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.task41taskmanagerapp.database.Task
import com.example.task41taskmanagerapp.database.TaskDatabase
import com.example.task41taskmanagerapp.database.TaskRepository
import com.example.task41taskmanagerapp.databinding.ActivityAddEditTaskBinding
import com.example.task41taskmanagerapp.utils.DateUtils
import kotlinx.coroutines.launch
import java.util.*

class AddEditTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditTaskBinding
    private lateinit var repository: TaskRepository

    private var selectedDate: Date = Date()
    private var taskId: Long = -1
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDatabase()
        setupClickListeners()
        checkEditMode()
        updateDateButton()
    }

    private fun setupDatabase() {
        val database = TaskDatabase.getDatabase(this)
        repository = TaskRepository(database.taskDao())
    }

    private fun setupClickListeners() {
        binding.dueDateButton.setOnClickListener {
            showDatePicker()
        }

        binding.saveButton.setOnClickListener {
            saveTask()
        }

        binding.cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun checkEditMode() {
        taskId = intent.getLongExtra("TASK_ID", -1)
        if (taskId != -1L) {
            isEditMode = true
            title = getString(R.string.edit_task)
            binding.saveButton.text = getString(R.string.save_task)
            loadTask()
        } else {
            title = getString(R.string.add_task)
            binding.saveButton.text = getString(R.string.add_task)
        }
    }

    private fun loadTask() {
        lifecycleScope.launch {
            val task = repository.getTaskById(taskId)
            task?.let {
                binding.titleEditText.setText(it.title)
                binding.descriptionEditText.setText(it.description)
                selectedDate = it.dueDate
                updateDateButton()
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        calendar.time = selectedDate

        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                selectedDate = calendar.time
                updateDateButton()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateDateButton() {
        binding.dueDateButton.text = DateUtils.formatDate(selectedDate)
    }

    private fun saveTask() {
        if (!validateInput()) {
            return
        }

        val title = binding.titleEditText.text.toString().trim()
        val description = binding.descriptionEditText.text.toString().trim()

        lifecycleScope.launch {
            if (isEditMode) {
                val existingTask = repository.getTaskById(taskId)
                existingTask?.let { task ->
                    val updatedTask = task.copy(
                        title = title,
                        description = description,
                        dueDate = selectedDate,
                        updatedAt = Date()
                    )
                    repository.updateTask(updatedTask)
                    Toast.makeText(this@AddEditTaskActivity, R.string.task_updated, Toast.LENGTH_SHORT).show()
                }
            } else {
                val newTask = Task(
                    title = title,
                    description = description,
                    dueDate = selectedDate
                )
                repository.insertTask(newTask)
                Toast.makeText(this@AddEditTaskActivity, R.string.task_created, Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }

    private fun validateInput(): Boolean {
        val title = binding.titleEditText.text.toString().trim()
        val description = binding.descriptionEditText.text.toString().trim()

        when {
            title.isEmpty() -> {
                binding.titleEditText.error = getString(R.string.title_required)
                binding.titleEditText.requestFocus()
                return false
            }
            description.isEmpty() -> {
                binding.descriptionEditText.error = getString(R.string.description_required)
                binding.descriptionEditText.requestFocus()
                return false
            }
            selectedDate.before(Date()) && !DateUtils.isToday(selectedDate) -> {
                Toast.makeText(this, R.string.past_date_error, Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }
}