package com.haesal.batterymonitor.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.haesal.batterymonitor.R

class ToastHelper {

    companion object {
        /**
         * Shows a dark-themed toast message
         * @param context The context to show the toast in
         * @param message The message to display
         * @param duration Duration of the toast (Toast.LENGTH_SHORT or Toast.LENGTH_LONG)
         */
        fun showToast(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
            // Create custom toast layout
            val layout: View = LayoutInflater.from(context).inflate(R.layout.toast_custom, null)
            val text: TextView = layout.findViewById(R.id.toast_message)
            text.text = message

            // Create toast
            val toast = Toast(context)
            toast.view = layout
            toast.duration = duration
            toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 100)

            // Show the toast
            toast.show()
        }
    }
}