package com.krissphi.id.dicoding_events_mid_submission_app.ui.settings

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.krissphi.id.dicoding_events_mid_submission_app.R
import com.krissphi.id.dicoding_events_mid_submission_app.data.response.EventsResponse
import com.krissphi.id.dicoding_events_mid_submission_app.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.CountDownLatch

class ReminderWorkManager(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val reminderPref = ReminderPreferences.getInstance(applicationContext.reminderDataStore)
        val isReminderActive = runBlocking { reminderPref.getReminderSetting().first() }

        if (!isReminderActive) {
            return Result.success()
        }

        val latch = CountDownLatch(1)
        var result = Result.failure()

        getNearestEvents { success ->
            result = if (success) Result.success() else Result.retry()
            latch.countDown()
        }

        latch.await()
        return result
    }

    private fun getNearestEvents(callback: (Boolean) -> Unit) {
        val client = ApiConfig.getApiService().getListEvents(-1, null, 1)
        client.enqueue(object : Callback<EventsResponse> {
            override fun onResponse(
                call: Call<EventsResponse>,
                response: Response<EventsResponse>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()?.listEvents
                    if (!data.isNullOrEmpty()) {
                        val nearestEvent = data[0]
                        showNotification(
                            "Event Reminder: ${nearestEvent.name ?: "Upcoming Event"}",
                            "Date: ${nearestEvent.beginTime ?: "Check the app for details"}"
                        )
                        callback(true)
                    } else {
                        callback(false)
                    }
                } else {
                    callback(false)
                }
            }

            override fun onFailure(call: Call<EventsResponse>, t: Throwable) {
                callback(false)
            }
        })
    }

    private fun showNotification(title: String, description: String?) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_notifications_active_24)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notification.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }

    companion object {
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "channel_01"
        const val CHANNEL_NAME = "dicoding channel"
    }
}