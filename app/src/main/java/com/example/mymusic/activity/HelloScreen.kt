package com.example.mymusic.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.mymusic.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HelloScreen : AppCompatActivity() {
    private lateinit var animation: Animation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello_screen)
        val logo = findViewById<ImageView>(R.id.logomain)
        if (intent.getBooleanExtra("EXIT", false)) {
            finish()
        } else {
            overridePendingTransition(R.anim.anim_intent_in, R.anim.anim_intent_out)
            animation = AnimationUtils.loadAnimation(this, R.anim.anim_intent_in_main)

            Handler(Looper.getMainLooper()).postDelayed({
                logo.visibility = View.VISIBLE
                logo.startAnimation(animation)
            }, 2500)

            Handler(Looper.getMainLooper()).postDelayed({
                val mainIntent = Intent(this, LoginActivity::class.java)
                startActivity(mainIntent)
                finish()
            }, 5000)
        }
    }
}