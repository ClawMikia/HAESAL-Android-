package com.haesal.batterymonitor.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.haesal.batterymonitor.data.model.BatteryHistory

@Database(
    entities = [BatteryHistory::class],
    version = 1,
    exportSchema = false
)
abstract class BatteryDatabase : RoomDatabase() {

    abstract fun batteryHistoryDao(): BatteryHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: BatteryDatabase? = null

        fun getInstance(context: Context): BatteryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BatteryDatabase::class.java,
                    "haesal_battery_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
