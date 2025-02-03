package com.example.gps_lab

// Importing necessary Android and Kotlin libraries
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location // Provides the Location class for working with geographic locations
import android.location.LocationListener // Interface for receiving location updates
import android.location.LocationManager // Manages location providers and updates
import android.os.Bundle // Contains data sent between activities or saved states
import android.os.Handler // Used to schedule tasks on a thread
import android.os.Looper // Provides access to the main thread's message loop
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button // Represents a button UI element
import android.widget.EditText // Represents an editable text input field
import android.widget.ListView
import android.widget.TextView // Represents a text display UI element
import android.widget.Toast // Provides a simple popup message
import androidx.appcompat.app.AppCompatActivity // Base class for activities using modern Android components
import androidx.core.app.ActivityCompat // Helper class for managing runtime permissions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

// MainActivity class that implements LocationListener to handle location updates
class MainActivity : AppCompatActivity(), LocationListener {

    private val LOCATION_PERMISSION_REQUEST_CODE = 1000

    // Variables for managing location and UI components
    private lateinit var locationManager: LocationManager // Used to request location updates
    private lateinit var editMinTime: EditText // Input field for minimum time between updates
    private lateinit var editMinDistance: EditText // Input field for minimum distance between updates
    private lateinit var editLog: EditText // Text area for displaying log entries
    private lateinit var clockView: TextView // TextView for displaying the current time

    // Variables for tracking time and distance
    private var lastUpdateTime: Long = 0 // Timestamp of the last location update
    private var currentDistance: Float = 0f // Tracks simulated distance updates

    private val locationHistory = mutableListOf<String>()
    private lateinit var historyAdapter: ArrayAdapter<String>
    //private lateinit var addressAdapter: AddressAdapter

    // Called when the activity is first created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // Call to the superclass method
        setContentView(R.layout.activity_main) // Set the layout for the activity

        checkLocationPermissions()

//        val zooCoordinatesView: TextView = findViewById(R.id.dublinZooCoordinates)
//        zooCoordinatesView.text = "Dublin Zoo Coordinates:\nLatitude: 53.3562, Longitude: -6.3046"
//
//        // Recycler view and adapter
//        val recyclerView: RecyclerView = findViewById(R.id.addressesRecyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        addressAdapter = AddressAdapter(mutableListOf())
//        recyclerView.adapter = addressAdapter

        // Initialize UI components using their IDs from the layout file
        editMinTime = findViewById(R.id.editMinTime) // EditText for minimum time
        editMinDistance = findViewById(R.id.editMinDistance) // EditText for minimum distance
        clockView = findViewById(R.id.clockView) // TextView for displaying the clock
        val buttonIncreaseDistance: Button = findViewById(R.id.buttonIncreaseDistance) // Button to increase distance
        val locationHistoryList: ListView = findViewById(R.id.locationHistoryList)
        editLog = findViewById(R.id.editLog) // EditText for displaying log entries
//        val locationView: TextView = findViewById(R.id.locationView) // TextView for location display (currently unused)

        // Initialize the LocationManager to handle location updates
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        historyAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationHistory)
        locationHistoryList.adapter = historyAdapter

        // Handler for updating the clock every second
        val handler = Handler(Looper.getMainLooper()) // Ensure updates are on the main thread
        handler.post(object : Runnable { // Define a Runnable to repeatedly update the clock
            override fun run() {
                val currentTimeMillis = System.currentTimeMillis() // Get the current time in milliseconds
                val time = java.text.SimpleDateFormat("HH:mm:ss").format(currentTimeMillis) // Format the time as HH:mm:ss
                clockView.text = time // Display the formatted time in the clockView
                handler.postDelayed(this, 1000) // Schedule the next update after 1 second
            }
        })

        // Set up a click listener for the "Increase Distance" button
        buttonIncreaseDistance.setOnClickListener {
            currentDistance += 10f // Increment the distance by 10 meters
            Toast.makeText(this, "Distance increased by 10m", Toast.LENGTH_SHORT).show() // Show a confirmation toast
            simulateLocationUpdate() // Trigger a simulated location update
        }

        // Check if the app has location permissions and request updates if allowed
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION // Fine location permission
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, // Use GPS for location updates
                1000, // Minimum time interval between updates (1 second)
                1f, // Minimum distance interval between updates (1 meter)
                this // Pass the current activity as the LocationListener
            )
        } else {
            // Show a message if permissions are not granted
            Toast.makeText(this, "Location permissions are required", Toast.LENGTH_SHORT).show()
        }
    }

    // Callback method for location updates
    override fun onLocationChanged(location: Location) {
        // Ensure that all required UI components are initialized
        if (!::editMinTime.isInitialized || !::editMinDistance.isInitialized || !::editLog.isInitialized) {
            Toast.makeText(this, "UI components not fully initialized!", Toast.LENGTH_SHORT).show()
            return
        }

        // Get user-specified minimum time and distance thresholds
        val minTime = editMinTime.text.toString().toLongOrNull() ?: 0L // Default to 0 if input is invalid
        val minDistance = editMinDistance.text.toString().toFloatOrNull() ?: 0f // Default to 0 if input is invalid

        // Calculate the time difference since the last update
        val currentTimeMillis = System.currentTimeMillis()
        val timeDifference = currentTimeMillis - lastUpdateTime

        // Check if the time and distance thresholds are met
        if (timeDifference >= minTime && currentDistance >= minDistance) {
            // Log the time and distance when conditions are satisfied
            val logEntry = StringBuilder(editLog.text.toString()) // Get current log text
            logEntry.append("Time: ${java.text.SimpleDateFormat("HH:mm:ss").format(currentTimeMillis)} | ") // Add timestamp
            logEntry.append("Distance: ${"%.2f".format(currentDistance)} m\n") // Add distance
            editLog.setText(logEntry.toString()) // Update the log EditText
            editLog.setSelection(logEntry.length) // Move cursor to the end

            // Reset variables for the next update
            lastUpdateTime = currentTimeMillis
            currentDistance = 0f

            Toast.makeText(this, "onLocationChanged triggered!", Toast.LENGTH_SHORT).show() // Notify the user
        }

        // Get address for new location
        val address = getAddressFromCoordinates(this, location.latitude, location.longitude)
        if (address != null) {
            locationHistory.add(address)
            historyAdapter.notifyDataSetChanged()
        } else {
            Toast.makeText(this, "Address not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getAddressFromCoordinates(context: Context, latitude: Double, longitude: Double) : String? {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        return if (addresses != null && addresses.isNotEmpty()) {
            val address = addresses[0]
            "${address.getAddressLine(0)}, ${address.locality}, ${address.countryName}"
        } else {
            null
        }
    }

//    private fun displayCurrentLocation() {
//        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
//            == PackageManager.PERMISSION_GRANTED
//        ) {
//            locationManager.requestLocationUpdates(
//                LocationManager.GPS_PROVIDER,
//                1000,
//                1f,
//                object : LocationListener {
//                    override fun onLocationChanged(location: Location) {
//                        val latitude = location.latitude
//                        val longitude = location.longitude
//                        val currentLocationView: TextView = findViewById(R.id.currentLocationView)
//                        currentLocationView.text = "Current Location:\nLat: $latitude, Lon: $longitude"
//                        updateAddress(latitude, longitude) // Update address using Geocoder
//                    }
//
//                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
//                    override fun onProviderEnabled(provider: String) {}
//                    override fun onProviderDisabled(provider: String) {}
//                }
//            )
//        }
//    }

//    private fun updateAddress(latitude: Double, longitude: Double) {
//        val geocoder = Geocoder(this, Locale.getDefault())
//        try {
//            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
//            if (addresses != null && addresses.isNotEmpty()) {
//                val address = addresses[0]
//                val addressView: TextView = findViewById(R.id.addressTextView)
//                addressView.text = "Address:\n${address.getAddressLine(0)}"
//            }
//        } catch (e: Exception) {
//            Toast.makeText(this, "Failed to fetch address", Toast.LENGTH_SHORT).show()
//        }
//    }


    private fun checkLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    // Simulate a location update for testing purposes
    private fun simulateLocationUpdate() {
        val location = Location(LocationManager.GPS_PROVIDER).apply {
            latitude = 53.3572264 // Simulated latitude
            longitude = -6.309684 // Simulated longitude
            accuracy = 1f // Simulated accuracy
        }
        onLocationChanged(location) // Trigger the location update callback
    }

    // Callback when a location provider is enabled
    override fun onProviderEnabled(provider: String) {}

    // Callback when a location provider is disabled
    override fun onProviderDisabled(provider: String) {}

    // Callback when the status of a location provider changes
    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    override fun onResume() {
        super.onResume()
        //displayCurrentLocation() // Start location tracking
        Log.d("Lifecycle", "Location tracking started")
    }

    override fun onPause() {
        super.onPause()
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.removeUpdates(this) // Stop location tracking
        Log.d("Lifecycle", "Location tracking stopped")
    }


}
