package com.rivaphy.dicoding.submissionstory.view.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rivaphy.dicoding.submissionstory.data.api.response.ListStoryItem
import com.rivaphy.dicoding.submissionstory.data.repository.PagingRepository

class TellYoursViewModel(repository: PagingRepository): ViewModel() {
    val pagingStory: LiveData<PagingData<ListStoryItem>> =
        repository.getPaging().cachedIn(viewModelScope)
}