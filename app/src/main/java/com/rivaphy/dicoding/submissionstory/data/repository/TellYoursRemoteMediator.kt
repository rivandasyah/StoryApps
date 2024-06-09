package com.rivaphy.dicoding.submissionstory.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.rivaphy.dicoding.submissionstory.data.api.ApiService
import com.rivaphy.dicoding.submissionstory.data.api.response.ListStoryItem
import com.rivaphy.dicoding.submissionstory.data.database.RemoteKeys
import com.rivaphy.dicoding.submissionstory.data.database.TellYoursRoomDatabase

@OptIn(ExperimentalPagingApi::class)
class TellYoursRemoteMediator(
    private val database: TellYoursRoomDatabase,
    private val apiService: ApiService,
) : RemoteMediator<Int, ListStoryItem>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ListStoryItem>
    ): MediatorResult {
        val paging = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(INITIAL_PAGE_INDEX) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val response = apiService.getStories(paging, state.config.pageSize)
            val endOfPaginationReached = response.listStory.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().deleteRemoteKeys()
                    database.tellYoursDao().deleteUserStory()
                }

                val prevKey = if (paging == INITIAL_PAGE_INDEX) null else paging - INITIAL_PAGE_INDEX
                val nextKey = if (endOfPaginationReached) null else paging + INITIAL_PAGE_INDEX
                val keys = response.listStory.map {
                    RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }

                database.remoteKeysDao().insertAll(keys)
                database.tellYoursDao().inputStory(response.listStory)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ListStoryItem>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { it ->
            database.remoteKeysDao().getRemoteKeysId(it.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ListStoryItem>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let {
            database.remoteKeysDao().getRemoteKeysId(it.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ListStoryItem>): RemoteKeys? {
        return state.anchorPosition?.let {
            state.closestItemToPosition(it)?.id?.let {
                database.remoteKeysDao().getRemoteKeysId(it)
            }
        }
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }
    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}