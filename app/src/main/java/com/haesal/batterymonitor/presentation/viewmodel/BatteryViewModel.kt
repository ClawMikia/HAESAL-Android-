package com.haesal.batterymonitor.presentation.viewmodel

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.haesal.batterymonitor.HaesalApplication
import com.haesal.batterymonitor.data.model.BatteryHistory
import com.haesal.batterymonitor.data.model.BatteryStatus
import com.haesal.batterymonitor.data.model.ChargingSource
import com.haesal.batterymonitor.data.repository.BatteryInsights
import com.haesal.batterymonitor.util.BatteryInfoFormatter
import com.haesal.batterymonitor.util.BatteryReceiver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BatteryViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as HaesalApplication
    private val repository = app.repository
    private val prefs = app.preferencesManager

    private val _batteryStatus = MutableStateFlow(BatteryStatus())
    val batteryStatus: StateFlow<BatteryStatus> = _batteryStatus.asStateFlow()

    private val _historyList = MutableStateFlow<List<BatteryHistory>>(emptyList())
    val historyList: StateFlow<List<BatteryHistory>> = _historyList.asStateFlow()

    private val _insights = MutableStateFlow<BatteryInsights?>(null)
    val insights: StateFlow<BatteryInsights?> = _insights.asStateFlow()

    private val _isSolarMode = MutableStateFlow(prefs.isSolarModeEnabled)
    val isSolarMode: StateFlow<Boolean> = _isSolarMode.asStateFlow()

    private val _isBatteryHistoryEnabled = MutableStateFlow(prefs.isBatteryHistoryEnabled)
    val isBatteryHistoryEnabled: StateFlow<Boolean> = _isBatteryHistoryEnabled.asStateFlow()

    private val _isNotificationsEnabled = MutableStateFlow(prefs.isNotificationsEnabled)
    val isNotificationsEnabled: StateFlow<Boolean> = _isNotificationsEnabled.asStateFlow()

    private val batteryUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val level = intent.getIntExtra(BatteryReceiver.EXTRA_BATTERY_LEVEL, 0)
            val isCharging = intent.getBooleanExtra(BatteryReceiver.EXTRA_IS_CHARGING, false)
            val sourceStr = intent.getStringExtra(BatteryReceiver.EXTRA_CHARGING_SOURCE) ?: "UNKNOWN"
            val health = intent.getStringExtra(BatteryReceiver.EXTRA_HEALTH) ?: "Unknown"
            val tech = intent.getStringExtra(BatteryReceiver.EXTRA_TECHNOLOGY) ?: "Unknown"
            val temp = intent.getFloatExtra(BatteryReceiver.EXTRA_TEMPERATURE, 0f)
            val voltage = intent.getIntExtra(BatteryReceiver.EXTRA_VOLTAGE, 0)
            val source = try { ChargingSource.valueOf(sourceStr) } catch (e: Exception) { ChargingSource.UNKNOWN }

            _batteryStatus.value = BatteryStatus(
                level = level,
                isCharging = isCharging,
                chargingSource = source,
                health = health,
                technology = tech,
                temperature = temp,
                voltage = voltage,
                isSolarMode = prefs.isSolarModeEnabled
            )
        }
    }

    init {
        loadCurrentBatteryStatus()
        observeHistory()
        registerBatteryReceiver()
    }

    private fun registerBatteryReceiver() {
        val context = getApplication<Application>()
        val filter = IntentFilter(BatteryReceiver.ACTION_BATTERY_UPDATE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(batteryUpdateReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            @Suppress("UnspecifiedRegisterReceiverFlag")
            context.registerReceiver(batteryUpdateReceiver, filter)
        }
    }

    private fun loadCurrentBatteryStatus() {
        val context = getApplication<Application>()
        val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        intent?.let {
            val level = it.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = it.getIntExtra(BatteryManager.EXTRA_SCALE, 100)
            val batteryPct = if (scale > 0) (level * 100 / scale) else 0
            val status = it.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL
            val plugged = it.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
            val chargingSource = ChargingSource.fromPlugged(plugged)
            val health = it.getIntExtra(BatteryManager.EXTRA_HEALTH, BatteryManager.BATTERY_HEALTH_UNKNOWN)
            val tech = it.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY) ?: "Unknown"
            val temp = it.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)
            val voltage = it.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)

            _batteryStatus.value = BatteryStatus(
                level = batteryPct,
                isCharging = isCharging,
                chargingSource = chargingSource,
                health = BatteryInfoFormatter.formatHealth(health),
                technology = tech,
                temperature = temp.toFloat(),
                voltage = voltage,
                isSolarMode = prefs.isSolarModeEnabled
            )
        }
    }

    private fun observeHistory() {
        viewModelScope.launch {
            repository.getAllHistory().collect { list ->
                _historyList.value = list
            }
        }
    }

    fun refreshBatteryStatus() {
        loadCurrentBatteryStatus()
    }

    fun setSolarMode(enabled: Boolean) {
        prefs.isSolarModeEnabled = enabled
        _isSolarMode.value = enabled
        _batteryStatus.value = _batteryStatus.value.copy(isSolarMode = enabled)
    }

    fun setBatteryHistoryEnabled(enabled: Boolean) {
        prefs.isBatteryHistoryEnabled = enabled
        _isBatteryHistoryEnabled.value = enabled
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        prefs.isNotificationsEnabled = enabled
        _isNotificationsEnabled.value = enabled
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearAllHistory()
        }
    }

    fun loadInsights() {
        viewModelScope.launch {
            _insights.value = repository.getInsights()
        }
    }

    override fun onCleared() {
        super.onCleared()
        try {
            getApplication<Application>().unregisterReceiver(batteryUpdateReceiver)
        } catch (e: Exception) {
            // Ignore if not registered
        }
    }
}
