package com.example.moodify

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mood")
data class Mood(
    @PrimaryKey(autoGenerate = true) val moodId: Int,
    val userEmail: String, // for access control
    val date: String,
    val mood: String
)
