package id.overlogic.devent.data.retrofit

import id.overlogic.devent.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("events")
    fun getEvents(
        @Query("active") active: Int,
    ): Call<EventResponse>

    @GET("events")
    fun searchEvents(
        @Query("active") active: Int,
        @Query("q") keyword: String,
    ): Call<EventResponse>

}