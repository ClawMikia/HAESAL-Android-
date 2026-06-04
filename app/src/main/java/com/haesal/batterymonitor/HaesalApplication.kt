package com.haesal.batterymonitor

import android.app.Application
import com.haesal.batterymonitor.data.database.BatteryDatabase
import com.haesal.batterymonitor.data.repository.BatteryRepository
import com.haesal.batterymonitor.util.NotificationHelper
import com.haesal.batterymonitor.util.PreferencesManager

class HaesalApplication : Application() {

    val database: BatteryDatabase by lazy { BatteryDatabase.getInstance(this) }
    val repository: BatteryRepository by lazy { BatteryRepository(database.batteryHistoryDao()) }
    val preferencesManager: PreferencesManager by lazy { PreferencesManager(this) }
    val notificationHelper: NotificationHelper by lazy { NotificationHelper(this) }

    override fun onCreate() {
        super.onCreate()
        notificationHelper // Initialize notification channels
    }
}
