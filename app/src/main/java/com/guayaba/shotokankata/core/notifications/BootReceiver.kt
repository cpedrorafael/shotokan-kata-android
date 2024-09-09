package com.guayaba.shotokankata.core.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.guayaba.shotokankata.ui.MainActivity

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val mainActivity = MainActivity()
            mainActivity.setDailyNotification()
        }
    }
}
