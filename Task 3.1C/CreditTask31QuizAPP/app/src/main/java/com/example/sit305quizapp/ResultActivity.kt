package com.example.sit305quizapp

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {

    private lateinit var textViewCongratulations: TextView
    private lateinit var textViewScore: TextView
    private lateinit var textViewPercentage: TextView
    private lateinit var textViewNewHighScore: TextView
    private lateinit var buttonTakeNewQuiz: Button
    private lateinit var buttonFinish: Button

    private lateinit var userName: String
    private var score: Int = 0
    private var totalQuestions: Int = 5
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // Get data from intent
        userName = intent.getStringExtra("USER_NAME") ?: "User"
        score = intent.getIntExtra("SCORE", 0)
        totalQuestions = intent.getIntExtra("TOTAL_QUESTIONS", 5)

        sharedPreferences = getSharedPreferences("QuizPrefs", MODE_PRIVATE)

        initializeViews()
        displayResults()
        checkHighScore()
        setupClickListeners()
        styleButtons() // Add proper button styling
    }

    private fun initializeViews() {
        textViewCongratulations = findViewById(R.id.textViewCongratulations)
        textViewScore = findViewById(R.id.textViewScore)
        textViewPercentage = findViewById(R.id.textViewPercentage)
        textViewNewHighScore = findViewById(R.id.textViewNewHighScore)
        buttonTakeNewQuiz = findViewById(R.id.buttonTakeNewQuiz)
        buttonFinish = findViewById(R.id.buttonFinish)
    }

    private fun displayResults() {
        textViewCongratulations.text = "Congratulations $userName!"
        textViewScore.text = "Your Score: $score/$totalQuestions"

        val percentage = if (totalQuestions > 0) {
            (score * 100) / totalQuestions
        } else {
            0
        }
        textViewPercentage.text = "Percentage: $percentage%"
    }

    private fun checkHighScore() {
        val currentHighScore = sharedPreferences.getInt("HIGH_SCORE", 0)

        if (score > currentHighScore) {
            // Save new high score
            with(sharedPreferences.edit()) {
                putInt("HIGH_SCORE", score)
                putString("HIGH_SCORE_NAME", userName)
                apply()
            }

            // Show congratulations for new high score
            textViewNewHighScore.text = "🎉 NEW HIGH SCORE! 🎉"
            textViewNewHighScore.visibility = TextView.VISIBLE
        } else {
            textViewNewHighScore.visibility = TextView.GONE
        }
    }

    private fun setupClickListeners() {
        buttonTakeNewQuiz.setOnClickListener {
            takeNewQuiz()
        }

        buttonFinish.setOnClickListener {
            finishQuiz()
        }
    }

    private fun styleButtons() {
        // Style "Take New Quiz" button - Primary blue
        createButtonBackground(buttonTakeNewQuiz, "#2196F3", "#FFFFFF", "#1565C0")

        // Style "Finish" button - Secondary with dark text for visibility
        createButtonBackground(buttonFinish, "#E3F2FD", "#1976D2", "#2196F3")
    }

    private fun createButtonBackground(button: Button, backgroundColor: String, textColor: String, strokeColor: String) {
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.RECTANGLE
        drawable.setColor(Color.parseColor(backgroundColor))
        drawable.cornerRadius = 24f
        drawable.setStroke(4, Color.parseColor(strokeColor))

        button.background = drawable
        button.setTextColor(Color.parseColor(textColor))
        button.setPadding(32, 32, 32, 32)

        // Ensure text is visible and properly sized
        button.textSize = 16f
        button.isAllCaps = false
    }

    private fun takeNewQuiz() {
        // Return to MainActivity to start a new quiz
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun finishQuiz() {
        // Close the app or return to main screen
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}