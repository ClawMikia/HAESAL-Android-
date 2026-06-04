package com.haesal.batterymonitor.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.haesal.batterymonitor.R

class SplashActivity : AppCompatActivity() {
    private val SPLASH_DELAY: Long = 3000 // 3 seconds to allow animations to complete

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Load animations
        val iconAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_fade_in)
        val quoteAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_quote_fade)

        // Apply animations to views
        findViewById<android.widget.ImageView>(R.id.app_icon).startAnimation(iconAnimation)
        findViewById<android.widget.TextView>(R.id.quote_text).startAnimation(quoteAnimation)
        findViewById<android.widget.TextView>(R.id.author_text).startAnimation(quoteAnimation)

        // Handler to delay the transition to MainActivity
        Handler(Looper.getMainLooper()).postDelayed({
            // Apply fade-out animation before transitioning
            val fadeOut = AnimationUtils.loadAnimation(this, R.anim.splash_fade_out)
            val rootView = findViewById<android.view.ViewGroup>(android.R.id.content).getChildAt(0) as android.view.ViewGroup
            rootView.startAnimation(fadeOut)

            // Set a listener for the animation to complete before transitioning
            fadeOut.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                    // Override transition animation
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }
            })
        }, SPLASH_DELAY)
    }

    // Prevent going back to SplashActivity by pressing back button
    override fun onBackPressed() {
        // Do nothing
    }
}
