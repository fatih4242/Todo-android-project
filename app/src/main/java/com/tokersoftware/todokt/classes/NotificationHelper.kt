package com.tokersoftware.todokt.classes

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.tokersoftware.todokt.R
import com.tokersoftware.todokt.activity.main.view.MainActivity

class NotificationHelper(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        // Bildirim gönderme işlemleri burada gerçekleştirilir
        val sharedPref = applicationContext.getSharedPreferences(applicationContext.getString(R.string.sharedpreferences_key), Context.MODE_PRIVATE)
        if (sharedPref.getBoolean(applicationContext.getString(R.string.sharedpreferences_notification), true)){
            showNotification("WorkManager Bildirimi")
        }
        return Result.success()
    }

    private fun showNotification(message: String) {
        createNotificationChannel()

        val intent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(applicationContext, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)


        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(applicationContext.getString(R.string.you_have_something_to_do_today))
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = applicationContext.getString(R.string.you_have_something_to_do_today)
            val descriptionText = applicationContext.getString(R.string.you_have_something_to_do_today)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "work_manager_channel"
        private const val NOTIFICATION_ID = 1
    }




}
