package com.example.task51c.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.task51c.adapters.RelatedNewsAdapter
import com.example.task51c.databinding.FragmentNewsDetailBinding
import com.example.task51c.models.NewsItem

class NewsDetailFragment : Fragment() {

    private var _binding: FragmentNewsDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var relatedNewsAdapter: RelatedNewsAdapter
    private var newsItem: NewsItem? = null

    companion object {
        private const val ARG_NEWS_ITEM = "news_item"

        fun newInstance(newsItem: NewsItem): NewsDetailFragment {
            val fragment = NewsDetailFragment()
            val args = Bundle()
            args.putParcelable(ARG_NEWS_ITEM, newsItem)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            newsItem = it.getParcelable(ARG_NEWS_ITEM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupRelatedNewsRecyclerView()
        loadRelatedNews()
    }

    private fun setupUI() {
        newsItem?.let { item ->
            binding.textViewTitle.text = item.title
            binding.textViewDescription.text = item.description
            binding.textViewCategory.text = item.category

            // Load image using Glide
            Glide.with(this)
                .load(item.imageUrl)
                .placeholder(com.example.task51c.R.drawable.placeholder_news)
                .error(com.example.task51c.R.drawable.placeholder_news)
                .into(binding.imageViewNews)
        }
    }

    private fun setupRelatedNewsRecyclerView() {
        relatedNewsAdapter = RelatedNewsAdapter { relatedItem ->
            // Navigate to another news detail
            val detailFragment = newInstance(relatedItem)
            parentFragmentManager.beginTransaction()
                .replace(com.example.task51c.R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit()
        }

        binding.recyclerViewRelatedNews.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = relatedNewsAdapter
        }
    }

    private fun loadRelatedNews() {
        // Sample related news data
        val relatedNews = listOf(
            NewsItem(
                101,
                "Related Story: Environmental Impact",
                "Additional coverage on environmental policies and their global impact.",
                "https://example.com/related1.jpg",
                newsItem?.category ?: "General"
            ),
            NewsItem(
                102,
                "Follow-up: Expert Analysis",
                "Industry experts provide detailed analysis on the recent developments.",
                "https://example.com/related2.jpg",
                newsItem?.category ?: "General"
            ),
            NewsItem(
                103,
                "Background: Historical Context",
                "Understanding the historical background that led to current events.",
                "https://example.com/related3.jpg",
                newsItem?.category ?: "General"
            ),
            NewsItem(
                104,
                "Future Outlook: What's Next?",
                "Predictions and forecasts about future developments in this area.",
                "https://example.com/related4.jpg",
                newsItem?.category ?: "General"
            ),
            NewsItem(
                105,
                "Global Perspective: Worldwide View",
                "How these developments are being viewed and handled globally.",
                "https://example.com/related5.jpg",
                newsItem?.category ?: "General"
            )
        )

        relatedNewsAdapter.submitList(relatedNews)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}