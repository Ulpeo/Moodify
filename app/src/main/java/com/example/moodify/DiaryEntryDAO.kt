package com.example.moodify

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface DiaryEntryDAO {
    // insert method
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entry: DiaryEntry)

    // update method
    @Update
    fun update(entry: DiaryEntry)

    // delete method
    @Delete
    fun delete(entry: DiaryEntry)

    // get all diary entries
    @Query("SELECT * FROM diary_entries")
    fun getAll(): List<DiaryEntry>

    // get a specific diary entry
    @Query("SELECT * FROM diary_entries WHERE userEmail = :userEmail AND date = :entryDate")
    fun getEntry(userEmail: String, entryDate: String): DiaryEntry

    //get the number of entries by a person
    @Query("SELECT COUNT(*) FROM diary_entries WHERE userEmail = :userEmail")
    fun getNumberEntries(userEmail: String?): String
}