package com.example.task51c

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.task51c.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.buttonCreateAccount.setOnClickListener {
            val fullName = binding.editTextFullName.text.toString().trim()
            val username = binding.editTextUsername.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            val confirmPassword = binding.editTextConfirmPassword.text.toString().trim()

            if (validateSignUp(fullName, username, password, confirmPassword)) {
                // Save user data
                sharedPreferences.edit()
                    .putBoolean("isLoggedIn", true)
                    .putString("username", username)
                    .putString("fullName", fullName)
                    .apply()

                Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()
                startMainActivity()
            }
        }

        binding.textViewLogin.setOnClickListener {
            finish() // Go back to login
        }
    }

    private fun validateSignUp(fullName: String, username: String, password: String, confirmPassword: String): Boolean {
        return when {
            fullName.isEmpty() -> {
                Toast.makeText(this, "Please enter your full name", Toast.LENGTH_SHORT).show()
                false
            }
            username.isEmpty() -> {
                Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show()
                false
            }
            password.isEmpty() -> {
                Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
                false
            }
            password.length < 6 -> {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                false
            }
            password != confirmPassword -> {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}