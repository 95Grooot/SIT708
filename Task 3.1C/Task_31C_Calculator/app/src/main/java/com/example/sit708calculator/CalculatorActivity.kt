package com.example.sit708calculator

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CalculatorActivity : AppCompatActivity() {

    private lateinit var editTextNumber1: EditText
    private lateinit var editTextNumber2: EditText
    private lateinit var buttonAdd: Button
    private lateinit var buttonSubtract: Button
    private lateinit var buttonClear: Button
    private lateinit var textViewResult: TextView
    private lateinit var textViewOperation: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        initializeViews()
        setupClickListeners()
    }

    private fun initializeViews() {
        editTextNumber1 = findViewById(R.id.editTextNumber1)
        editTextNumber2 = findViewById(R.id.editTextNumber2)
        buttonAdd = findViewById(R.id.buttonAdd)
        buttonSubtract = findViewById(R.id.buttonSubtract)
        buttonClear = findViewById(R.id.buttonClear)
        textViewResult = findViewById(R.id.textViewResult)
        textViewOperation = findViewById(R.id.textViewOperation)
    }

    private fun setupClickListeners() {
        buttonAdd.setOnClickListener {
            performAddition()
        }

        buttonSubtract.setOnClickListener {
            performSubtraction()
        }

        buttonClear.setOnClickListener {
            clearAll()
        }
    }

    private fun performAddition() {
        val numbers = getInputNumbers()
        if (numbers != null) {
            val (num1, num2) = numbers
            val result = num1 + num2

            displayResult(num1, num2, "+", result)
        }
    }

    private fun performSubtraction() {
        val numbers = getInputNumbers()
        if (numbers != null) {
            val (num1, num2) = numbers
            val result = num1 - num2

            displayResult(num1, num2, "-", result)
        }
    }

    private fun getInputNumbers(): Pair<Double, Double>? {
        try {
            val number1Text = editTextNumber1.text.toString().trim()
            val number2Text = editTextNumber2.text.toString().trim()

            if (number1Text.isEmpty()) {
                showError("Please enter the first number")
                editTextNumber1.requestFocus()
                return null
            }

            if (number2Text.isEmpty()) {
                showError("Please enter the second number")
                editTextNumber2.requestFocus()
                return null
            }

            val number1 = number1Text.toDouble()
            val number2 = number2Text.toDouble()

            return Pair(number1, number2)

        } catch (e: NumberFormatException) {
            showError("Please enter valid numbers")
            return null
        }
    }

    private fun displayResult(num1: Double, num2: Double, operation: String, result: Double) {
        // Format numbers to remove unnecessary decimal places
        val formattedNum1 = formatNumber(num1)
        val formattedNum2 = formatNumber(num2)
        val formattedResult = formatNumber(result)

        // Display the operation
        textViewOperation.text = "$formattedNum1 $operation $formattedNum2 ="

        // Display the result
        textViewResult.text = formattedResult

        // Show success message
        val operationName = if (operation == "+") "Addition" else "Subtraction"
        Toast.makeText(this, "$operationName completed successfully!", Toast.LENGTH_SHORT).show()
    }

    private fun formatNumber(number: Double): String {
        return if (number == number.toLong().toDouble()) {
            // If it's a whole number, display as integer
            number.toLong().toString()
        } else {
            // If it has decimals, format to 2 decimal places
            String.format("%.2f", number)
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun clearAll() {
        editTextNumber1.setText("")
        editTextNumber2.setText("")
        textViewOperation.text = ""
        textViewResult.text = "0"
        editTextNumber1.requestFocus()

        Toast.makeText(this, "Calculator cleared", Toast.LENGTH_SHORT).show()
    }
}