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

class TopStoriesAdapter(
    private val onItemClick: (NewsItem) -> Unit
) : ListAdapter<NewsItem, TopStoriesAdapter.TopStoryViewHolder>(TopStoriesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopStoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_top_story, parent, false)
        return TopStoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopStoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TopStoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.text_view_title)
        private val imageView: ImageView = itemView.findViewById(R.id.image_view_news)
        private val categoryTextView: TextView = itemView.findViewById(R.id.text_view_category)

        fun bind(newsItem: NewsItem) {
            titleTextView.text = newsItem.title
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

    private class TopStoriesDiffCallback : DiffUtil.ItemCallback<NewsItem>() {
        override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
            return oldItem == newItem
        }
    }
}