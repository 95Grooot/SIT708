package com.example.lostandfoundmanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lostandfoundmanager.R
import com.example.lostandfoundmanager.database.AdvertItem
import com.example.lostandfoundmanager.databinding.ItemAdvertBinding
import java.text.SimpleDateFormat
import java.util.*

class AdvertAdapter(
    private val onItemClick: (AdvertItem) -> Unit
) : ListAdapter<AdvertItem, AdvertAdapter.AdvertViewHolder>(AdvertDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdvertViewHolder {
        val binding = ItemAdvertBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdvertViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdvertViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AdvertViewHolder(private val binding: ItemAdvertBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(advert: AdvertItem) {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            binding.tvType.text = advert.type
            binding.tvDescription.text = advert.description
            binding.tvLocation.text = advert.location
            binding.tvDate.text = dateFormat.format(advert.date)
            binding.tvContactName.text = advert.name

            // Set different colors for Lost and Found
            val backgroundColor = if (advert.type == "Lost") {
                ContextCompat.getColor(binding.root.context, R.color.lost_color)
            } else {
                ContextCompat.getColor(binding.root.context, R.color.found_color)
            }
            binding.tvType.setBackgroundColor(backgroundColor)

            binding.root.setOnClickListener {
                onItemClick(advert)
            }
        }
    }

    class AdvertDiffCallback : DiffUtil.ItemCallback<AdvertItem>() {
        override fun areItemsTheSame(oldItem: AdvertItem, newItem: AdvertItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AdvertItem, newItem: AdvertItem): Boolean {
            return oldItem == newItem
        }
    }
}