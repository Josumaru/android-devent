package id.overlogic.devent.ui.favourite

import androidx.lifecycle.ViewModel
import id.overlogic.devent.data.EventRepository

class FavouriteViewModel(private val eventRepository: EventRepository): ViewModel() {
    fun getBookmark() = eventRepository.getBookmark()
}