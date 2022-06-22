package com.dicoding.android.intermediate.submission.storyapp.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.android.intermediate.submission.storyapp.data.entities.StoryEntity

@Dao
interface StoryDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: StoryEntity)

    @Query("SELECT * FROM story")
    fun getStoryList(): PagingSource<Int, StoryEntity>

    @Query("DELETE FROM story")
    fun deleteAllStory()
}