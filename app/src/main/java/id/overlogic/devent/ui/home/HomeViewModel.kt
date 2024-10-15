package id.overlogic.devent.ui.home

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.overlogic.devent.data.response.EventResponse
import id.overlogic.devent.data.response.ListEventsItem
import id.overlogic.devent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    companion object{
        private const val TAG = "HomeViewModel"
    }

    private val _events = MutableLiveData<EventResponse>()
    val events: LiveData<EventResponse> = _events

    private val _listUpcomingEvents = MutableLiveData<List<ListEventsItem>>()
    val listUpcomingEvents: LiveData<List<ListEventsItem>> = _listUpcomingEvents

    private val _listFinishedEvents = MutableLiveData<List<ListEventsItem>>()
    val listFinishedEvents: LiveData<List<ListEventsItem>> = _listFinishedEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getEvents()
    }

    private fun getEvents() {
        _isLoading.value = true
        val upcomingEventClient = ApiConfig.getApiService().getEvents(1)
        val finishedEventClient = ApiConfig.getApiService().getEvents(0)

        upcomingEventClient.enqueue(object: Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _events.value = responseBody!!
                        _listUpcomingEvents.value = responseBody.listEvents
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })


        finishedEventClient.enqueue(object: Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _events.value = responseBody!!
                        _listFinishedEvents.value = responseBody.listEvents
                        _isLoading.value = false
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}