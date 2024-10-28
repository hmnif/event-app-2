package com.example.eventapps.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.eventapps.database.FavoriteEvent
import com.example.eventapps.remote.EventRepository

class FavoriteViewModel(val eventRepository: EventRepository) : ViewModel() {
    fun getAllFavoriteEvent(): LiveData<List<FavoriteEvent>> = eventRepository.getAllFavoriteEvents()
}