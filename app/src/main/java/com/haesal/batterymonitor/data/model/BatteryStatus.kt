package com.haesal.batterymonitor.data.model

data class BatteryStatus(
    val level: Int = 0,
    val isCharging: Boolean = false,
    val chargingSource: ChargingSource = ChargingSource.UNKNOWN,
    val health: String = "Unknown",
    val technology: String = "Unknown",
    val temperature: Float = 0f,
    val voltage: Int = 0,
    val isSolarMode: Boolean = false
) {
    val temperatureCelsius: Float get() = temperature / 10f
    val voltageVolts: Float get() = voltage / 1000f
    val displayChargingSource: String get() = if (isSolarMode && isCharging) "Solar" else chargingSource.displayName
}
