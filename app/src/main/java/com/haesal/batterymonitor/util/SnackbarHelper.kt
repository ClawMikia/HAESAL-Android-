package com.haesal.batterymonitor.util

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.haesal.batterymonitor.R

class SnackbarHelper {

    companion object {
        /**
         * Shows a dark-themed snackbar message
         * @param view The view to anchor the snackbar to
         * @param message The message to display
         * @param duration Duration of the snackbar (Snackbar.LENGTH_SHORT, Snackbar.LENGTH_LONG, or Snackbar.LENGTH_INDEFINITE)
         */
        fun showSnackbar(view: View, message: String, duration: Int = Snackbar.LENGTH_SHORT) {
            val snackbar = Snackbar.make(view, message, duration)

            // Customize the snackbar to match our dark theme
            val snackbarView = snackbar.view
            snackbarView.setBackgroundColor(view.context.resources.getColor(R.color.surface))

            // Set text color
            val textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as android.widget.TextView
            textView.setTextColor(view.context.resources.getColor(R.color.textPrimary))
            textView.textSize = 14f
            textView.typeface = android.graphics.Typeface.MONOSPACE

            // Set action button color if needed
            val actionView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_action) as android.widget.TextView
            actionView.setTextColor(view.context.resources.getColor(R.color.primaryEnergy))
            actionView.textSize = 14f
            actionView.typeface = android.graphics.Typeface.MONOSPACE

            // Show the snackbar
            snackbar.show()
        }

        /**
         * Shows a dark-themed snackbar with an action button
         * @param view The view to anchor the snackbar to
         * @param message The message to display
         * @param actionText The text for the action button
         * @param actionListener The listener for the action button
         * @param duration Duration of the snackbar (Snackbar.LENGTH_SHORT, Snackbar.LENGTH_LONG, or Snackbar.LENGTH_INDEFINITE)
         */
        fun showSnackbarWithAction(
            view: View, 
            message: String, 
            actionText: String, 
            actionListener: View.OnClickListener, 
            duration: Int = Snackbar.LENGTH_LONG
        ) {
            val snackbar = Snackbar.make(view, message, duration)
            snackbar.setAction(actionText, actionListener)

            // Customize the snackbar to match our dark theme
            val snackbarView = snackbar.view
            snackbarView.setBackgroundColor(view.context.resources.getColor(R.color.surface))

            // Set text color
            val textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as android.widget.TextView
            textView.setTextColor(view.context.resources.getColor(R.color.textPrimary))
            textView.textSize = 14f
            textView.typeface = android.graphics.Typeface.MONOSPACE

            // Set action button color
            val actionView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_action) as android.widget.TextView
            actionView.setTextColor(view.context.resources.getColor(R.color.primaryEnergy))
            actionView.textSize = 14f
            actionView.typeface = android.graphics.Typeface.MONOSPACE

            // Show the snackbar
            snackbar.show()
        }
    }
}