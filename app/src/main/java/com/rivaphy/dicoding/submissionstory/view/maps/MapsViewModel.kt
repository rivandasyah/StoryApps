package com.rivaphy.dicoding.submissionstory.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rivaphy.dicoding.submissionstory.data.api.response.ListStoryItem
import com.rivaphy.dicoding.submissionstory.data.repository.PagingRepository
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: PagingRepository) : ViewModel() {

    private val _mapStories = MutableLiveData<List<ListStoryItem>>()
    val mapStories: LiveData<List<ListStoryItem>> = _mapStories

    private val _isError = MutableLiveData<String>()
    val isError: LiveData<String> = _isError

    fun getMapStories() {
        viewModelScope.launch {
            try {
                val response = repository.getMapStories()
                if (!response.error) {
                    _mapStories.value = response.listStory
                } else {
                    _isError.value = response.message
                }
            } catch (e: Exception) {
                _isError.value = e.message ?: "An unexpected error occured"
            }
        }
    }
}