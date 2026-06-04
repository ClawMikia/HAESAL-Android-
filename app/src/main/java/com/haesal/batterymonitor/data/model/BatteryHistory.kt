package com.haesal.batterymonitor.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "battery_history")
data class BatteryHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val batteryLevel: Int,
    val temperature: Float,
    val voltage: Int,
    val charging: Boolean,
    val chargingSource: String,
    val timestamp: Long = System.currentTimeMillis()
)
