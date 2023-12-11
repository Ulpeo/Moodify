package com.example.moodify

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DiaryEntry::class], version = 1)
abstract class DiaryEntriesDB: RoomDatabase() {
    abstract fun diaryEntryDAO(): DiaryEntryDAO

    companion object {
        @Volatile private var INSTANCE: DiaryEntriesDB? = null

        fun getInstance(context: Context): DiaryEntriesDB {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DiaryEntriesDB::class.java,
                    "diary_entries"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}