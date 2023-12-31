package com.example.moodify

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MoodDAO {
    // insert method
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(mood: Mood)

    // delete method
    @Delete
    fun delete(mood: Mood)

    // get all diary entries
    @Query("SELECT * FROM mood WHERE userEmail = :userEmail")
    fun getAll(userEmail: String): List<Mood>

    // get a specific diary entry
    @Query("SELECT * FROM mood WHERE userEmail = :userEmail AND date = :moodDate")
    fun getMood(userEmail: String, moodDate: String): Mood
}