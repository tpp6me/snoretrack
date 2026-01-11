package com.praveen.snoretrack.di

import android.content.Context
import androidx.room.Room
import com.praveen.snoretrack.data.AppDatabase
import com.praveen.snoretrack.data.SessionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "snoretrack.db"
        ).build()
    }

    @Provides
    fun provideSessionDao(db: AppDatabase): SessionDao {
        return db.sessionDao()
    }
}
