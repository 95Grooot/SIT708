package com.example.sit305quizapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var buttonStart: Button
    private lateinit var textViewHighScore: TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("QuizPrefs", MODE_PRIVATE)

        initializeViews()
        displayHighScore()
        setupClickListeners()
    }

    private fun initializeViews() {
        editTextName = findViewById(R.id.editTextName)
        buttonStart = findViewById(R.id.buttonStart)
        textViewHighScore = findViewById(R.id.textViewHighScore)
    }

    private fun displayHighScore() {
        val highScore = sharedPreferences.getInt("HIGH_SCORE", 0)
        textViewHighScore.text = "High Score: $highScore/5"
    }

    private fun setupClickListeners() {
        buttonStart.setOnClickListener {
            val name = editTextName.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
            } else {
                startQuiz(name)
            }
        }
    }

    private fun startQuiz(name: String) {
        val intent = Intent(this, QuizActivity::class.java)
        intent.putExtra("USER_NAME", name)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        displayHighScore()
        // Clear name field when returning to welcome screen
        editTextName.text?.clear()
    }
}