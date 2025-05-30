package com.example.sit305quizapp

import androidx.lifecycle.ViewModel

data class Question(
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)

class QuizViewModel : ViewModel() {

    private val questions = listOf(
        Question(
            "What is the capital of Australia?",
            listOf("Sydney", "Melbourne", "Canberra"),
            2
        ),
        Question(
            "Which programming language is primarily used for Android development?",
            listOf("Java", "Kotlin", "Both Java and Kotlin"),
            2
        ),
        Question(
            "What does SIT305 stand for?",
            listOf("Software Engineering", "Mobile Application Development", "Database Systems"),
            1
        ),
        Question(
            "Which company developed the Android operating system?",
            listOf("Apple", "Google", "Microsoft"),
            1
        ),
        Question(
            "What is the latest version of Android as of 2024?",
            listOf("Android 13", "Android 14", "Android 15"),
            2
        )
    )

    private var currentQuestionIndex = 0
    private var score = 0
    private val shuffledQuestions = questions.shuffled()

    init {
        // Shuffle answer options for each question
        shuffledQuestions.forEach { question ->
            question.options.shuffled()
        }
    }

    fun getCurrentQuestion(): Question {
        return shuffledQuestions[currentQuestionIndex]
    }

    fun getCurrentQuestionIndex(): Int {
        return currentQuestionIndex
    }

    fun hasNextQuestion(): Boolean {
        return currentQuestionIndex < shuffledQuestions.size - 1
    }

    fun moveToNextQuestion() {
        if (hasNextQuestion()) {
            currentQuestionIndex++
        }
    }

    fun recordAnswer(isCorrect: Boolean) {
        if (isCorrect) {
            score++
        }
    }

    fun getScore(): Int {
        return score
    }

    fun getTotalQuestions(): Int {
        return shuffledQuestions.size
    }

    fun resetQuiz() {
        currentQuestionIndex = 0
        score = 0
    }
}