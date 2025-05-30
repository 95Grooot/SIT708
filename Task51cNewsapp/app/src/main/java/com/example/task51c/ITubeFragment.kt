package com.example.task51c.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task51c.adapters.PlaylistAdapter
import com.example.task51c.databinding.FragmentItubeBinding
import com.example.task51c.models.VideoItem
import com.example.task51c.viewmodel.ITubeViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

class ITubeFragment : Fragment() {

    private var _binding: FragmentItubeBinding? = null
    private val binding get() = _binding!!

    private lateinit var playlistAdapter: PlaylistAdapter
    private lateinit var viewModel: ITubeViewModel
    private var youTubePlayer: YouTubePlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItubeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[ITubeViewModel::class.java]

        setupYouTubePlayer()
        setupPlaylistRecyclerView()
        setupClickListeners()
        observePlaylist()
    }

    private fun setupYouTubePlayer() {
        lifecycle.addObserver(binding.youtubePlayerView)

        binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(player: YouTubePlayer) {
                youTubePlayer = player
            }
        })
    }

    private fun setupPlaylistRecyclerView() {
        playlistAdapter = PlaylistAdapter(
            onPlayClick = { videoItem ->
                playVideo(videoItem.videoId)
            },
            onDeleteClick = { videoItem ->
                viewModel.deleteVideo(videoItem)
                Toast.makeText(context, "Removed from playlist", Toast.LENGTH_SHORT).show()
            }
        )

        binding.recyclerViewPlaylist.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = playlistAdapter
        }
    }

    private fun observePlaylist() {
        viewModel.allVideos.observe(viewLifecycleOwner) { videos ->
            playlistAdapter.submitList(videos)
        }
    }

    private fun setupClickListeners() {
        binding.buttonPlay.setOnClickListener {
            val url = binding.editTextUrl.text.toString().trim()
            if (url.isNotEmpty()) {
                val videoId = extractVideoId(url)
                if (videoId != null) {
                    playVideo(videoId)
                } else {
                    Toast.makeText(context, "Invalid YouTube URL", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Please enter a YouTube URL", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonAddToPlaylist.setOnClickListener {
            val url = binding.editTextUrl.text.toString().trim()
            if (url.isNotEmpty()) {
                val videoId = extractVideoId(url)
                if (videoId != null) {
                    val videoItem = VideoItem(
                        title = "Video: $videoId",
                        videoId = videoId,
                        url = url
                    )
                    viewModel.insertVideo(videoItem)
                    binding.editTextUrl.text?.clear()
                    Toast.makeText(context, "Added to playlist", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Invalid YouTube URL", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Please enter a YouTube URL", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonMyPlaylist.setOnClickListener {
            if (binding.recyclerViewPlaylist.visibility == View.VISIBLE) {
                binding.recyclerViewPlaylist.visibility = View.GONE
                binding.buttonMyPlaylist.text = "Show My Playlist"
            } else {
                binding.recyclerViewPlaylist.visibility = View.VISIBLE
                binding.buttonMyPlaylist.text = "Hide My Playlist"
            }
        }
    }

    private fun playVideo(videoId: String) {
        youTubePlayer?.loadVideo(videoId, 0f)
    }

    private fun extractVideoId(url: String): String? {
        return when {
            url.contains("youtube.com/watch?v=") -> {
                val start = url.indexOf("v=") + 2
                val end = url.indexOf("&", start).takeIf { it != -1 } ?: url.length
                url.substring(start, end)
            }
            url.contains("youtu.be/") -> {
                val start = url.lastIndexOf("/") + 1
                val end = url.indexOf("?", start).takeIf { it != -1 } ?: url.length
                url.substring(start, end)
            }
            else -> null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}