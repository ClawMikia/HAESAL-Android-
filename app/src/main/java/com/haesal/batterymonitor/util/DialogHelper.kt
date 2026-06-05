package com.haesal.batterymonitor.util

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.haesal.batterymonitor.databinding.DialogConfirmBinding

class DialogHelper {

    companion object {
        /**
         * Shows a confirmation dialog with dark theme
         * @param context The context to show the dialog in
         * @param title The title of the dialog
         * @param message The message to display
         * @param onPositive Callback when positive button is clicked
         * @param onNegative Callback when negative button is clicked (optional)
         */
        fun showConfirmDialog(
            context: Context,
            title: String,
            message: String,
            onPositive: () -> Unit,
            onNegative: (() -> Unit)? = null
        ) {
            val binding = DialogConfirmBinding.inflate(LayoutInflater.from(context))
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(binding.root)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)

            dialog.window?.apply {
                setBackgroundDrawableResource(android.R.color.transparent)
                setLayout(
                    (context.resources.displayMetrics.widthPixels * 0.85).toInt(),
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }

            // Set dialog content
            binding.dialogTitle.text = title
            binding.dialogMessage.text = message

            // Set button listeners
            binding.dialogButtonPositive.setOnClickListener {
                onPositive()
                dialog.dismiss()
            }

            binding.dialogButtonNegative.setOnClickListener {
                onNegative?.invoke()
                dialog.dismiss()
            }

            // Show the dialog
            dialog.show()
        }

        /**
         * Shows a message dialog with dark theme
         * @param context The context to show the dialog in
         * @param title The title of the dialog
         * @param message The message to display
         * @param onDismiss Callback when dialog is dismissed (optional)
         */
        fun showMessageDialog(
            context: Context,
            title: String,
            message: String,
            onDismiss: (() -> Unit)? = null
        ) {
            val binding = DialogConfirmBinding.inflate(LayoutInflater.from(context))
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(binding.root)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)

            dialog.window?.apply {
                setBackgroundDrawableResource(android.R.color.transparent)
                setLayout(
                    (context.resources.displayMetrics.widthPixels * 0.85).toInt(),
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }

            // Set dialog content
            binding.dialogTitle.text = title
            binding.dialogMessage.text = message

            // Hide negative button and adjust positive button
            binding.dialogButtonNegative.visibility = android.view.View.GONE
            binding.dialogButtonPositive.text = "OK"

            // Set button listener
            binding.dialogButtonPositive.setOnClickListener {
                onDismiss?.invoke()
                dialog.dismiss()
            }

            // Show the dialog
            dialog.show()
        }
    }
}