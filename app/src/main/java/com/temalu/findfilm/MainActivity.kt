package com.temalu.findfilm

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.AppBarLayout
import com.temalu.findfilm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //создаём объект класса activity_main
        binding = ActivityMainBinding.inflate(layoutInflater)
        //смотрим корневой элемент в activity_main
        setContentView(binding.root)


        //кликСлушатель topAppBar
        binding.topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> {
                    Toast.makeText(this, "Настройки", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.favorites -> {
                    Toast.makeText(this, "Избранное", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.watch_later -> {
                    Toast.makeText(this, "Посмотреть похже", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.selections -> {
                    Toast.makeText(this, "Подборки", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }

        }


        /*val button1: Button = findViewById(R.id.button1)
        button1.setOnClickListener{
            Toast.makeText(applicationContext, "Меню", Toast.LENGTH_SHORT).show()
        }

        val button2: Button = findViewById(R.id.button2)
        button2.setOnClickListener{
            Toast.makeText(applicationContext, "Избранное", Toast.LENGTH_SHORT).show()
        }

        val button3: Button = findViewById(R.id.button3)
        button3.setOnClickListener{
            Toast.makeText(applicationContext, "Посмотреть позже", Toast.LENGTH_SHORT).show()
        }

        val button4: Button = findViewById(R.id.button4)
        button4.setOnClickListener{
            Toast.makeText(applicationContext, "Подборки", Toast.LENGTH_SHORT).show()
        }

        val button5: Button = findViewById(R.id.button5)
        button5.setOnClickListener{
            Toast.makeText(applicationContext, "Настройки", Toast.LENGTH_SHORT).show()
        }*/ //кликер кнопок и toast
    }
}