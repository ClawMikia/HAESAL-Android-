package com.haesal.batterymonitor.presentation.activity

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.haesal.batterymonitor.R
import com.haesal.batterymonitor.data.model.BatteryStatus
import com.haesal.batterymonitor.databinding.ActivityMainBinding
import com.haesal.batterymonitor.presentation.viewmodel.BatteryViewModel
import com.haesal.batterymonitor.util.BatteryInfoFormatter
import com.haesal.batterymonitor.util.BatteryReceiver
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: BatteryViewModel by viewModels()
    private lateinit var batteryReceiver: BatteryReceiver

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* Permission result handled silently */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestNotificationPermission()
        setupAnimations()
        setupClickListeners()
        observeViewModel()

        batteryReceiver = BatteryReceiver()
        registerReceiver(batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        
        // Register receiver for solar mode changes
        solarModeReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == "com.haesal.batterymonitor.SOLAR_MODE_CHANGED") {
                    viewModel.refreshBatteryStatus()
                }
            }
        }
        
        // Fix for newer Android versions - specify RECEIVER_EXPORTED
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(solarModeReceiver, IntentFilter("com.haesal.batterymonitor.SOLAR_MODE_CHANGED"), Context.RECEIVER_EXPORTED)
        } else {
            registerReceiver(solarModeReceiver, IntentFilter("com.haesal.batterymonitor.SOLAR_MODE_CHANGED"))
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun setupAnimations() {
        val pulseAnim = AnimationUtils.loadAnimation(this, R.anim.pulse_animation)
        val rotateAnim = AnimationUtils.loadAnimation(this, R.anim.sun_ray_rotation)
        val scanAnim = AnimationUtils.loadAnimation(this, R.anim.energy_scan)
        val reactorAnim = AnimationUtils.loadAnimation(this, R.anim.reactor_pulse)

        binding.ivReactorCore.startAnimation(pulseAnim)
        binding.ivSolarRays.startAnimation(rotateAnim)
        binding.viewScanLine.startAnimation(scanAnim)
        binding.ivReactorOuter.startAnimation(reactorAnim)
    }

    private fun setupClickListeners() {
        binding.fabRefresh.setOnClickListener {
            viewModel.refreshBatteryStatus()
        }

        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_history -> {
                    startActivity(Intent(this, HistoryActivity::class.java))
                    true
                }
                R.id.navigation_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.batteryStatus.collect { status ->
                updateUI(status)
            }
        }
    }

    private fun updateUI(status: BatteryStatus) {
        binding.tvBatteryPercentage.text = "${status.level}%"
        binding.tvChargingState.text = BatteryInfoFormatter.formatChargingState(status.isCharging)
        binding.tvChargingSource.text = "Source: ${status.displayChargingSource}"
        binding.tvTemperature.text = BatteryInfoFormatter.formatTemperature(status.temperature.toInt())
        binding.tvVoltage.text = BatteryInfoFormatter.formatVoltage(status.voltage)
        binding.tvHealth.text = status.health
        binding.tvTechnology.text = status.technology

        val solarText = if (status.isSolarMode) "☀ SOLAR MODE ACTIVE" else "○ SOLAR MODE OFF"
        binding.tvSolarStatus.text = solarText
        
        // Update Solar UI
        if (status.isSolarMode) {
            binding.tvSolarEfficiency.text = "Efficiency: ${status.solarEfficiency}%"
            binding.tvSolarLux.text = "Light Intensity: ${status.lux.toInt()} Lux"
            binding.tvSolarAiInsight.text = BatteryInfoFormatter.getSolarAiInsight(status.lux)
            binding.tvSolarAiInsight.visibility = android.view.View.VISIBLE
            
            // Adjust animation speed based on efficiency
            val duration = when {
                status.solarEfficiency > 80 -> 1000L
                status.solarEfficiency > 50 -> 2000L
                status.solarEfficiency > 20 -> 4000L
                else -> 8000L
            }
            binding.ivSolarRays.animation?.duration = duration
        } else {
            binding.tvSolarEfficiency.text = "Efficiency: 0%"
            binding.tvSolarLux.text = "Solar Reactor Offline"
            binding.tvSolarAiInsight.visibility = android.view.View.GONE
            binding.ivSolarRays.animation?.duration = 10000L
        }

        // Color battery percentage
        val percentColor = when {
            status.level >= 80 -> ContextCompat.getColor(this, R.color.success)
            status.level >= 40 -> ContextCompat.getColor(this, R.color.primaryEnergy)
            else -> ContextCompat.getColor(this, R.color.warning)
        }
        binding.tvBatteryPercentage.setTextColor(percentColor)

        // Charging indicator
        val chargingIcon = if (status.isCharging) R.drawable.ic_charging else R.drawable.ic_battery_idle
        binding.ivChargingIcon.setImageResource(chargingIcon)

        binding.progressBatteryLevel.progress = status.level

        // Solar mode glow
        if (status.isSolarMode && status.isCharging) {
            binding.cardSolarReactor.alpha = 1f
        } else {
            binding.cardSolarReactor.alpha = 0.5f
        }
    }

    private var solarModeReceiver: android.content.BroadcastReceiver? = null
    
    override fun onDestroy() {
        super.onDestroy()
        try { unregisterReceiver(batteryReceiver) } catch (e: Exception) { /* ignore */ }
        try { solarModeReceiver?.let { unregisterReceiver(it) } } catch (e: Exception) { /* ignore */ }
    }
}
