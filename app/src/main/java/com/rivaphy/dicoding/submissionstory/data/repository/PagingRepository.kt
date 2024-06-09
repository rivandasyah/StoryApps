package com.rivaphy.dicoding.submissionstory.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.rivaphy.dicoding.submissionstory.data.api.ApiService
import com.rivaphy.dicoding.submissionstory.data.api.response.ListResponse
import com.rivaphy.dicoding.submissionstory.data.api.response.ListStoryItem
import com.rivaphy.dicoding.submissionstory.data.database.TellYoursRoomDatabase

class PagingRepository private constructor(
    private val database: TellYoursRoomDatabase,
    private val apiService: ApiService
){
    fun getPaging(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager (
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = TellYoursRemoteMediator(database, apiService),
            pagingSourceFactory = {
                database.tellYoursDao().getUserStory()
            }
        ).liveData
    }

    suspend fun getMapStories(): ListResponse {
        return apiService.getStoriesWithLocation()
    }

    companion object {
        @Volatile
        private var instance: PagingRepository? = null

        fun getInstance(
            database: TellYoursRoomDatabase,
            apiService: ApiService
        ): PagingRepository =
            instance ?: synchronized(this) {
                instance ?: PagingRepository(database, apiService).also { instance = it }
            }
    }
}