package com.temalu.findfilm.receivers

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import com.androtema.local.data.entity.Film
import com.temalu.findfilm.presentation.notifications.NotificationConstants
import com.temalu.findfilm.presentation.notifications.NotificationRememberFilm

class LaterTimeReceiver : BroadcastReceiver() {
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context?, intent: Intent?) {

        val bundle = intent?.getBundleExtra(NotificationConstants.FILM_BUNDLE_KEY)
        val film: Film = bundle?.get(NotificationConstants.FILM_KEY) as Film

        NotificationRememberFilm.createNotification(context!!, film)
    }
}