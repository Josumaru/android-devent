package id.overlogic.devent.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.overlogic.devent.data.EventRepository
import id.overlogic.devent.data.remote.response.DetailResponse
import id.overlogic.devent.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val eventRepository: EventRepository): ViewModel() {
    fun getDetailEvent(id: Int) = eventRepository.getDetail(id)
    fun getBookmarkEvent() = eventRepository.getBookmark()
    fun setBookmark(status: Int, id: Int) = eventRepository.setBookmark(status, id)

}