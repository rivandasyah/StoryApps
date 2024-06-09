package com.rivaphy.dicoding.submissionstory.view.login

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rivaphy.dicoding.submissionstory.R
import com.rivaphy.dicoding.submissionstory.data.api.response.LoginResponse
import com.rivaphy.dicoding.submissionstory.data.pref.UserModel
import com.rivaphy.dicoding.submissionstory.data.repository.TellYoursRepository
import kotlinx.coroutines.launch
class LoginViewModel(private val repository: TellYoursRepository) : ViewModel() {

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    suspend fun userSignIn(email: String?, password: String?): Result<LoginResponse> {
        val emailUser = email.orEmpty()
        val passwordUser = password.orEmpty()
        if (emailUser.isEmpty() || passwordUser.isEmpty()) {
            _errorMessage.postValue("Fields cannot be empty")
            return Result.failure(Exception("Fields cannot be empty"))
        }

        if (passwordUser.length < 8) {
            _errorMessage.postValue("Password must be at least 8 characters")
            return Result.failure(Exception("Password must be at least 8 characters"))
        }

        return try {
            val response = repository.userSignIn(emailUser, passwordUser)
            Result.success(response)
        } catch (e: Exception) {
            _errorMessage.postValue(e.message ?: "Unknown error")
            Result.failure(e)
        }
    }

    fun saveSession(model: UserModel) {
        viewModelScope.launch {
            repository.saveSession(model)
        }
    }

}