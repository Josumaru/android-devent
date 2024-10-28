package id.overlogic.devent.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import id.overlogic.devent.R
import id.overlogic.devent.data.remote.response.EventResponse
import id.overlogic.devent.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    companion object {
        const val NOTIFICATION_ID = 2
        const val CHANNEL_ID = "Daily Reminder"
        const val CHANNEL_NAME = "Devent Upcoming Event"
    }

    private var resultStatus: Result? = null

    override fun doWork(): Result {
        return getEventReminder()
    }


    private fun getEventReminder(): Result {
        Looper.prepare()
        val upcomingEventClient = ApiConfig.getApiService().getEvents(1)

        upcomingEventClient.enqueue(object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        showNotification(responseBody.listEvents[0].name, responseBody.listEvents[0].beginTime)
                        resultStatus = Result.success()
                    }
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                showNotification("Failed to get event ", t.message)
                resultStatus = Result.failure()
            }
        })

        return resultStatus as Result
    }

    private fun showNotification(title: String, description: String?) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_upcoming)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notification.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }
}