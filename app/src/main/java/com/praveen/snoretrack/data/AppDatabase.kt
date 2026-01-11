package com.praveen.snoretrack.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Session::class, SnoreEvent::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
}
