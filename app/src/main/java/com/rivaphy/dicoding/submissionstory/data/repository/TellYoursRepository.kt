package com.rivaphy.dicoding.submissionstory.data.repository


import android.util.Log
import com.rivaphy.dicoding.submissionstory.data.api.ApiConfig
import com.rivaphy.dicoding.submissionstory.data.api.ApiService
import com.rivaphy.dicoding.submissionstory.data.api.response.LoginResponse
import com.rivaphy.dicoding.submissionstory.data.api.response.RegisterResponse
import com.rivaphy.dicoding.submissionstory.data.pref.UserModel
import com.rivaphy.dicoding.submissionstory.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException

class TellYoursRepository(
    private val preference: UserPreference,
    private val apiService: ApiService
) {


    suspend fun userSignUp(name: String, email: String, password: String): RegisterResponse {
        return try {
            apiService.register(name, email, password)
        } catch (e: HttpException) {
            val body = e.response()?.errorBody()?.string()
            throw Exception(body)
        } catch (e: Throwable) {
            throw Exception(e.message)
        }
    }

    suspend fun userSignIn(email: String, password: String) : LoginResponse {
        return try {
            val response = apiService.login(email, password)
            val token = response.loginResult?.token ?: ""
            val model = UserModel(email = email, token = token, isLogin = true)
            saveSession(model)
            ApiConfig.setToken(token)
            response
        } catch (e: HttpException) {
            val errorMessage = try {
                val body = e.response()?.errorBody()?.string()
                if (body != null) {
                    JSONObject(body).getString("message")
                } else {
                    "Unknown error occurred."
                }
            } catch (jsonException: JSONException) {
                Log.e("SignIn Error", "Error parsing error body: $e")
                "Something went wrong. Please try again."
            }
            throw Exception(errorMessage)
        } catch (e: Throwable) {
            throw Exception(e.message ?: "Unknown error")
        }
    }

    suspend fun saveSession(user: UserModel) {
        preference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return preference.getSession()
    }

    suspend fun logout() {
        preference.logout()
    }

    companion object {
        fun getInstance(
            preferences: UserPreference,
            apiService: ApiService
        ) = TellYoursRepository(preferences, apiService)
    }
}