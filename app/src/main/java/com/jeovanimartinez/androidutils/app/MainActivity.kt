package com.jeovanimartinez.androidutils.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.jeovanimartinez.androidutils.moreapps.MoreAppsGPlay
import com.jeovanimartinez.androidutils.reviews.RateApp
import com.jeovanimartinez.androidutils.reviews.rateinapp.RateInApp
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

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
        rateInGooglePlayBtn.setOnClickListener{
            RateApp.goToRateInGooglePlay(this@MainActivity)
        }

        // Más aplicaciones
        moreAppsBtn.setOnClickListener {
            MoreAppsGPlay.showAppList(this@MainActivity)
        }

        initRateInAppBtn.setOnClickListener {
            RateInApp.rateWithInAppReviewApi(this@MainActivity)
            RateInApp.setShowAtEvent(2).setMinInstallLaunchTimes(3).setMinInstallElapsedDays(5).init(this@MainActivity)
        }

        checkAndShowRateInAppBtn.setOnClickListener {
            RateInApp.checkAndShow(this@MainActivity)
        }

    }

}