package com.haesal.batterymonitor.data.database

import androidx.room.*
import com.haesal.batterymonitor.data.model.BatteryHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface BatteryHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: BatteryHistory)

    @Query("SELECT * FROM battery_history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<BatteryHistory>>

    @Query("SELECT * FROM battery_history ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentHistory(limit: Int): List<BatteryHistory>

    @Query("DELETE FROM battery_history WHERE timestamp < :cutoff")
    suspend fun deleteOlderThan(cutoff: Long)

    @Query("DELETE FROM battery_history")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM battery_history")
    suspend fun getCount(): Int

    @Query("SELECT AVG(batteryLevel) FROM battery_history")
    suspend fun getAverageBatteryLevel(): Float?

    @Query("SELECT COUNT(*) FROM battery_history WHERE charging = 1")
    suspend fun getChargingSessionsCount(): Int

    @Query("SELECT MAX(batteryLevel) FROM battery_history")
    suspend fun getHighestBatteryLevel(): Int?

    @Query("SELECT MIN(batteryLevel) FROM battery_history")
    suspend fun getLowestBatteryLevel(): Int?

    @Query("SELECT AVG(temperature) FROM battery_history")
    suspend fun getAverageTemperature(): Float?

    @Query("SELECT AVG(voltage) FROM battery_history")
    suspend fun getAverageVoltage(): Float?

    @Query("SELECT chargingSource FROM battery_history GROUP BY chargingSource ORDER BY COUNT(*) DESC LIMIT 1")
    suspend fun getMostCommonChargingSource(): String?
}
