package com.example.moodify

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DiaryEntryDAO {
    // insert method
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entry: DiaryEntry)

    // delete method
    @Delete
    fun delete(entry: DiaryEntry)

    // get all diary entries
    @Query("SELECT * FROM diary_entries")
    fun getAll(): List<DiaryEntry>

    // get a specific diary entry
    @Query("SELECT * FROM diary_entries WHERE date = :entryDate")
    fun getEntry(entryDate: String): DiaryEntry
}