package com.example.llamachatbot

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.llamachatbot.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupClickListeners()
    }

    private fun setupUI() {
        // Set status bar color to match the blue theme
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary_blue)

        // Set initial focus
        binding.usernameEditText.requestFocus()
    }

    private fun setupClickListeners() {
        binding.goButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString().trim()

            if (username.isEmpty()) {
                Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (username.length < 2) {
                Toast.makeText(this, "Username must be at least 2 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Navigate to chat activity
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("USERNAME", username)
            startActivity(intent)
        }
    }
}