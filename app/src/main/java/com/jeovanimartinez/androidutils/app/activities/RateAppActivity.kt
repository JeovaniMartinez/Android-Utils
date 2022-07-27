package com.jeovanimartinez.androidutils.app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jeovanimartinez.androidutils.app.R
import com.jeovanimartinez.androidutils.app.databinding.ActivityRateAppBinding
import com.jeovanimartinez.androidutils.reviews.RateApp

/** RateAppActivity */
class RateAppActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRateAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRateAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appBar.btnBack.setOnClickListener { super.onBackPressed() }
        binding.appBar.tvTitle.text = getString(R.string.rate_app_title)

        rateInGooglePlaySetup()
        rateInAppSetup()
    }

    /** Rate In Google Play Setup */
    private fun rateInGooglePlaySetup() {
        binding.btnGoToRateInGooglePlay.setOnClickListener {
            RateApp.goToRateInGooglePlay(this)
        }
    }

    /** Rate In App Setup */
    private fun rateInAppSetup() {
        binding.tvMinInstallElapsedDays.text = RateApp.minInstallElapsedDays.toString()
        binding.tvMinInstallLaunchTimes.text = RateApp.minInstallLaunchTimes.toString()
        binding.tvMinRemindElapsedDays.text = RateApp.minRemindElapsedDays.toString()
        binding.tvMinRemindLaunchTimes.text = RateApp.minRemindLaunchTimes.toString()
        binding.tvShowAtEvent.text = RateApp.showAtEvent.toString()

        binding.btnCheckAndShow.setOnClickListener {
            RateApp.checkAndShow(this)
        }
    }

}
