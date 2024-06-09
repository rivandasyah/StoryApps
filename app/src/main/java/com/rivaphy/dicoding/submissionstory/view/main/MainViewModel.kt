package com.rivaphy.dicoding.submissionstory.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rivaphy.dicoding.submissionstory.data.pref.UserModel
import com.rivaphy.dicoding.submissionstory.data.repository.TellYoursRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: TellYoursRepository) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}