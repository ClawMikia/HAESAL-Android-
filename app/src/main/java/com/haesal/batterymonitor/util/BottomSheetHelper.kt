package com.haesal.batterymonitor.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.haesal.batterymonitor.R
import com.haesal.batterymonitor.databinding.BottomSheetOptionsBinding

class BottomSheetHelper {

    companion object {
        /**
         * Shows a dark-themed bottom sheet with options
         * @param context The context to show the bottom sheet in
         * @param title The title of the bottom sheet
         * @param options List of option texts
         * @param onOptionSelected Callback when an option is selected
         */
        fun showOptionsBottomSheet(
            context: Context,
            title: String,
            options: List<String>,
            onOptionSelected: (Int) -> Unit
        ) {
            val binding = BottomSheetOptionsBinding.inflate(LayoutInflater.from(context))
            val bottomSheet = BottomSheetDialog(context)
            bottomSheet.setContentView(binding.root)

            // Set title
            binding.bottomSheetTitle.text = title

            // Set options
            binding.optionsRecyclerView.adapter = OptionsAdapter(options) { position ->
                onOptionSelected(position)
                bottomSheet.dismiss()
            }

            // Show the bottom sheet
            bottomSheet.show()
        }
    }
}

// Simple adapter for bottom sheet options
class OptionsAdapter(
    private val options: List<String>,
    private val onOptionSelected: (Int) -> Unit
) : androidx.recyclerview.widget.RecyclerView.Adapter<OptionsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        private val textView: android.widget.TextView = itemView.findViewById(R.id.option_text)

        fun bind(text: String, position: Int) {
            textView.text = text
            itemView.setOnClickListener { onOptionSelected(position) }
        }
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_option, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(options[position], position)
    }

    override fun getItemCount(): Int = options.size
}