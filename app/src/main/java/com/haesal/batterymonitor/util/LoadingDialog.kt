package com.haesal.batterymonitor.util

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import com.haesal.batterymonitor.databinding.DialogLoadingBinding

class LoadingDialog {

    companion object {
        private var dialog: Dialog? = null

        /**
         * Shows a loading dialog with dark theme
         * @param context The context to show the dialog in
         * @param message The message to display (optional)
         */
        fun show(context: Context, message: String = "Loading...") {
            if (dialog != null && dialog!!.isShowing) {
                return
            }

            val binding = DialogLoadingBinding.inflate(LayoutInflater.from(context))
            dialog = Dialog(context)
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setContentView(binding.root)
            dialog?.setCancelable(false)
            dialog?.setCanceledOnTouchOutside(false)

            // Set message
            binding.tvLoadingMessage.text = message

            // Show the dialog
            dialog?.show()
        }

        /**
         * Hides the loading dialog if it's showing
         */
        fun hide() {
            dialog?.dismiss()
            dialog = null
        }
    }
}