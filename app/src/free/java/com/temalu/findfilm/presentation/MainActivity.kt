package com.temalu.findfilm.presentation

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.androtema.local.data.entity.Film
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.temalu.findfilm.R
import com.temalu.findfilm.databinding.ActivityMainBinding
import com.temalu.findfilm.presentation.fragments.DetailsFragment
import com.temalu.findfilm.presentation.fragments.HomeFragment
import com.temalu.findfilm.presentation.fragments.SettingsFragment
import com.temalu.findfilm.receivers.BatteryReceiver
import java.util.concurrent.TimeUnit
import androidx.core.content.edit
import com.temalu.findfilm.presentation.fragments.DifferentFilmsFragment
import com.temalu.findfilm.presentation.fragments.FavoritesFragment
import com.temalu.findfilm.presentation.fragments.LaterWatchFragment

private const val TIME_TOAST_MENU_NAV = 800
private const val TIME_INTERVAL_CLICK_ON_BACK_FOR_EXIT = 2000
private const val TIME_PRO_FREE_PERIOD = 10

class MainActivity : AppCompatActivity() {

    private var backPressedTime = 0L
    private lateinit var binding: ActivityMainBinding

    private val TAG_HOME_FRAGMENT = "home"
    private val TAG_SETTINGS_FRAGMENT = "settings"
    private val TAG_FAVORITE_FRAGMENT = "favorites"
    private val TAG_SLECTIONS_FRAGMENT = "selections"
    private val TAG_LATER_FRAGMENT = "watch_later"

    private var isProgrammaticSelection = false // Флаг для отслеживания программного изменения

    override fun onCreate(savedInstanceState: Bundle?) {
        //"разрешение" использовать экран целиком (включая up/down menu navigation)
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (isTrialPeriodActive()) {
            reactivateProVersion()
        } else {
            showCustomToast(this, "Пробный период истек", 1500)
            setBottomToastFree()
            // Здесь можно предложить покупку или перенаправить на free версию в магазин приложений
        }

        //запускаем стартовый фрагмент
        changeFragment(HomeFragment(), TAG_HOME_FRAGMENT)
        startSavedFilmInNotification()

        val receiver = BatteryReceiver()
        val filter = IntentFilter(Intent.ACTION_BATTERY_LOW)
        filter.addAction(Intent.ACTION_POWER_CONNECTED)
        filter.addAction(Intent.ACTION_BATTERY_OKAY)
        registerReceiver(receiver, filter)

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                //Если не удастся получить токен, то логируем причину и выходим из слушателя
                if (!task.isSuccessful) {
                    Log.d("MainActivity", "Fetching FCM registration token failed", task.exception);
                    return@OnCompleteListener
                }
                //Если получилось, то логируем токен
                Log.i("MainActivity", task.result!!)
            })
    }

    private fun startSavedFilmInNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("film", Film::class.java)?.let { film ->
                launchDetailsFragment(film)
            }
        } else {
            intent.getParcelableExtra<Film>("film")?.let { film ->
                launchDetailsFragment(film)
            }
        }
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

    private fun setBottomToastFree() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            if (isProgrammaticSelection) {
                return@setOnItemSelectedListener true
            }
            when (item.itemId) {
                R.id.favorites -> {
                    showCustomToast(this, "Доступно в Pro версии", TIME_TOAST_MENU_NAV)
                    false
                }

                R.id.watch_later -> {
                    showCustomToast(this, "Доступно в Pro версии", TIME_TOAST_MENU_NAV)
                    false
                }

                R.id.selections -> {
                    showCustomToast(this, "Доступно в Pro версии", TIME_TOAST_MENU_NAV)
                    false
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
            if (TIME_INTERVAL_CLICK_ON_BACK_FOR_EXIT > System.currentTimeMillis() - backPressedTime) {
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

    fun showCustomToast(context: Context, message: String, durationMs: Int) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
        toast.show()
        Handler(Looper.getMainLooper()).postDelayed({ toast.cancel() }, durationMs.toLong())
    }

    internal fun reactivateProVersion(){
        getSharedPreferences(
            "trial_prefs",
            MODE_PRIVATE
        ).edit { putLong("first_launch_time", System.currentTimeMillis()) }

        Toast.makeText(
            this,
            "Pro версия (пробный период $TIME_PRO_FREE_PERIOD секунд)",
            Toast.LENGTH_SHORT
        ).show()
        setBottomToast()
    }

    private fun isTrialPeriodActive(): Boolean {
        val prefs = getSharedPreferences("trial_prefs", MODE_PRIVATE)
        val firstLaunchTime = prefs.getLong("first_launch_time", 0L)

        // Если это первый запуск, сохраняем время
        if (firstLaunchTime == 0L) {
            prefs.edit { putLong("first_launch_time", System.currentTimeMillis()) }
            return true
        }

        val trialDuration = TimeUnit.SECONDS.toMillis(TIME_PRO_FREE_PERIOD.toLong())
        val currentTime = System.currentTimeMillis()
        return (currentTime - firstLaunchTime) <= trialDuration
    }

    companion object {
        val favoritesList: MutableList<Film> = emptyList<Film>().toMutableList()
    }
}