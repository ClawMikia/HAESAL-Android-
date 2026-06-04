package com.haesal.batterymonitor.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.haesal.batterymonitor.data.model.BatteryHistory
import com.haesal.batterymonitor.databinding.ItemBatteryHistoryBinding
import com.haesal.batterymonitor.util.BatteryInfoFormatter

class BatteryHistoryAdapter :
    ListAdapter<BatteryHistory, BatteryHistoryAdapter.HistoryViewHolder>(DIFF_CALLBACK) {

    inner class HistoryViewHolder(private val binding: ItemBatteryHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(history: BatteryHistory) {
            binding.tvTimestamp.text = BatteryInfoFormatter.formatTimestamp(history.timestamp)
            binding.tvBatteryLevel.text = "${history.batteryLevel}%"
            binding.tvTemperature.text = BatteryInfoFormatter.formatTemperature(history.temperature.toInt())
            binding.tvVoltage.text = BatteryInfoFormatter.formatVoltage(history.voltage)
            binding.tvChargingSource.text = if (history.charging) history.chargingSource else "Discharging"
            binding.tvChargingState.text = if (history.charging) "⚡ CHARGING" else "○ IDLE"

            val levelColor = when {
                history.batteryLevel >= 80 -> 0xFF00E676.toInt()
                history.batteryLevel >= 40 -> 0xFFFFD54F.toInt()
                else -> 0xFFFF5252.toInt()
            }
            binding.tvBatteryLevel.setTextColor(levelColor)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemBatteryHistoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BatteryHistory>() {
            override fun areItemsTheSame(oldItem: BatteryHistory, newItem: BatteryHistory) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: BatteryHistory, newItem: BatteryHistory) =
                oldItem == newItem
        }
    }
}
