package com.rivaphy.dicoding.submissionstory.view.signup


import androidx.lifecycle.ViewModel
import com.rivaphy.dicoding.submissionstory.data.api.response.RegisterResponse
import com.rivaphy.dicoding.submissionstory.data.repository.TellYoursRepository

class SignupViewModel(private val repository: TellYoursRepository) : ViewModel() {
    suspend fun userSignUp(name: String, email: String, password: String): RegisterResponse {
        return repository.userSignUp(name, email, password)
    }
}