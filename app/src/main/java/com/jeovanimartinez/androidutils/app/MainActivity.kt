package com.jeovanimartinez.androidutils.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.jeovanimartinez.androidutils.moreapps.MoreAppsGPlay
import com.jeovanimartinez.androidutils.reviews.RateApp
import com.jeovanimartinez.androidutils.reviews.rateinapp.RateInApp
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var rateInAppConfigured = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initSetup()
    }

    private fun initSetup() {

        // Alternar los temas
        toggleThemeBtn.setOnClickListener {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }

        // Calificar en google play
        rateInGooglePlayBtn.setOnClickListener {
            RateApp.goToRateInGooglePlay(this@MainActivity)
        }

        // Más aplicaciones
        moreAppsBtn.setOnClickListener {
            MoreAppsGPlay.showAppList(this@MainActivity)
        }

        initRateInAppBtn.setOnClickListener {
            RateInApp
                .setMinInstallElapsedDays(0)
                .setMinInstallLaunchTimes(1)
                .setMinRemindElapsedDays(0)
                .setMinRemindLaunchTimes(1)
                .setShowAtEvent(1)
                .init(this@MainActivity)

            rateInAppConfigured = true
        }

        checkAndShowRateInAppBtn.setOnClickListener {
            if (!rateInAppConfigured) {
                Toast.makeText(this@MainActivity, "Please initialize RateInApp before to click this button", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            RateInApp.checkAndShow(this@MainActivity)
        }

    }

}