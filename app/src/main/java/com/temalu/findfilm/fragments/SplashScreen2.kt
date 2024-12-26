package com.temalu.findfilm.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.temalu.findfilm.MainActivity
import com.temalu.findfilm.R

class SplashScreen2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen2)

        Handler().postDelayed({
            val intent = Intent(this@SplashScreen2, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}