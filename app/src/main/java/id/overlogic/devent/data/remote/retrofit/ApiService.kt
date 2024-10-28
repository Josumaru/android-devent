package id.overlogic.devent.data.remote.retrofit

import id.overlogic.devent.data.remote.response.DetailResponse
import id.overlogic.devent.data.remote.response.EventResponse
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

    @GET("events/{id}")
    fun getEventById(
        @Path("id") id: String
    ): Call<DetailResponse>

    @GET("events")
    fun getReminder(
        @Query("active") active: Int = 1,
        @Query("limit") limit: Int = 1,
    ): Call<EventResponse>


}