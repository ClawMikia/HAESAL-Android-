package com.haesal.batterymonitor.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import com.haesal.batterymonitor.data.database.BatteryDatabase
import com.haesal.batterymonitor.data.model.BatteryHistory
import com.haesal.batterymonitor.data.model.BatteryStatus
import com.haesal.batterymonitor.data.model.ChargingSource
import com.haesal.batterymonitor.data.repository.BatteryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class BatteryReceiver : BroadcastReceiver() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BATTERY_CHANGED) return

        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100)
        val batteryPct = if (scale > 0) (level * 100 / scale) else 0

        val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL

        val plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
        val chargingSource = ChargingSource.fromPlugged(plugged)

        val health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, BatteryManager.BATTERY_HEALTH_UNKNOWN)
        val technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY) ?: "Unknown"
        val temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)
        val voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)

        val prefs = PreferencesManager(context)
        val isSolarMode = prefs.isSolarModeEnabled

        val batteryStatus = BatteryStatus(
            level = batteryPct,
            isCharging = isCharging,
            chargingSource = chargingSource,
            health = BatteryInfoFormatter.formatHealth(health),
            technology = technology,
            temperature = temperature.toFloat(),
            voltage = voltage,
            isSolarMode = isSolarMode
        )

        // Notify UI via application-level broadcast
        val updateIntent = Intent(ACTION_BATTERY_UPDATE).apply {
            putExtra(EXTRA_BATTERY_LEVEL, batteryStatus.level)
            putExtra(EXTRA_IS_CHARGING, batteryStatus.isCharging)
            putExtra(EXTRA_CHARGING_SOURCE, batteryStatus.chargingSource.name)
            putExtra(EXTRA_HEALTH, batteryStatus.health)
            putExtra(EXTRA_TECHNOLOGY, batteryStatus.technology)
            putExtra(EXTRA_TEMPERATURE, batteryStatus.temperature)
            putExtra(EXTRA_VOLTAGE, batteryStatus.voltage)
        }
        context.sendBroadcast(updateIntent)

        // Handle notifications
        if (prefs.isNotificationsEnabled) {
            val notifHelper = NotificationHelper(context)
            val lastLevel = prefs.lastBatteryLevel
            val wasCharging = prefs.wasCharging

            if (batteryPct == 100 && isCharging && lastLevel < 100) {
                if (isSolarMode) {
                    notifHelper.showSolarChargingNotification()
                } else {
                    notifHelper.showBatteryFullNotification()
                }
            }
            if (batteryPct < 20 && (lastLevel < 0 || lastLevel >= 20)) {
                notifHelper.showLowBatteryNotification(batteryPct)
            }
            if (isCharging && !wasCharging) {
                notifHelper.showChargingStartedNotification(batteryStatus.displayChargingSource)
            }
        }

        prefs.lastBatteryLevel = batteryPct
        prefs.wasCharging = isCharging

        // Save history
        if (prefs.isBatteryHistoryEnabled) {
            scope.launch {
                val db = BatteryDatabase.getInstance(context)
                val repo = BatteryRepository(db.batteryHistoryDao())
                repo.insertHistory(
                    BatteryHistory(
                        batteryLevel = batteryPct,
                        temperature = temperature.toFloat(),
                        voltage = voltage,
                        charging = isCharging,
                        chargingSource = chargingSource.displayName
                    )
                )
                repo.deleteOldRecords()
            }
        }
    }

    companion object {
        const val ACTION_BATTERY_UPDATE = "com.haesal.batterymonitor.BATTERY_UPDATE"
        const val EXTRA_BATTERY_LEVEL = "battery_level"
        const val EXTRA_IS_CHARGING = "is_charging"
        const val EXTRA_CHARGING_SOURCE = "charging_source"
        const val EXTRA_HEALTH = "health"
        const val EXTRA_TECHNOLOGY = "technology"
        const val EXTRA_TEMPERATURE = "temperature"
        const val EXTRA_VOLTAGE = "voltage"
    }
}
