package id.overlogic.devent.ui.home

import android.util.Log
import android.widget.Toast
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

class HomeViewModel(private val eventRepository: EventRepository?) : ViewModel() {
    fun getUpcomingEvent() = eventRepository?.getUpcomingEvents()
    fun getFinishedEvent() = eventRepository?.getFinishedEvents()
}