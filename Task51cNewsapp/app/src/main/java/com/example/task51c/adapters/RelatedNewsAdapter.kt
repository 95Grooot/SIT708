package com.example.task51c.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.task51c.R
import com.example.task51c.databinding.ItemRelatedNewsBinding
import com.example.task51c.models.NewsItem

class RelatedNewsAdapter(
    private val onItemClick: (NewsItem) -> Unit
) : ListAdapter<NewsItem, RelatedNewsAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(
        private val binding: ItemRelatedNewsBinding,
        private val onItemClick: (NewsItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(newsItem: NewsItem) {
            binding.textViewTitle.text = newsItem.title
            binding.textViewDescription.text = newsItem.description

            Glide.with(binding.imageViewNews)
                .load(newsItem.imageUrl)
                .placeholder(R.drawable.placeholder_news)
                .error(R.drawable.placeholder_news)
                .into(binding.imageViewNews)

            binding.root.setOnClickListener {
                onItemClick(newsItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRelatedNewsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<NewsItem>() {
        override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
            return oldItem == newItem
        }
    }
}