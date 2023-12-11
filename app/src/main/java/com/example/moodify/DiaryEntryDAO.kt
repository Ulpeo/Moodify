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

    // get all lucky numbers method
    @Query("SELECT * FROM diary_entries")
    fun getAll(): List<DiaryEntry>
}