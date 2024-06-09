package com.rivaphy.dicoding.submissionstory.data.di

import android.content.Context
import com.rivaphy.dicoding.submissionstory.data.api.ApiConfig
import com.rivaphy.dicoding.submissionstory.data.database.TellYoursRoomDatabase
import com.rivaphy.dicoding.submissionstory.data.pref.UserPreference
import com.rivaphy.dicoding.submissionstory.data.pref.dataStore
import com.rivaphy.dicoding.submissionstory.data.repository.PagingRepository
import com.rivaphy.dicoding.submissionstory.data.repository.TellYoursRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {

    fun provideRepository(context: Context): TellYoursRepository {
        val preferences = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return TellYoursRepository.getInstance(preferences, apiService)
    }

    fun provideTellYoursRepository(context: Context): PagingRepository {
        val apiService = ApiConfig.getApiService()
        val database = TellYoursRoomDatabase.getDatabase(context)
        return PagingRepository.getInstance(database, apiService)
    }
}