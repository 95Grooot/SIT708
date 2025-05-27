package com.example.lostandfoundmanager.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lostandfoundmanager.R
import com.example.lostandfoundmanager.databinding.ItemLostFoundBinding
import com.example.lostandfoundmanager.models.LostFoundItem

class ItemsAdapter : ListAdapter<LostFoundItem, ItemsAdapter.ItemViewHolder>(ItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemLostFoundBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ItemViewHolder(private val binding: ItemLostFoundBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LostFoundItem) {
            binding.apply {
                tvItemName.text = item.name
                tvItemType.text = item.type
                tvItemDescription.text = item.description
                tvItemDate.text = item.date
                tvItemLocation.text = item.location
                tvItemPhone.text = item.phone

                // Set type indicator color
                val color = if (item.type == "Lost") {
                    ContextCompat.getColor(root.context, R.color.lost_color)
                } else {
                    ContextCompat.getColor(root.context, R.color.found_color)
                }
                typeIndicator.setBackgroundColor(color)
            }
        }
    }

    class ItemDiffCallback : DiffUtil.ItemCallback<LostFoundItem>() {
        override fun areItemsTheSame(oldItem: LostFoundItem, newItem: LostFoundItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LostFoundItem, newItem: LostFoundItem): Boolean {
            return oldItem == newItem
        }
    }
}