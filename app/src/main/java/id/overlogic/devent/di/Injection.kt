package id.overlogic.devent.di

import android.content.Context
import id.overlogic.devent.data.EventRepository
import id.overlogic.devent.data.local.room.EventDatabase
import id.overlogic.devent.data.remote.retrofit.ApiConfig
import id.overlogic.devent.util.AppExecutors

object Injection {
    fun provider (context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventDatabase.getInstance(context)
        val dao = database.eventDao()
        val appExecutors = AppExecutors()
        return EventRepository.getInstance(apiService, dao, appExecutors)
    }
}