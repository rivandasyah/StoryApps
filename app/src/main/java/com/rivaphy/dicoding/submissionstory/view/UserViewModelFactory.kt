package com.rivaphy.dicoding.submissionstory.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rivaphy.dicoding.submissionstory.data.di.Injection
import com.rivaphy.dicoding.submissionstory.data.repository.PagingRepository
import com.rivaphy.dicoding.submissionstory.view.maps.MapsViewModel
import com.rivaphy.dicoding.submissionstory.view.story.TellYoursViewModel

class UserViewModelFactory(private val repository: PagingRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(repository) as T
            }
            modelClass.isAssignableFrom(TellYoursViewModel::class.java) -> {
                TellYoursViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        fun getInstance(context: Context) = UserViewModelFactory(Injection.provideTellYoursRepository(context))
    }
}