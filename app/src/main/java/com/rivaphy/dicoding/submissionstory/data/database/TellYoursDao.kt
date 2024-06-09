package com.rivaphy.dicoding.submissionstory.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rivaphy.dicoding.submissionstory.data.api.response.ListStoryItem

@Dao
interface TellYoursDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inputStory(story: List<ListStoryItem>)

    @Query("SELECT * FROM ListStoryItem")
    fun getUserStory(): PagingSource<Int, ListStoryItem>

    @Query("DELETE FROM ListStoryItem")
    suspend fun deleteUserStory()
}