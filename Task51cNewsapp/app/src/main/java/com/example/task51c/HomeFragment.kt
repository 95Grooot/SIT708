package com.example.task51c.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task51c.adapters.NewsAdapter
import com.example.task51c.adapters.TopStoriesAdapter
import com.example.task51c.databinding.FragmentHomeBinding
import com.example.task51c.models.NewsItem

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var topStoriesAdapter: TopStoriesAdapter
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTopStoriesRecyclerView()
        setupNewsRecyclerView()
        loadSampleData()
    }

    private fun setupTopStoriesRecyclerView() {
        topStoriesAdapter = TopStoriesAdapter { newsItem ->
            // Navigate to news detail fragment
            val detailFragment = NewsDetailFragment.newInstance(newsItem)
            parentFragmentManager.beginTransaction()
                .replace(com.example.task51c.R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit()
        }

        binding.recyclerViewTopStories.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = topStoriesAdapter
        }
    }

    private fun setupNewsRecyclerView() {
        newsAdapter = NewsAdapter { newsItem ->
            // Navigate to news detail fragment
            val detailFragment = NewsDetailFragment.newInstance(newsItem)
            parentFragmentManager.beginTransaction()
                .replace(com.example.task51c.R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit()
        }

        binding.recyclerViewNews.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = newsAdapter
        }
    }

    private fun loadSampleData() {
        val topStories = listOf(
            NewsItem(
                1,
                "Breaking: Major Climate Summit Begins",
                "World leaders gather to discuss climate action and environmental policies for the next decade.",
                "https://example.com/climate.jpg",
                "Climate"
            ),
            NewsItem(
                2,
                "Tech Innovation Awards 2024",
                "Annual technology awards recognize groundbreaking innovations in AI, robotics, and sustainable tech.",
                "https://example.com/tech.jpg",
                "Technology"
            ),
            NewsItem(
                3,
                "Global Economic Recovery Update",
                "Latest economic indicators show positive trends in global markets and employment rates.",
                "https://example.com/economy.jpg",
                "Economy"
            )
        )

        val newsItems = listOf(
            NewsItem(
                4,
                "Sports Championship Finals",
                "Exciting matches ahead as teams compete for the championship title this weekend.",
                "https://example.com/sports.jpg",
                "Sports"
            ),
            NewsItem(
                5,
                "Health & Wellness Trends",
                "New research reveals important insights about mental health and lifestyle choices.",
                "https://example.com/health.jpg",
                "Health"
            ),
            NewsItem(
                6,
                "Cultural Festival Highlights",
                "Annual cultural festival brings together diverse communities celebrating art and tradition.",
                "https://example.com/culture.jpg",
                "Culture"
            ),
            NewsItem(
                7,
                "Education System Reforms",
                "New educational policies aim to improve learning outcomes and accessibility.",
                "https://example.com/education.jpg",
                "Education"
            )
        )

        topStoriesAdapter.submitList(topStories)
        newsAdapter.submitList(newsItems)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}