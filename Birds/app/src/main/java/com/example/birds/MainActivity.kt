package com.example.birds

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch


// Main activity for the app, displaying a list of birds in a RecyclerView
class MainActivity : AppCompatActivity() {
    private lateinit var birdRecyclerView: RecyclerView // RecyclerView for displaying bird items
    private lateinit var birdAdapter: BirdAdapter // Adapter to bind bird data to RecyclerView
    private lateinit var birdDatabase: BirdDatabase // Room database instance for storing and retrieving birds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up RecyclerView and its layout manager
        birdRecyclerView = findViewById(R.id.birdRecyclerView)
        birdRecyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize the Room database instance
        birdDatabase = BirdDatabase.getDatabase(this)

        // Initialize the adapter with an empty list and set it to the RecyclerView
        birdAdapter = BirdAdapter(emptyList())
        birdRecyclerView.adapter = birdAdapter

        // Insert sample birds into the database, then retrieve and display them
        insertBirdsIntoDatabase(getSampleBirds())
    }

    // Inserts a list of bird data into the database
    private fun insertBirdsIntoDatabase(birdList: List<Bird>) {
        // Launches a coroutine to handle database operations on a background thread
        lifecycleScope.launch {
            birdList.forEach { bird ->
                birdDatabase.birdDao().insertBird(bird) // Insert each bird into the database
                Log.d("Bird", "Inserted: ${bird.name}") // Log insertion for debugging
            }
            readBirdsFromDatabase() // Retrieve all birds after insertion
        }
    }

    // Reads all birds from the database and updates the RecyclerView
    private fun readBirdsFromDatabase() {
        lifecycleScope.launch {
            val birds = birdDatabase.birdDao().getAllBirds() // Retrieve all bird entries
            Log.d("Bird", "Retrieved birds: $birds") // Log retrieved birds for debugging
            updateRecyclerView(birds) // Update the RecyclerView with retrieved data
        }
    }

    // Updates the RecyclerView with a new list of birds
    private fun updateRecyclerView(birds: List<Bird>) {
        birdAdapter.updateBirds(birds) // Calls the adapter’s update method to refresh data
    }

    private fun getSampleBirds(): List<Bird> {
        return listOf(
            Bird(name = "Sparrow", imageResId = R.drawable.sparrow, description = "A small, brown and gray bird."),
            Bird(name = "Eagle", imageResId = R.drawable.eagle, description = "A large bird of prey with powerful wings."),
            Bird(name = "Parrot", imageResId = R.drawable.parrot, description = "A colorful bird known for its ability to mimic sounds."),
            Bird(name = "Hummingbird", imageResId = R.drawable.hummingbird, description = "A tiny bird known for its ability to hover in mid-air."),
            Bird(name = "Penguin", imageResId = R.drawable.penguin, description = "A flightless bird that waddles and is often found in cold climates."),
            Bird(name = "Flamingo", imageResId = R.drawable.flamingo, description = "A wading bird known for its pink color and long legs."),
            Bird(name = "Owl", imageResId = R.drawable.owl, description = "A nocturnal bird known for its hooting sound and wise appearance."),
            Bird(name = "Peacock", imageResId = R.drawable.peacock, description = "A bird known for its iridescent tail feathers and beautiful display."),
            Bird(name = "Canary", imageResId = R.drawable.canary, description = "A small yellow bird known for its singing ability."),
            Bird(name = "Cardinal", imageResId = R.drawable.cardinal, description = "A red bird with a distinctive crest, often found in gardens."),
            Bird(name = "Blue Jay", imageResId = R.drawable.blue_jay, description = "A blue bird known for its intelligence and complex social behavior."),
            Bird(name = "Robin", imageResId = R.drawable.robin, description = "A small bird with a red breast, often associated with spring."),
            Bird(name = "Woodpecker", imageResId = R.drawable.woodpecker, description = "A bird known for its pecking behavior on trees."),
            Bird(name = "Hawk", imageResId = R.drawable.hawk, description = "A bird of prey known for its keen eyesight and hunting skills."),
            Bird(name = "Dove", imageResId = R.drawable.dove, description = "A gentle bird often symbolizing peace, known for its cooing sound.")
        )
    }

}
