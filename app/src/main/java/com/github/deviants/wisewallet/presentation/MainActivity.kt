package com.github.deviants.wisewallet.presentation

import android.app.*
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.github.deviants.wisewallet.R
import com.github.deviants.wisewallet.databinding.ActivityMainBinding
import com.github.deviants.wisewallet.domain.notification.ReminderNotificationReceiver
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_list,
                R.id.navigation_chart,
                R.id.navigation_settings,
                R.id.accountsFragment,
                R.id.goalsFragment,
                R.id.utilsFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.navView.apply {

            setupWithNavController(navController)
            background = null
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.addItemFragment -> hideBottomNav(View.GONE)
                R.id.itemDetailFragment -> hideBottomNav(View.INVISIBLE)
                else -> showBottomNav()
            }
        }
        setNotification()
    }


    private fun showBottomNav() {
        binding.bottomAppBar.visibility = View.VISIBLE
        //supportActionBar?.hide()
    }

    private fun hideBottomNav(visibility: Int) {
        binding.bottomAppBar.visibility = visibility
        //supportActionBar?.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


    fun setNotification() {

        val alarmIntent = Intent(this, ReminderNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE)

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager


        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar[Calendar.HOUR_OF_DAY] = 20
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

    }

}