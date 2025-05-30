package com.example.task51c.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.task51c.R
import com.example.task51c.models.NewsItem

class NewsAdapter(
    private val onItemClick: (NewsItem) -> Unit
) : ListAdapter<NewsItem, NewsAdapter.NewsViewHolder>(NewsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.text_view_title)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.text_view_description)
        private val imageView: ImageView = itemView.findViewById(R.id.image_view_news)
        private val categoryTextView: TextView = itemView.findViewById(R.id.text_view_category)

        fun bind(newsItem: NewsItem) {
            titleTextView.text = newsItem.title
            descriptionTextView.text = newsItem.description
            categoryTextView.text = newsItem.category

            Glide.with(itemView.context)
                .load(newsItem.imageUrl)
                .placeholder(R.color.light_gray)
                .error(R.color.light_gray)
                .into(imageView)

            itemView.setOnClickListener {
                onItemClick(newsItem)
            }
        }
    }

    private class NewsDiffCallback : DiffUtil.ItemCallback<NewsItem>() {
        override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
            return oldItem == newItem
        }
    }
}