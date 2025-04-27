package com.temalu.findfilm.presentation

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.temalu.findfilm.R
import com.androtema.local.data.entity.Film
import com.temalu.findfilm.databinding.ActivityMainBinding
import com.temalu.findfilm.presentation.fragments.DetailsFragment
import com.temalu.findfilm.presentation.fragments.DifferentFilmsFragment
import com.temalu.findfilm.presentation.fragments.FavoritesFragment
import com.temalu.findfilm.presentation.fragments.HomeFragment
import com.temalu.findfilm.presentation.fragments.LaterWatchFragment
import com.temalu.findfilm.presentation.fragments.SettingsFragment

class MainActivity : AppCompatActivity() {

    private var backPressedTime = 0L
    private lateinit var binding: ActivityMainBinding

    private val TAG_HOME_FRAGMENT = "home"
    private val TAG_FAVORITE_FRAGMENT = "favorites"
    private val TAG_SLECTIONS_FRAGMENT = "selections"
    private val TAG_LATER_FRAGMENT = "watch_later"
    private val TAG_SETTINGS_FRAGMENT = "settings"

    private var isProgrammaticSelection = false // Флаг для отслеживания программного изменения

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
        val bundle = Bundle()
        bundle.putParcelable("film", film)
        val fragment = DetailsFragment()
        fragment.arguments = bundle

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setBottomToast() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            if (isProgrammaticSelection) {
                return@setOnItemSelectedListener true
            }

            when (item.itemId) {
                R.id.favorites -> {
                    val fragment = checkFragmentExistence(TAG_FAVORITE_FRAGMENT)
                    changeFragment(fragment ?: FavoritesFragment(), TAG_FAVORITE_FRAGMENT)
                    true
                }

                R.id.watch_later -> {
                    val fragment = checkFragmentExistence(TAG_LATER_FRAGMENT)
                    changeFragment(fragment ?: DifferentFilmsFragment(), TAG_LATER_FRAGMENT)
                    true
                }

                R.id.selections -> {
                    val fragment = checkFragmentExistence(TAG_SLECTIONS_FRAGMENT)
                    changeFragment(fragment ?: LaterWatchFragment(), TAG_SLECTIONS_FRAGMENT)
                    true
                }

                R.id.home -> {
                    val fragment = checkFragmentExistence(TAG_HOME_FRAGMENT)
                    changeFragment(fragment ?: HomeFragment(), TAG_HOME_FRAGMENT)
                    true
                }

                R.id.settings -> {
                    val fragment = checkFragmentExistence(TAG_SETTINGS_FRAGMENT)
                    changeFragment(fragment ?: SettingsFragment(), TAG_SETTINGS_FRAGMENT)
                    true
                }

                else -> false
            }
        }
    }

    private fun updateBottomNavigationSelectedItem(fragmentTag: String) {
        isProgrammaticSelection = true // Указываем, что изменение программное
        when (fragmentTag) {
            TAG_HOME_FRAGMENT -> binding.bottomNavigation.selectedItemId = R.id.home
            TAG_FAVORITE_FRAGMENT -> binding.bottomNavigation.selectedItemId = R.id.favorites
            TAG_LATER_FRAGMENT -> binding.bottomNavigation.selectedItemId = R.id.watch_later
            TAG_SLECTIONS_FRAGMENT -> binding.bottomNavigation.selectedItemId = R.id.selections
            TAG_SETTINGS_FRAGMENT -> binding.bottomNavigation.selectedItemId = R.id.settings
        }
        isProgrammaticSelection = false // Сбрасываем флаг
    }

    private fun checkFragmentExistence(tag: String): Fragment? =
        supportFragmentManager.findFragmentByTag(tag)

    private fun changeFragment(fragment: Fragment, tag: String) {
        logBackStack()

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_placeholder)
        // Если это текущий фрагмент - ничего не делаем
        if (currentFragment?.tag == tag) {
            return
        }

        // Проверяем, есть ли фрагмент с таким же tag в back stack
        if (isFragmentInBackStack(tag)) {
            // Фрагмент уже есть в back stack, ничего не делаем
            supportFragmentManager.popBackStack(tag, 0)
            updateBottomNavigationSelectedItem(tag)
            return
        }

        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_placeholder, fragment, tag)
            .addToBackStack(tag)
            .commit()
        // Обновляем выбранный элемент в BottomNavigationView
        updateBottomNavigationSelectedItem(tag)
    }

    private fun isFragmentInBackStack(tag: String): Boolean {
        // Проверяем бэкстэк
        for (i in 0 until supportFragmentManager.backStackEntryCount) {
            if (supportFragmentManager.getBackStackEntryAt(i).name == tag) {
                return true
            }
        }
        return false
    }

    override fun onBackPressed() {
        logBackStack()

        if (supportFragmentManager.backStackEntryCount == 1) {
            if (TIME_INTERVAL > System.currentTimeMillis() - backPressedTime) {
                super.onBackPressedDispatcher.onBackPressed()
                finish()
            } else {
                backPressedTime = System.currentTimeMillis()
                Toast.makeText(this, "Чтобы выйти нажмите ещё раз", Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onBackPressedDispatcher.onBackPressed()

            // Обновляем выбранный элемент в BottomNavigationView
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_placeholder)
            if (currentFragment != null) {
                val fragmentTag = currentFragment.tag
                updateBottomNavigationSelectedItem(fragmentTag!!)
            }
        }
    }

    private fun logBackStack() {
        val backStackEntryCount = supportFragmentManager.backStackEntryCount
        Log.d("BACKSTACK", "Total fragments in back stack: $backStackEntryCount")
    }

    companion object {
        const val TIME_INTERVAL = 2000  //интервал времени для нажатия на back второй раз
        val favoritesList: MutableList<Film> = emptyList<Film>().toMutableList()
    }
}