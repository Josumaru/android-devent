package id.overlogic.devent.ui.finished


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.overlogic.devent.data.EventRepository
import id.overlogic.devent.data.remote.response.EventResponse
import id.overlogic.devent.data.remote.response.ListEventsItem
import id.overlogic.devent.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedViewModel(private val eventRepository: EventRepository): ViewModel() {
    fun getFinishedEvent() = eventRepository.getFinishedEvents()
    fun searchEvent(query: String) = eventRepository.searchEvents(query)
}
