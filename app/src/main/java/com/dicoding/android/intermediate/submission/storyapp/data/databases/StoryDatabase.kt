package com.dicoding.android.intermediate.submission.storyapp.data.databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.android.intermediate.submission.storyapp.data.dao.RemoteKeysDAO
import com.dicoding.android.intermediate.submission.storyapp.data.dao.StoryDAO
import com.dicoding.android.intermediate.submission.storyapp.data.entities.RemoteKeysEntity
import com.dicoding.android.intermediate.submission.storyapp.data.entities.StoryEntity

@Database(
    entities = [StoryEntity::class, RemoteKeysEntity::class],
    version = 1,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDAO(): StoryDAO
    abstract fun remoteKeysDAO(): RemoteKeysDAO

    companion object {
        @Volatile
        var INSTANCE: StoryDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): StoryDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    StoryDatabase::class.java, "story"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}