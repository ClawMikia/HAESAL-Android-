package com.haesal.batterymonitor.data.model

enum class ChargingSource(val displayName: String) {
    AC("AC"),
    USB("USB"),
    WIRELESS("Wireless"),
    UNKNOWN("Unknown"),
    SOLAR("Solar");

    companion object {
        fun fromPlugged(plugged: Int): ChargingSource {
            return when (plugged) {
                android.os.BatteryManager.BATTERY_PLUGGED_AC -> AC
                android.os.BatteryManager.BATTERY_PLUGGED_USB -> USB
                android.os.BatteryManager.BATTERY_PLUGGED_WIRELESS -> WIRELESS
                else -> UNKNOWN
            }
        }
    }
}
