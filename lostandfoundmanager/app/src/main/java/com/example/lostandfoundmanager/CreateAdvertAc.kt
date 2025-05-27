package com.example.lostandfoundmanager

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.lostandfoundmanager.database.AdvertDatabase
import com.example.lostandfoundmanager.database.AdvertItem
import com.example.lostandfoundmanager.database.AdvertRepository
import com.example.lostandfoundmanager.databinding.ActivityCreateAdvertBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CreateAdvertActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateAdvertBinding
    private lateinit var repository: AdvertRepository
    private var selectedDate: Date = Date()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAdvertBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDatabase()
        setupDatePicker()
        setupButtons()

        // Set Lost as default
        binding.radioLost.isChecked = true
    }

    private fun setupDatabase() {
        val database = AdvertDatabase.getDatabase(this)
        repository = AdvertRepository(database.advertDao())
    }

    private fun setupDatePicker() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        binding.etDate.setText(dateFormat.format(selectedDate))

        binding.etDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.time = selectedDate

            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    selectedDate = calendar.time
                    binding.etDate.setText(dateFormat.format(selectedDate))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setupButtons() {
        binding.btnSave.setOnClickListener {
            saveAdvert()
        }
    }

    private fun saveAdvert() {
        val name = binding.etName.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()
        val location = binding.etLocation.text.toString().trim()
        val type = if (binding.radioLost.isChecked) "Lost" else "Found"

        // Validate all fields
        if (validateFields(name, phone, description, location)) {
            val advert = AdvertItem(
                type = type,
                name = name,
                phone = phone,
                description = description,
                date = selectedDate,
                location = location
            )

            lifecycleScope.launch {
                repository.insertAdvert(advert)
                Toast.makeText(this@CreateAdvertActivity, "Advert saved successfully!", Toast.LENGTH_SHORT).show()
                finish() // Return to previous screen
            }
        }
    }

    private fun validateFields(name: String, phone: String, description: String, location: String): Boolean {
        var isValid = true

        if (name.isEmpty()) {
            binding.etName.error = "Name is required"
            isValid = false
        }

        if (phone.isEmpty()) {
            binding.etPhone.error = "Phone is required"
            isValid = false
        }

        if (description.isEmpty()) {
            binding.etDescription.error = "Description is required"
            isValid = false
        }

        if (location.isEmpty()) {
            binding.etLocation.error = "Location is required"
            isValid = false
        }

        return isValid
    }
}