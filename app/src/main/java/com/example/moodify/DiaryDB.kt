package com.example.moodify

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [DiaryEntry::class],
    version = 1
)
abstract class DiaryDB: RoomDatabase() {
    abstract fun diaryEntryDAO(): DiaryEntryDAO

    companion object {
        @Volatile private var INSTANCE: DiaryDB? = null

        fun getInstance(context: Context): DiaryDB {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DiaryDB::class.java,
                    "diary"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}