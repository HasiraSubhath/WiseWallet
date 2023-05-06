package com.github.deviants.wisewallet.presentation

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.github.deviants.wisewallet.R
import com.github.deviants.wisewallet.data.transaction.TransactionRoomDatabase
import com.github.deviants.wisewallet.domain.notification.ReminderNotificationService

class ExpensesApplication : Application() {
    val database: TransactionRoomDatabase by lazy { TransactionRoomDatabase.getDatabase(this)}


    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            ReminderNotificationService.REMINDER_CHANNEL_ID,
            getString(R.string.notification_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = getString(R.string.notification_desction)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

}
