package com.example.birds

// BirdDatabase.kt
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

// Defines the Room database for Bird entities, with a version number for migrations
@Database(entities = [Bird::class], version = 2) // Increment the version when making schema changes
abstract class BirdDatabase : RoomDatabase() {
    // Abstract function to access BirdDao, which provides methods for database operations
    abstract fun birdDao(): BirdDao

    companion object {
        @Volatile
        private var INSTANCE: BirdDatabase? = null // Singleton instance to prevent multiple database instances

        // Returns the singleton instance of BirdDatabase, creating it if it doesn’t exist
        fun getDatabase(context: Context): BirdDatabase {
            // Checks if INSTANCE is already created; if not, it synchronizes to ensure only one instance is created
            return INSTANCE ?: synchronized(this) {
                // Builds the Room database with the specified configuration
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BirdDatabase::class.java,
                    "bird_database" // Sets the name of the database file
                ).build()
                INSTANCE = instance // Sets the INSTANCE to the newly created database
                instance // Returns the database instance
            }
        }
    }
}

