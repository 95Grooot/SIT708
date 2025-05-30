package com.example.sit305quizapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

class QuizActivity : AppCompatActivity() {

    private lateinit var textViewProgress: TextView
    private lateinit var textViewWelcome: TextView
    private lateinit var textViewQuestionTitle: TextView
    private lateinit var textViewQuestionDetails: TextView
    private lateinit var textViewTimer: TextView
    private lateinit var buttonOption1: Button
    private lateinit var buttonOption2: Button
    private lateinit var buttonOption3: Button
    private lateinit var buttonSubmit: Button
    private lateinit var buttonNext: Button
    private lateinit var progressBar: ProgressBar

    private lateinit var quizViewModel: QuizViewModel
    private lateinit var userName: String
    private var selectedAnswer = -1
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        userName = intent.getStringExtra("USER_NAME") ?: "User"

        initializeViews()
        initializeViewModel()
        displayCurrentQuestion()
        setupClickListeners()
    }

    private fun initializeViews() {
        textViewProgress = findViewById(R.id.textViewProgress)
        textViewWelcome = findViewById(R.id.textViewWelcome)
        textViewQuestionTitle = findViewById(R.id.textViewQuestionTitle)
        textViewQuestionDetails = findViewById(R.id.textViewQuestionDetails)
        textViewTimer = findViewById(R.id.textViewTimer)
        buttonOption1 = findViewById(R.id.buttonOption1)
        buttonOption2 = findViewById(R.id.buttonOption2)
        buttonOption3 = findViewById(R.id.buttonOption3)
        buttonSubmit = findViewById(R.id.buttonSubmit)
        buttonNext = findViewById(R.id.buttonNext)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun initializeViewModel() {
        quizViewModel = ViewModelProvider(this)[QuizViewModel::class.java]
    }

    private fun displayCurrentQuestion() {
        val currentQuestion = quizViewModel.getCurrentQuestion()
        val questionNumber = quizViewModel.getCurrentQuestionIndex() + 1

        // Update progress
        textViewProgress.text = "$questionNumber/5"
        progressBar.progress = questionNumber

        // Welcome message
        textViewWelcome.text = "Welcome, $userName!"

        // Question content
        textViewQuestionTitle.text = "Question $questionNumber"
        textViewQuestionDetails.text = currentQuestion.question

        // Answer options
        buttonOption1.text = currentQuestion.options[0]
        buttonOption2.text = currentQuestion.options[1]
        buttonOption3.text = currentQuestion.options[2]

        // Reset UI state
        resetButtonColors()
        enableAnswerSelection()
        buttonSubmit.isEnabled = false
        buttonNext.visibility = Button.GONE
        selectedAnswer = -1

        // Start timer
        startTimer()
    }

    private fun setupClickListeners() {
        buttonOption1.setOnClickListener { selectAnswer(0) }
        buttonOption2.setOnClickListener { selectAnswer(1) }
        buttonOption3.setOnClickListener { selectAnswer(2) }

        buttonSubmit.setOnClickListener {
            submitAnswer()
        }

        buttonNext.setOnClickListener {
            moveToNextQuestion()
        }
    }

    private fun selectAnswer(answerIndex: Int) {
        if (selectedAnswer != -1) return // Prevent changing answer after selection

        selectedAnswer = answerIndex

        // Reset all buttons first
        resetButtonColors()

        // Highlight selected answer with blue
        when (answerIndex) {
            0 -> {
                buttonOption1.setBackgroundColor(Color.parseColor("#2196F3"))
                buttonOption1.setTextColor(Color.WHITE)
            }
            1 -> {
                buttonOption2.setBackgroundColor(Color.parseColor("#2196F3"))
                buttonOption2.setTextColor(Color.WHITE)
            }
            2 -> {
                buttonOption3.setBackgroundColor(Color.parseColor("#2196F3"))
                buttonOption3.setTextColor(Color.WHITE)
            }
        }

        buttonSubmit.isEnabled = true
    }

    private fun submitAnswer() {
        countDownTimer?.cancel()

        val currentQuestion = quizViewModel.getCurrentQuestion()
        val correctAnswer = currentQuestion.correctAnswerIndex

        // Lock answer selection
        disableAnswerSelection()
        buttonSubmit.isEnabled = false

        // Record answer
        quizViewModel.recordAnswer(selectedAnswer == correctAnswer)

        // Show correct/incorrect colors - SIMPLIFIED APPROACH
        showAnswerColors(correctAnswer)

        // Show next button or finish quiz
        if (quizViewModel.hasNextQuestion()) {
            buttonNext.visibility = Button.VISIBLE
            buttonNext.text = "Next"
            // Make Next button blue with white text - clearly visible
            buttonNext.setBackgroundColor(Color.parseColor("#2196F3"))
            buttonNext.setTextColor(Color.WHITE)
        } else {
            buttonNext.visibility = Button.VISIBLE
            buttonNext.text = "View Results"
            // Make View Results button green with white text - clearly visible
            buttonNext.setBackgroundColor(Color.parseColor("#4CAF50"))
            buttonNext.setTextColor(Color.WHITE)
        }
    }

    private fun showAnswerColors(correctAnswer: Int) {
        // ALWAYS show correct answer in GREEN
        when (correctAnswer) {
            0 -> {
                buttonOption1.setBackgroundColor(Color.parseColor("#4CAF50"))
                buttonOption1.setTextColor(Color.WHITE)
            }
            1 -> {
                buttonOption2.setBackgroundColor(Color.parseColor("#4CAF50"))
                buttonOption2.setTextColor(Color.WHITE)
            }
            2 -> {
                buttonOption3.setBackgroundColor(Color.parseColor("#4CAF50"))
                buttonOption3.setTextColor(Color.WHITE)
            }
        }

        // If user selected wrong answer, show it in RED
        if (selectedAnswer != correctAnswer && selectedAnswer != -1) {
            when (selectedAnswer) {
                0 -> {
                    buttonOption1.setBackgroundColor(Color.parseColor("#F44336"))
                    buttonOption1.setTextColor(Color.WHITE)
                }
                1 -> {
                    buttonOption2.setBackgroundColor(Color.parseColor("#F44336"))
                    buttonOption2.setTextColor(Color.WHITE)
                }
                2 -> {
                    buttonOption3.setBackgroundColor(Color.parseColor("#F44336"))
                    buttonOption3.setTextColor(Color.WHITE)
                }
            }
        }
    }

    private fun moveToNextQuestion() {
        if (quizViewModel.hasNextQuestion()) {
            quizViewModel.moveToNextQuestion()
            displayCurrentQuestion()
        } else {
            showResults()
        }
    }

    private fun showResults() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("USER_NAME", userName)
        intent.putExtra("SCORE", quizViewModel.getScore())
        intent.putExtra("TOTAL_QUESTIONS", 5)
        startActivity(intent)
        finish()
    }

    private fun resetButtonColors() {
        val defaultColor = Color.parseColor("#FFFFFF")
        val defaultTextColor = Color.parseColor("#212121")

        buttonOption1.setBackgroundColor(defaultColor)
        buttonOption1.setTextColor(defaultTextColor)

        buttonOption2.setBackgroundColor(defaultColor)
        buttonOption2.setTextColor(defaultTextColor)

        buttonOption3.setBackgroundColor(defaultColor)
        buttonOption3.setTextColor(defaultTextColor)
    }

    private fun enableAnswerSelection() {
        buttonOption1.isEnabled = true
        buttonOption2.isEnabled = true
        buttonOption3.isEnabled = true
    }

    private fun disableAnswerSelection() {
        buttonOption1.isEnabled = false
        buttonOption2.isEnabled = false
        buttonOption3.isEnabled = false
    }

    private fun startTimer() {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                textViewTimer.text = "Time: ${seconds}s"

                // Change timer color when running out of time
                if (seconds <= 10) {
                    textViewTimer.setTextColor(Color.parseColor("#F44336"))
                } else {
                    textViewTimer.setTextColor(Color.parseColor("#FF4081"))
                }
            }

            override fun onFinish() {
                textViewTimer.text = "Time's up!"
                textViewTimer.setTextColor(Color.parseColor("#F44336"))
                if (selectedAnswer == -1) {
                    // Auto-submit with no answer
                    submitAnswer()
                }
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}