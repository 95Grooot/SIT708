package com.example.lostandfoundmanager

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lostandfoundmanager.adapter.AdvertAdapter
import com.example.lostandfoundmanager.database.AdvertDatabase
import com.example.lostandfoundmanager.database.AdvertRepository
import com.example.lostandfoundmanager.databinding.ActivityAllItemsBinding

class AllItemsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAllItemsBinding
    private lateinit var repository: AdvertRepository
    private lateinit var adapter: AdvertAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDatabase()
        setupRecyclerView()
        observeAdverts()
    }

    private fun setupDatabase() {
        val database = AdvertDatabase.getDatabase(this)
        repository = AdvertRepository(database.advertDao())
    }

    private fun setupRecyclerView() {
        adapter = AdvertAdapter { advert ->
            // Navigate to Item Detail Screen when item is tapped
            val intent = Intent(this, ItemDetailActivity::class.java)
            intent.putExtra("ADVERT_ID", advert.id)
            startActivity(intent)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@AllItemsActivity)
            adapter = this@AllItemsActivity.adapter
        }
    }

    private fun observeAdverts() {
        repository.getAllAdverts().observe(this) { adverts ->
            adapter.submitList(adverts)
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh the list when returning from other screens
        observeAdverts()
    }
}