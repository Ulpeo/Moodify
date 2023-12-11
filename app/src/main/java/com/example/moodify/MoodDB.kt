package com.example.moodify

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Mood::class], version = 1)
abstract class MoodDB: RoomDatabase() {
    abstract fun moodDAO(): MoodDAO

    companion object {
        @Volatile private var INSTANCE: MoodDB? = null

        fun getInstance(context: Context): MoodDB {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MoodDB::class.java,
                    "mood"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}