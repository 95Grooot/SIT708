package com.example.lostandfoundmanager

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.lostandfoundmanager.database.DatabaseHelper
import com.example.lostandfoundmanager.databinding.ActivityMapBinding
import com.example.lostandfoundmanager.models.LostFoundItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMapBinding
    private lateinit var database: DatabaseHelper
    private lateinit var googleMap: GoogleMap
    private var allItems: List<LostFoundItem> = emptyList()
    private var currentFilter = "All"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = DatabaseHelper.getDatabase(this)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Items Map"
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Configure map settings
        googleMap.apply {
            mapType = GoogleMap.MAP_TYPE_NORMAL
            uiSettings.isZoomControlsEnabled = true
            uiSettings.isCompassEnabled = true
        }

        loadItemsOnMap()
    }

    private fun loadItemsOnMap() {
        lifecycleScope.launch {
            allItems = withContext(Dispatchers.IO) {
                database.itemsDao().getAllItemsList()
            }
            displayItemsOnMap()
        }
    }

    private fun displayItemsOnMap() {
        googleMap.clear()

        val itemsToShow = when (currentFilter) {
            "Lost" -> allItems.filter { it.type == "Lost" }
            "Found" -> allItems.filter { it.type == "Found" }
            else -> allItems
        }

        if (itemsToShow.isEmpty()) return

        val boundsBuilder = LatLngBounds.Builder()
        var hasValidLocation = false

        itemsToShow.forEach { item ->
            if (item.latitude != 0.0 && item.longitude != 0.0) {
                val position = LatLng(item.latitude, item.longitude)

                val markerOptions = MarkerOptions()
                    .position(position)
                    .title(item.name)
                    .snippet("${item.type} - ${item.description}")
                    .icon(getMarkerIcon(item.type))

                googleMap.addMarker(markerOptions)?.tag = item
                boundsBuilder.include(position)
                hasValidLocation = true
            }
        }

        if (hasValidLocation) {
            try {
                val bounds = boundsBuilder.build()
                val padding = 100
                val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
                googleMap.animateCamera(cameraUpdate)
            } catch (e: Exception) {
                // Fallback to default location if bounds calculation fails
                val defaultLocation = LatLng(-37.8136, 144.9631) // Melbourne
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f))
            }
        }

        // Set info window click listener
        googleMap.setOnInfoWindowClickListener { marker ->
            val item = marker.tag as? LostFoundItem
            item?.let {
                // You can add navigation to item details here
            }
        }
    }

    private fun getMarkerIcon(type: String): BitmapDescriptor {
        return when (type) {
            "Lost" -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
            "Found" -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
            else -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.map_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filter_all -> {
                currentFilter = "All"
                displayItemsOnMap()
                true
            }
            R.id.filter_lost -> {
                currentFilter = "Lost"
                displayItemsOnMap()
                true
            }
            R.id.filter_found -> {
                currentFilter = "Found"
                displayItemsOnMap()
                true
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}