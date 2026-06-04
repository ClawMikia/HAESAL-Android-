package com.haesal.batterymonitor.presentation.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.haesal.batterymonitor.databinding.ActivitySettingsBinding
import com.haesal.batterymonitor.presentation.viewmodel.BatteryViewModel
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: BatteryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = "⚙ HAESAL SETTINGS"
            setDisplayHomeAsUpEnabled(true)
        }

        observeViewModel()
        setupListeners()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.isSolarMode.collect { enabled ->
                binding.switchSolarMode.isChecked = enabled
            }
        }
        lifecycleScope.launch {
            viewModel.isBatteryHistoryEnabled.collect { enabled ->
                binding.switchBatteryHistory.isChecked = enabled
            }
        }
        lifecycleScope.launch {
            viewModel.isNotificationsEnabled.collect { enabled ->
                binding.switchNotifications.isChecked = enabled
            }
        }
    }

    private fun setupListeners() {
        binding.switchSolarMode.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setSolarMode(isChecked)
            // Send broadcast to MainActivity to refresh UI
            sendBroadcast(Intent("com.haesal.batterymonitor.SOLAR_MODE_CHANGED").putExtra("solar_mode", isChecked))
        }
        binding.switchBatteryHistory.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setBatteryHistoryEnabled(isChecked)
        }
        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setNotificationsEnabled(isChecked)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
