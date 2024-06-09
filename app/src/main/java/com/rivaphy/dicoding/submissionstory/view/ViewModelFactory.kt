package com.rivaphy.dicoding.submissionstory.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rivaphy.dicoding.submissionstory.data.di.Injection
import com.rivaphy.dicoding.submissionstory.data.repository.TellYoursRepository
import com.rivaphy.dicoding.submissionstory.view.login.LoginViewModel
import com.rivaphy.dicoding.submissionstory.view.main.MainViewModel
import com.rivaphy.dicoding.submissionstory.view.signup.SignupViewModel

class ViewModelFactory(private val repository: TellYoursRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }

            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        fun getInstance(context: Context) = ViewModelFactory(Injection.provideRepository(context))
    }
}