package com.haesal.batterymonitor.presentation.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.haesal.batterymonitor.R
import com.haesal.batterymonitor.databinding.ActivityHistoryBinding
import com.haesal.batterymonitor.presentation.adapter.BatteryHistoryAdapter
import com.haesal.batterymonitor.presentation.viewmodel.BatteryViewModel
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private val viewModel: BatteryViewModel by viewModels()
    private lateinit var adapter: BatteryHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = "⚡ BATTERY HISTORY"
            setDisplayHomeAsUpEnabled(true)
        }

        setupRecyclerView()
        observeViewModel()
        setupListeners()
        viewModel.loadInsights()
    }

    private fun setupRecyclerView() {
        adapter = BatteryHistoryAdapter()
        binding.rvBatteryHistory.layoutManager = LinearLayoutManager(this)
        binding.rvBatteryHistory.adapter = adapter
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.historyList.collect { list ->
                adapter.submitList(list)
                binding.tvEmptyHistory.visibility =
                    if (list.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
            }
        }
        lifecycleScope.launch {
            viewModel.insights.collect { insights ->
                insights?.let {
                    binding.tvTotalRecords.text = "Total Records: ${it.totalRecords}"
                    binding.tvAvgLevel.text = "Avg Level: ${String.format("%.1f", it.averageBatteryLevel)}%"
                    binding.tvChargingSessions.text = "Charging Sessions: ${it.chargingSessionsCount}"
                    binding.tvHighestLevel.text = "Highest: ${it.highestBatteryLevel}%"
                    binding.tvLowestLevel.text = "Lowest: ${it.lowestBatteryLevel}%"
                    binding.tvAvgTemp.text = "Avg Temp: ${String.format("%.1f", it.averageTemperature)}°C"
                    binding.tvAvgVoltage.text = "Avg Voltage: ${String.format("%.2f", it.averageVoltage)}V"
                    binding.tvMostCommonSource.text = "Common Source: ${it.mostCommonChargingSource}"
                }
            }
        }
    }

    private fun setupListeners() {
        binding.btnClearHistory.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Clear History")
                .setMessage("Delete all battery history records?")
                .setPositiveButton("Delete") { _, _ ->
                    viewModel.clearHistory()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
