package com.example.moodify

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.AutoMigration

@Database(
    entities = [Mood::class],
    version = 1
)
abstract class MoodsDB: RoomDatabase() {
    abstract fun moodDAO(): MoodDAO

    companion object {
        @Volatile private var INSTANCE: MoodsDB? = null

        fun getInstance(context: Context): MoodsDB {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MoodsDB::class.java,
                    "moods"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}