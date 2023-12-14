package com.example.moodify

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "diary_entries")
data class DiaryEntry(
    @PrimaryKey(autoGenerate = true) var entryId: Int,
    var userEmail: String, // for access control
    var date: String,
    var dailyGratitude: String,
    var freeExpression: String,
    var photoURI: String? = null // path to the stored image, not the image itself
)
