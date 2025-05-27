package com.example.lostandfoundmanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lostandfoundmanager.adapters.ItemsAdapter
import com.example.lostandfoundmanager.database.DatabaseHelper
import com.example.lostandfoundmanager.databinding.ActivityAllItemsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllItemsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAllItemsBinding
    private lateinit var database: DatabaseHelper
    private lateinit var adapter: ItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadItems()

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "All Items"
        }
    }

    private fun setupRecyclerView() {
        database = DatabaseHelper.getDatabase(this)
        adapter = ItemsAdapter()

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@AllItemsActivity)
            adapter = this@AllItemsActivity.adapter
        }
    }

    private fun loadItems() {
        lifecycleScope.launch {
            val items = withContext(Dispatchers.IO) {
                database.itemsDao().getAllItemsList()
            }
            adapter.submitList(items)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}