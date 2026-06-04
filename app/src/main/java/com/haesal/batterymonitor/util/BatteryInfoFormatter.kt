package com.haesal.batterymonitor.util

import android.os.BatteryManager

object BatteryInfoFormatter {

    fun formatHealth(health: Int): String = when (health) {
        BatteryManager.BATTERY_HEALTH_GOOD -> "Good"
        BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Overheat"
        BatteryManager.BATTERY_HEALTH_DEAD -> "Dead"
        BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "Over Voltage"
        BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> "Failure"
        BatteryManager.BATTERY_HEALTH_COLD -> "Cold"
        else -> "Unknown"
    }

    fun formatTemperature(raw: Int): String {
        val celsius = raw / 10f
        return String.format("%.1f°C", celsius)
    }

    fun formatVoltage(millivolts: Int): String {
        val volts = millivolts / 1000f
        return String.format("%.2f V", volts)
    }

    fun formatChargingState(isCharging: Boolean): String =
        if (isCharging) "Charging" else "Discharging"

    fun formatTimestamp(timestamp: Long): String {
        val sdf = java.text.SimpleDateFormat("MMM dd, HH:mm", java.util.Locale.getDefault())
        return sdf.format(java.util.Date(timestamp))
    }
}
