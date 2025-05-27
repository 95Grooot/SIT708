package com.example.lostandfoundmanager

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.lostandfoundmanager.database.AdvertDatabase
import com.example.lostandfoundmanager.database.AdvertItem
import com.example.lostandfoundmanager.database.AdvertRepository
import com.example.lostandfoundmanager.databinding.ActivityItemDetailBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ItemDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemDetailBinding
    private lateinit var repository: AdvertRepository
    private var currentAdvert: AdvertItem? = null
    private var advertId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDatabase()
        loadAdvert()
        setupButtons()
    }

    private fun setupDatabase() {
        val database = AdvertDatabase.getDatabase(this)
        repository = AdvertRepository(database.advertDao())
    }

    private fun loadAdvert() {
        advertId = intent.getLongExtra("ADVERT_ID", -1)
        if (advertId == -1L) {
            finish()
            return
        }

        lifecycleScope.launch {
            currentAdvert = repository.getAdvertById(advertId)
            currentAdvert?.let { advert ->
                displayAdvert(advert)
            } ?: run {
                Toast.makeText(this@ItemDetailActivity, "Item not found", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun displayAdvert(advert: AdvertItem) {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        // Create item title based on type and description
        val itemTitle = "${advert.type}: ${advert.description}"

        binding.tvItemTitle.text = itemTitle
        binding.tvDate.text = "Date: ${dateFormat.format(advert.date)}"
        binding.tvLocation.text = "Location: ${advert.location}"
        binding.tvContactInfo.text = "Contact: ${advert.name} - ${advert.phone}"
    }

    private fun setupButtons() {
        binding.btnRemove.setOnClickListener {
            showRemoveConfirmationDialog()
        }
    }

    private fun showRemoveConfirmationDialog() {
        currentAdvert?.let { advert ->
            AlertDialog.Builder(this)
                .setTitle("Remove Item")
                .setMessage("Are you sure you want to remove this item?")
                .setPositiveButton("Remove") { _, _ ->
                    removeAdvert(advert)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun removeAdvert(advert: AdvertItem) {
        lifecycleScope.launch {
            repository.deleteAdvert(advert)
            Toast.makeText(this@ItemDetailActivity, "Item removed successfully", Toast.LENGTH_SHORT).show()
            finish() // Return to previous screen
        }
    }
}