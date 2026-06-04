package com.haesal.batterymonitor.util

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("haesal_prefs", Context.MODE_PRIVATE)

    var isSolarModeEnabled: Boolean
        get() = prefs.getBoolean(KEY_SOLAR_MODE, false)
        set(value) = prefs.edit().putBoolean(KEY_SOLAR_MODE, value).apply()

    var isBatteryHistoryEnabled: Boolean
        get() = prefs.getBoolean(KEY_BATTERY_HISTORY, true)
        set(value) = prefs.edit().putBoolean(KEY_BATTERY_HISTORY, value).apply()

    var isNotificationsEnabled: Boolean
        get() = prefs.getBoolean(KEY_NOTIFICATIONS, true)
        set(value) = prefs.edit().putBoolean(KEY_NOTIFICATIONS, value).apply()

    var lastBatteryLevel: Int
        get() = prefs.getInt(KEY_LAST_BATTERY_LEVEL, -1)
        set(value) = prefs.edit().putInt(KEY_LAST_BATTERY_LEVEL, value).apply()

    var wasCharging: Boolean
        get() = prefs.getBoolean(KEY_WAS_CHARGING, false)
        set(value) = prefs.edit().putBoolean(KEY_WAS_CHARGING, value).apply()

    companion object {
        private const val KEY_SOLAR_MODE = "solar_mode"
        private const val KEY_BATTERY_HISTORY = "battery_history"
        private const val KEY_NOTIFICATIONS = "notifications"
        private const val KEY_LAST_BATTERY_LEVEL = "last_battery_level"
        private const val KEY_WAS_CHARGING = "was_charging"
    }
}
