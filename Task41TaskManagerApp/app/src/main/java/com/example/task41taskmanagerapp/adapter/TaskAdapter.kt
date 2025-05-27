package com.example.task41taskmanagerapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.task41taskmanagerapp.database.Task
import com.example.task41taskmanagerapp.databinding.ItemTaskBinding
import com.example.task41taskmanagerapp.utils.DateUtils

class TaskAdapter(
    private val onTaskClick: (Task) -> Unit,
    private val onTaskLongClick: (Task) -> Unit,
    private val onTaskStatusChange: (Task, Boolean) -> Unit
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.titleText.text = task.title
            binding.descriptionText.text = task.description
            binding.dueDateText.text = "Due: ${DateUtils.formatDate(task.dueDate)}"
            binding.statusCheckBox.isChecked = task.isCompleted

            binding.root.setOnClickListener { onTaskClick(task) }
            binding.root.setOnLongClickListener {
                onTaskLongClick(task)
                true
            }
            binding.statusCheckBox.setOnCheckedChangeListener { _, isChecked ->
                onTaskStatusChange(task, isChecked)
            }
        }
    }

    class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
}