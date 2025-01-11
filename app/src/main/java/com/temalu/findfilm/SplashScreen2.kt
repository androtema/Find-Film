package com.temalu.findfilm

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

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