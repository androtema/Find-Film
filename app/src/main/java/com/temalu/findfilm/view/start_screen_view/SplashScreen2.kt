package com.temalu.findfilm.view.start_screen_view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.temalu.findfilm.R
import com.temalu.findfilm.view.MainActivity

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