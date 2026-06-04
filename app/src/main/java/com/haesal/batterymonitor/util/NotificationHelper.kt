package com.haesal.batterymonitor.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.haesal.batterymonitor.R
import com.haesal.batterymonitor.presentation.activity.MainActivity

class NotificationHelper(private val context: Context) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val batteryFullChannel = NotificationChannel(
                CHANNEL_BATTERY_FULL,
                "Battery Full",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifies when battery is fully charged"
            }

            val lowBatteryChannel = NotificationChannel(
                CHANNEL_LOW_BATTERY,
                "Low Battery",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifies when battery is critically low"
            }

            val chargingChannel = NotificationChannel(
                CHANNEL_CHARGING,
                "Charging Status",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Notifies when charging starts"
            }

            notificationManager.createNotificationChannels(
                listOf(batteryFullChannel, lowBatteryChannel, chargingChannel)
            )
        }
    }

    fun showBatteryFullNotification(isSolarMode: Boolean = false) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(context, CHANNEL_BATTERY_FULL)
            .setSmallIcon(R.drawable.ic_battery_full)
            .setContentTitle("⚡ HAESAL — Battery Full")
            .setContentText(if (isSolarMode) "Solar reactor at 100% capacity. Energy transfer complete." else "Solar core at 100% capacity. Charging complete.")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(NOTIF_BATTERY_FULL, notification)
    }
    
    fun showSolarChargingNotification() {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(context, CHANNEL_CHARGING)
            .setSmallIcon(R.drawable.ic_solar_rays)
            .setContentTitle("☀ HAESAL — Solar Charging Complete")
            .setContentText("Solar reactor efficiency maximized. Battery fully charged.")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(NOTIF_SOLAR_CHARGING, notification)
    }

    fun showLowBatteryNotification(level: Int) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(context, CHANNEL_LOW_BATTERY)
            .setSmallIcon(R.drawable.ic_battery_low)
            .setContentTitle("⚠ HAESAL — Low Battery Alert")
            .setContentText("Reactor energy critical — $level% remaining.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(NOTIF_LOW_BATTERY, notification)
    }

    fun showChargingStartedNotification(source: String) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(context, CHANNEL_CHARGING)
            .setSmallIcon(R.drawable.ic_charging)
            .setContentTitle("☀ HAESAL — Charging Started")
            .setContentText("Solar reactor online — Source: $source")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(NOTIF_CHARGING, notification)
    }

    companion object {
        const val CHANNEL_BATTERY_FULL = "battery_full_channel"
        const val CHANNEL_LOW_BATTERY = "low_battery_channel"
        const val CHANNEL_CHARGING = "charging_channel"
        const val NOTIF_BATTERY_FULL = 1001
        const val NOTIF_LOW_BATTERY = 1002
        const val NOTIF_CHARGING = 1003
        const val NOTIF_SOLAR_CHARGING = 1004
    }
}
