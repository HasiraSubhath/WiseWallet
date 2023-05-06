package com.github.deviants.wisewallet.domain.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReminderNotificationReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val service = ReminderNotificationService(context)
        service.showNotification()
    }
}