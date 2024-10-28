package com.example.eventapps.ui.home

import androidx.lifecycle.ViewModel
import com.example.eventapps.remote.EventRepository

class HomeViewModel(val eventRepository: EventRepository) : ViewModel() {

    init {
        findFiveFinishedEvents()
        findFiveUpcomingEvents()
    }

    fun findFiveFinishedEvents() = eventRepository.getFinishedEvent(true)
    fun findFiveUpcomingEvents() = eventRepository.getUpcomingEvent(true)
}