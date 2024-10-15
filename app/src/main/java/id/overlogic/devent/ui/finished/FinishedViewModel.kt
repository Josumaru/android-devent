package id.overlogic.devent.ui.finished


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.overlogic.devent.data.response.EventResponse
import id.overlogic.devent.data.response.ListEventsItem
import id.overlogic.devent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedViewModel : ViewModel() {
    companion object{
        private const val TAG = "UpcomingViewModel"
    }

    private val _events = MutableLiveData<EventResponse>()
    val events: LiveData<EventResponse> = _events

    private val _listFinishedEvents = MutableLiveData<List<ListEventsItem>>()
    val listFinishedEvents: LiveData<List<ListEventsItem>> = _listFinishedEvents

    private val _listSearchEvents = MutableLiveData<List<ListEventsItem>>()
    val listSearchEvents: LiveData<List<ListEventsItem>> = _listSearchEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getEvents()
    }

    fun searchEvents(query: String) {
        _isLoading.value = true
        val searchListEvent = ApiConfig.getApiService().searchEvents(-1, query)
        searchListEvent.enqueue(object: Callback<EventResponse> {
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

    private fun getEvents() {
        _isLoading.value = true
        val upcomingEventClient = ApiConfig.getApiService().getEvents(0)

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