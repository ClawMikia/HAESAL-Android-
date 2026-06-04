package com.haesal.batterymonitor.data.repository

import com.haesal.batterymonitor.data.database.BatteryHistoryDao
import com.haesal.batterymonitor.data.model.BatteryHistory
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit

data class BatteryInsights(
    val totalRecords: Int,
    val averageBatteryLevel: Float,
    val chargingSessionsCount: Int,
    val highestBatteryLevel: Int,
    val lowestBatteryLevel: Int,
    val averageTemperature: Float,
    val averageVoltage: Float,
    val mostCommonChargingSource: String
)

class BatteryRepository(private val dao: BatteryHistoryDao) {

    fun getAllHistory(): Flow<List<BatteryHistory>> = dao.getAllHistory()

    suspend fun insertHistory(history: BatteryHistory) {
        dao.insert(history)
    }

    suspend fun deleteOldRecords() {
        val cutoff = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7)
        dao.deleteOlderThan(cutoff)
    }

    suspend fun clearAllHistory() {
        dao.deleteAll()
    }

    suspend fun getInsights(): BatteryInsights {
        return BatteryInsights(
            totalRecords = dao.getCount(),
            averageBatteryLevel = dao.getAverageBatteryLevel() ?: 0f,
            chargingSessionsCount = dao.getChargingSessionsCount(),
            highestBatteryLevel = dao.getHighestBatteryLevel() ?: 0,
            lowestBatteryLevel = dao.getLowestBatteryLevel() ?: 0,
            averageTemperature = (dao.getAverageTemperature() ?: 0f) / 10f,
            averageVoltage = (dao.getAverageVoltage() ?: 0f) / 1000f,
            mostCommonChargingSource = dao.getMostCommonChargingSource() ?: "Unknown"
        )
    }
}
