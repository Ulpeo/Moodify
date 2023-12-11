package com.example.moodify

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mood")
data class Mood(
    @PrimaryKey val date: String,
    val mood: String
)
