package com.temalu.findfilm

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.temalu.findfilm.databinding.ActivityMainBinding
import com.temalu.findfilm.fragments.DetailsFragment
import com.temalu.findfilm.fragments.DifferentFilmsFragment
import com.temalu.findfilm.fragments.FavoritesFragment
import com.temalu.findfilm.fragments.HomeFragment
import com.temalu.findfilm.fragments.LaterWatchFragment
import com.temalu.findfilm.fragments.SplashScreen2

class MainActivity : AppCompatActivity() {

    private var backPressedTime = 0L
    private lateinit var binding: ActivityMainBinding

    private val TAG_HOME_FRAGMENT = "home"
    private val TAG_FAVORITE_FRAGMENT = "favorites"
    private val TAG_SLECTIONS_FRAGMENT = "selections"
    private val TAG_LATER_FRAGMENT = "watch_later"

    override fun onCreate(savedInstanceState: Bundle?) {
        //"разрешение" использовать экран целиком (включая up/down menu navigation)
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        /*val intent = Intent(
            this,
            SplashScreen2::class.java
        )
        startActivity(intent)*/


        //создаём объект класса activity_main
        binding = ActivityMainBinding.inflate(layoutInflater)

        //смотрим корневой элемент в activity_main
        setContentView(binding.root)

        //настройка кнопок меню навигации
        setBottomToast()

        //запускаем стартовый фрагмент
        changeFragment(HomeFragment(), TAG_HOME_FRAGMENT)
    }

    fun launchDetailsFragment(film: Film) {
        //Создаем "посылку"
        val bundle = Bundle()
        //Кладем наш фильм в "посылку"
        bundle.putParcelable("film", film)
        //Кладем фрагмент с деталями в перменную
        val fragment = DetailsFragment()
        //Прикрепляем нашу "посылку" к фрагменту
        fragment.arguments = bundle

        //Запускаем фрагмент
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setBottomToast() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.favorites -> {
                    val tag = TAG_FAVORITE_FRAGMENT
                    val fragment = checkFragmentExistence(tag)
                    changeFragment(fragment ?: FavoritesFragment(), tag)
                    Toast.makeText(this, "Избранное", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.watch_later -> {
                    val tag = TAG_LATER_FRAGMENT
                    val fragment = checkFragmentExistence(tag)
                    changeFragment(fragment ?: DifferentFilmsFragment(), tag)
                    Toast.makeText(this, "Посмотреть похже", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.selections -> {
                    val tag = TAG_SLECTIONS_FRAGMENT
                    val fragment = checkFragmentExistence(tag)
                    changeFragment(fragment ?: LaterWatchFragment(), tag)
                    Toast.makeText(this, "Подборки", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.home -> {
                    val tag = TAG_HOME_FRAGMENT
                    val fragment = checkFragmentExistence(tag)
                    changeFragment(fragment ?: HomeFragment(), tag)
                    Toast.makeText(this, "Главное меню", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }
        }
    }

    //Ищем фрагмент по тегу, если он есть то возвращаем его, если нет, то null
    private fun checkFragmentExistence(tag: String): Fragment? =
        supportFragmentManager.findFragmentByTag(tag)

    //запуск фрагмента
    private fun changeFragment(fragment: Fragment, tag: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, fragment, tag)
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            if (backPressedTime + TIME_INTERVAL > System.currentTimeMillis()) {
                super.onBackPressedDispatcher.onBackPressed()
                finish()
            } else {
                Toast.makeText(this, "Чтобы выйти нажмите ещё раз", Toast.LENGTH_SHORT).show()
            }
            backPressedTime = System.currentTimeMillis()
        } else {
            super.onBackPressedDispatcher.onBackPressed()
        }
    }


    companion object {
        const val TIME_INTERVAL = 2000  //интервал времени для нажатия на back второй раз
        val favoritesList: MutableList<Film> = emptyList<Film>().toMutableList()
    }
}