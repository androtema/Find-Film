package com.temalu.findfilm.receivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationCompat

class BatteryReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_BATTERY_LOW -> {
                Toast.makeText(
                    context,
                    "Battery is low",
                    Toast.LENGTH_SHORT
                ).show()
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                showBatteryNotification(context)
            }

            Intent.ACTION_BATTERY_OKAY -> {
                Toast.makeText(
                    context,
                    "Battery is okay",
                    Toast.LENGTH_SHORT
                ).show()
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }

            Intent.ACTION_POWER_CONNECTED -> {
                Toast.makeText(
                    context,
                    "Battery is charged",
                    Toast.LENGTH_SHORT
                ).show()
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun showBatteryNotification(context: Context?) {
        val notificationManager = (context?.getSystemService(Context.NOTIFICATION_SERVICE))
                as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                "battery_channel",
                "Find Film",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val notification = NotificationCompat.Builder(context, "battery_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Find Film")
            .setContentText(
                "Your battery is low.\n" +
                        "Please charge your device for further viewing. "
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(1, notification)
    }
}
