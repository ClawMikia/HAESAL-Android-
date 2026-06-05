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

    fun getSolarAiInsight(lux: Float): String {
        return when {
            lux < 10 -> "REACTOR STALLED: Absolute darkness detected. Move to light source."
            lux < 500 -> "LOW YIELD: Ambient light insufficient for reactor ignition."
            lux < 5000 -> "STABLE: Gathering energy from local photons. Efficiency moderate."
            lux < 20000 -> "OPTIMAL: Solar rays detected. Reactor core reaching peak state."
            else -> "OVERLOAD: Direct solar impact! Energy absorption at maximum capacity."
        }
    }
}
