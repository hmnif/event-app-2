package com.example.eventapps.ui.upcoming

import androidx.lifecycle.ViewModel
import com.example.eventapps.remote.EventRepository

class UpcomingViewModel(val eventRepository: EventRepository) : ViewModel() {

    init {
        findUpcomingEvents()
    }

    fun findUpcomingEvents(keyword: String? = null) = eventRepository.getUpcomingEvent(limitFiveData = false, keyword = keyword)
}