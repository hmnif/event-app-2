package com.example.eventapps.ui.finished

import androidx.lifecycle.ViewModel
import com.example.eventapps.remote.EventRepository

class FinishedViewModel(val eventRepository: EventRepository) : ViewModel() {
    init {
        findFinishedEvents()
    }
    fun findFinishedEvents(keyword: String? = null) = eventRepository.getFinishedEvent(limitFiveData = false, keyword = keyword)
}