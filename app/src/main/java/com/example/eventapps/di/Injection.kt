package com.example.eventapps.di

import android.content.Context
import com.example.eventapps.database.EventDatabase
import com.example.eventapps.remote.EventRepository
import com.example.eventapps.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventDatabase.getInstance(context)
        val favoriteEventDao = database.favoriteEventDao()
        val upcomingEVentDao = database.upcomingEventDao()
        val finishedEventDao = database.finishedEventDao()
        return EventRepository(apiService, favoriteEventDao, upcomingEVentDao, finishedEventDao)
    }
}