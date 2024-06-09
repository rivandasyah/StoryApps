package com.rivaphy.dicoding.submissionstory.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rivaphy.dicoding.submissionstory.data.api.ApiService
import com.rivaphy.dicoding.submissionstory.data.api.response.ListStoryItem

class MainPagingSource(private val apiService: ApiService) :
    PagingSource<Int, ListStoryItem>() {

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(INITIAL_PAGE_INDEX) ?: anchorPage?.nextKey?.minus(
                INITIAL_PAGE_INDEX
            )
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val response = apiService.getStories(position, params.loadSize)
            val responseData = response.listStory
            LoadResult.Page(
                data = responseData,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - INITIAL_PAGE_INDEX,
                nextKey = if (response.listStory.isEmpty()) null else position + INITIAL_PAGE_INDEX
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}
