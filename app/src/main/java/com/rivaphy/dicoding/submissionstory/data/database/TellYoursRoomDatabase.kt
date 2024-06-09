package com.rivaphy.dicoding.submissionstory.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rivaphy.dicoding.submissionstory.data.api.response.ListStoryItem

@Database(
    entities = [ListStoryItem::class, RemoteKeys::class],
    version = 5,
    exportSchema = false
)
abstract class TellYoursRoomDatabase : RoomDatabase() {
    abstract fun tellYoursDao(): TellYoursDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: TellYoursRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): TellYoursRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    TellYoursRoomDatabase::class.java,
                    "tellyours_db"
                ).fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}