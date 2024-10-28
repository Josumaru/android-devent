package id.overlogic.devent.data

import android.net.http.UrlRequest.Status
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import id.overlogic.devent.data.local.entity.EventEntity
import id.overlogic.devent.data.local.room.EventDao
import id.overlogic.devent.data.remote.response.DetailResponse
import id.overlogic.devent.data.remote.response.EventResponse
import id.overlogic.devent.data.remote.retrofit.ApiService
import id.overlogic.devent.util.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventRepository private constructor(
    private val apiService: ApiService,
    private val eventDao: EventDao,
    private val appExecutors: AppExecutors
) {
    private val result = MediatorLiveData<Result<List<EventEntity>>>()
    private val search = MediatorLiveData<Result<List<EventEntity>>>()
    private val finishedResult = MediatorLiveData<Result<List<EventEntity>>>()
    private val detailResult = MediatorLiveData<Result<EventEntity>>()
    private val bookmark = MediatorLiveData<Result<List<EventEntity>>>()

    fun setBookmark(status: Int, id: Int) {
        appExecutors.diskIO.execute {
            eventDao.setBookmark(status, id)
        }
    }

    fun getBookmark(): LiveData<Result<List<EventEntity>>> {
        bookmark.value = Result.Loading
        appExecutors.diskIO.execute {
            val bm = eventDao.getBookmark()
            if(bm != null) {
                bookmark.postValue(Result.Success(bm))
            } else {
                bookmark.postValue(Result.Error("Data kosong"))
            }
        }
        return bookmark
    }

    fun getDetail(id: Int): LiveData<Result<EventEntity>> {
        detailResult.value = Result.Loading
        val client = apiService.getEventById(id.toString())
        client.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                if (response.isSuccessful) {
                    val event = response.body()?.event!!
                    detailResult.value = Result.Success(
                        EventEntity(
                            event.id,
                            event.name,
                            event.summary,
                            event.description,
                            event.imageLogo,
                            event.mediaCover,
                            event.category,
                            event.ownerName,
                            event.cityName,
                            event.quota,
                            event.registrants,
                            event.beginTime,
                            event.endTime,
                            event.link,
                        )
                    )
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                detailResult.value = Result.Error(t.message.toString())
            }
        })

        return detailResult
    }

    fun getUpcomingEvents(): LiveData<Result<List<EventEntity>>> {
        result.value = Result.Loading
        val client = apiService.getEvents(1)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    val lists = response.body()?.listEvents
                    val eventList = ArrayList<EventEntity>()
                    appExecutors.diskIO.execute {
                        lists?.forEach { event ->
                            val existingEvent = eventDao.getEventById(event.id)
                            if (existingEvent == null) {
                                val ev = EventEntity(
                                    event.id,
                                    event.name,
                                    event.summary,
                                    event.description,
                                    event.imageLogo,
                                    event.mediaCover,
                                    event.category,
                                    event.ownerName,
                                    event.cityName,
                                    event.quota,
                                    event.registrants,
                                    event.beginTime,
                                    event.endTime,
                                    event.link,
                                    active = true
                                )
                                eventList.add(ev)
                            }
                            eventDao.insertEvent(eventList)
                        }}
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })
        val localData = eventDao.getEvent()
        result.addSource(localData) { data: List<EventEntity> ->
            val listEvent = ArrayList<EventEntity>()
            data.forEach { event ->
                if(event.active) {
                    listEvent += event
                }
            }
            result.value = Result.Success(listEvent)
        }
        return result
    }

    fun getFinishedEvents(): LiveData<Result<List<EventEntity>>> {
        finishedResult.value = Result.Loading
        val client = apiService.getEvents(0)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    val lists = response.body()?.listEvents
                    val eventList = ArrayList<EventEntity>()
                    appExecutors.diskIO.execute {
                        lists?.forEach { event ->
                            val existingEvent = eventDao.getEventById(event.id)
                            if (existingEvent == null) {
                                val ev = EventEntity(
                                    event.id,
                                    event.name,
                                    event.summary,
                                    event.description,
                                    event.imageLogo,
                                    event.mediaCover,
                                    event.category,
                                    event.ownerName,
                                    event.cityName,
                                    event.quota,
                                    event.registrants,
                                    event.beginTime,
                                    event.endTime,
                                    event.link,
                                )
                                eventList.add(ev)
                        }
                        eventDao.insertEvent(eventList)
                    }}
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                finishedResult.value = Result.Error(t.message.toString())
            }
        })
        val localData = eventDao.getEvent()
        finishedResult.addSource(localData) { data: List<EventEntity> ->
            val listEvent = ArrayList<EventEntity>()
            data.forEach { event ->
                if(!event.active) {
                    listEvent += event
                }
            }
            finishedResult.value = Result.Success(listEvent)
        }
        return finishedResult
    }

    fun searchEvents(query: String): LiveData<Result<List<EventEntity>>> {
        search.value = Result.Loading
        val client = apiService.searchEvents(-1, query)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    val lists = response.body()?.listEvents
                    val newsList = ArrayList<EventEntity>()
                    appExecutors.diskIO.execute {
                        lists?.forEach { event ->
                            val news = EventEntity(
                                event.id,
                                event.name,
                                event.summary,
                                event.description,
                                event.imageLogo,
                                event.mediaCover,
                                event.category,
                                event.ownerName,
                                event.cityName,
                                event.quota,
                                event.registrants,
                                event.beginTime,
                                event.endTime,
                                event.link,
                            )
                            newsList.add(news)
                        }
                        eventDao.deleteAll()
                        eventDao.insertEvent(newsList)
                    }
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                search.value = Result.Error(t.message.toString())
            }
        })
        val localData = eventDao.getEvent()
        search.addSource(localData) { data: List<EventEntity> ->
            search.value = Result.Success(data)
        }
        return search
    }

    companion object {
        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            apiService: ApiService,
            eventDao: EventDao,
            appExecutors: AppExecutors
        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(apiService, eventDao, appExecutors)
            }.also { instance = it }
    }
}