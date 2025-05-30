package com.example.task51c.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.task51c.R
import com.example.task51c.models.VideoItem

class PlaylistAdapter(
    private val onPlayClick: (VideoItem) -> Unit,
    private val onDeleteClick: (VideoItem) -> Unit
) : ListAdapter<VideoItem, PlaylistAdapter.PlaylistViewHolder>(PlaylistDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_playlist, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.text_view_title)
        private val thumbnailImageView: ImageView = itemView.findViewById(R.id.image_view_thumbnail)
        private val playButton: Button = itemView.findViewById(R.id.button_play)
        private val deleteButton: Button = itemView.findViewById(R.id.button_delete)

        fun bind(videoItem: VideoItem) {
            titleTextView.text = videoItem.title

            // Load YouTube thumbnail
            val thumbnailUrl = "https://img.youtube.com/vi/${videoItem.videoId}/maxresdefault.jpg"
            // You can use Glide here if needed

            playButton.setOnClickListener {
                onPlayClick(videoItem)
            }

            deleteButton.setOnClickListener {
                onDeleteClick(videoItem)
            }
        }
    }

    private class PlaylistDiffCallback : DiffUtil.ItemCallback<VideoItem>() {
        override fun areItemsTheSame(oldItem: VideoItem, newItem: VideoItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: VideoItem, newItem: VideoItem): Boolean {
            return oldItem == newItem
        }
    }
}