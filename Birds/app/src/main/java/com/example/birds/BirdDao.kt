package com.example.birds

// BirdDao.kt
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

// BirdDao.kt
@Dao // Annotation indicating this is a Data Access Object (DAO)
interface BirdDao {
    // Inserts a Bird object into the database, replacing it if a conflict occurs
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBird(bird: Bird)

    // Queries the database to retrieve all Bird objects
    @Query("SELECT * FROM birds")
    suspend fun getAllBirds(): List<Bird>

    // Deletes all Bird entries from the database
    @Query("DELETE FROM birds")
    suspend fun deleteAllBirds() // Add this method
}
