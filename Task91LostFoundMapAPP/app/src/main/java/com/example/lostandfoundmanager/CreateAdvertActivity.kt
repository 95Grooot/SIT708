package com.example.lostandfoundmanager

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.lostandfoundmanager.database.DatabaseHelper
import com.example.lostandfoundmanager.databinding.ActivityCreateAdvertBinding
import com.example.lostandfoundmanager.models.LostFoundItem
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*

class CreateAdvertActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateAdvertBinding
    private lateinit var database: DatabaseHelper
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var selectedLatitude: Double = 0.0
    private var selectedLongitude: Double = 0.0

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                getCurrentLocation()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                getCurrentLocation()
            }
            else -> {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAdvertBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initializeComponents()
        setupPlacesAutocomplete()
        setupButtons()
    }

    private fun initializeComponents() {
        database = DatabaseHelper.getDatabase(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Initialize Places API
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, BuildConfig.MAPS_API_KEY)
         //  Places.initialize(applicationContext, "AIzaSyCTAHE71Chqq2nwcMjwa3R5y8RYu-99hF8")
        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Create Advertisement"
        }
    }

    private fun setupPlacesAutocomplete() {
        val autocompleteFragment = supportFragmentManager
            .findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment

        autocompleteFragment.setPlaceFields(
            listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)
        )

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                binding.etLocation.setText(place.address ?: place.name)
                place.latLng?.let {
                    selectedLatitude = it.latitude
                    selectedLongitude = it.longitude
                }
            }

            override fun onError(status: Status) {
                Toast.makeText(this@CreateAdvertActivity,
                    "Error: ${status.statusMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupButtons() {
        binding.btnGetCurrentLocation.setOnClickListener {
            checkLocationPermissionAndGetLocation()
        }

        binding.btnSave.setOnClickListener {
            saveItem()
        }
    }

    private fun checkLocationPermissionAndGetLocation() {
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                getCurrentLocation()
            }
            else -> {
                locationPermissionRequest.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    selectedLatitude = it.latitude
                    selectedLongitude = it.longitude
                    getAddressFromLocation(it.latitude, it.longitude)
                } ?: run {
                    Toast.makeText(this, "Unable to get current location", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getAddressFromLocation(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val addressText = address.getAddressLine(0) ?: "Current Location"
                binding.etLocation.setText(addressText)
            }
        } catch (e: IOException) {
            binding.etLocation.setText("Current Location")
        }
    }

    private fun saveItem() {
        val type = if (binding.rbLost.isChecked) "Lost" else "Found"
        val name = binding.etName.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()
        val date = binding.etDate.text.toString().trim()
        val location = binding.etLocation.text.toString().trim()

        if (validateInput(name, phone, description, date, location)) {
            val item = LostFoundItem(
                type = type,
                name = name,
                phone = phone,
                description = description,
                date = date,
                location = location,
                latitude = selectedLatitude,
                longitude = selectedLongitude
            )

            lifecycleScope.launch {
                try {
                    withContext(Dispatchers.IO) {
                        database.itemsDao().insertItem(item)
                    }
                    Toast.makeText(this@CreateAdvertActivity,
                        "Item saved successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(this@CreateAdvertActivity,
                        "Error saving item", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun validateInput(
        name: String, phone: String, description: String,
        date: String, location: String
    ): Boolean {
        return when {
            name.isEmpty() -> {
                binding.etName.error = "Name is required"
                false
            }
            phone.isEmpty() -> {
                binding.etPhone.error = "Phone is required"
                false
            }
            description.isEmpty() -> {
                binding.etDescription.error = "Description is required"
                false
            }
            date.isEmpty() -> {
                binding.etDate.error = "Date is required"
                false
            }
            location.isEmpty() -> {
                binding.etLocation.error = "Location is required"
                false
            }
            selectedLatitude == 0.0 && selectedLongitude == 0.0 -> {
                Toast.makeText(this, "Please select a valid location", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}