package id.overlogic.devent.ui.upcoming

import androidx.lifecycle.ViewModel
import id.overlogic.devent.data.EventRepository

class UpcomingViewModel(private val eventRepository: EventRepository): ViewModel() {

    fun getUpcomingEvent() = eventRepository.getUpcomingEvents()
    fun searchEvent(query: String) = eventRepository.searchEvents(query)
}
