package com.example.minglethedogs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.minglethedogs.auth.IntroActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

         Handler(Looper.getMainLooper()).postDelayed({
             startActivity(Intent(this, IntroActivity::class.java))
             finish()
         },3000)
    }
}