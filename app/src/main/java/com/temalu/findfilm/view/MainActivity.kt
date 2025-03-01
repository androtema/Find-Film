package com.temalu.findfilm.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.temalu.findfilm.view.fragments.DetailsFragment
import com.temalu.findfilm.view.fragments.DifferentFilmsFragment
import com.temalu.findfilm.view.fragments.FavoritesFragment
import com.temalu.findfilm.view.fragments.HomeFragment
import com.temalu.findfilm.view.fragments.LaterWatchFragment
import com.temalu.findfilm.R
import com.temalu.findfilm.databinding.ActivityMainBinding
import com.temalu.findfilm.domain.Film
import com.temalu.findfilm.view.fragments.SettingsFragment

class MainActivity : AppCompatActivity() {

    private var backPressedTime = 0L
    private lateinit var binding: ActivityMainBinding

    private val TAG_HOME_FRAGMENT = "home"
    private val TAG_FAVORITE_FRAGMENT = "favorites"
    private val TAG_SLECTIONS_FRAGMENT = "selections"
    private val TAG_LATER_FRAGMENT = "watch_later"
    private val TAG_SETTINGS_FRAGMENT = "settings"

    override fun onCreate(savedInstanceState: Bundle?) {
        //"разрешение" использовать экран целиком (включая up/down menu navigation)
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
                    val fragment = checkFragmentExistence(TAG_FAVORITE_FRAGMENT)
                    changeFragment(fragment ?: FavoritesFragment(), TAG_FAVORITE_FRAGMENT)
                    Toast.makeText(this, "Избранное", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.watch_later -> {
                    val fragment = checkFragmentExistence(TAG_LATER_FRAGMENT)
                    changeFragment(fragment ?: DifferentFilmsFragment(), TAG_LATER_FRAGMENT)
                    Toast.makeText(this, "Посмотреть похже", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.selections -> {
                    val fragment = checkFragmentExistence(TAG_SLECTIONS_FRAGMENT)
                    changeFragment(fragment ?: LaterWatchFragment(), TAG_SLECTIONS_FRAGMENT)
                    Toast.makeText(this, "Подборки", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.home -> {
                    val fragment = checkFragmentExistence(TAG_HOME_FRAGMENT)
                    changeFragment(fragment ?: HomeFragment(), TAG_HOME_FRAGMENT)
                    Toast.makeText(this, "Главное меню", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.settings -> {
                    val fragment = checkFragmentExistence(TAG_SETTINGS_FRAGMENT)
                    changeFragment( fragment?: SettingsFragment(), TAG_SETTINGS_FRAGMENT)
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
        } else {
            super.onBackPressedDispatcher.onBackPressed()
        }
    }
    
    companion object {
        const val TIME_INTERVAL = 2000  //интервал времени для нажатия на back второй раз
        val favoritesList: MutableList<Film> = emptyList<Film>().toMutableList()
    }
}