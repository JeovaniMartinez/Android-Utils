package com.jeovanimartinez.androidutils.app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jeovanimartinez.androidutils.app.R
import com.jeovanimartinez.androidutils.app.databinding.ActivityRateAppBinding
import com.jeovanimartinez.androidutils.extensions.activity.configureTaskDescription
import com.jeovanimartinez.androidutils.extensions.context.getColorCompat
import com.jeovanimartinez.androidutils.extensions.view.onAnimationEnd
import com.jeovanimartinez.androidutils.reviews.RateApp

/** RateAppActivity */
class RateAppActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRateAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRateAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configureTaskDescription(R.string.app_name, R.mipmap.ic_launcher, getColorCompat(R.color.md_theme_background))

        binding.appBar.btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.appBar.tvTitle.text = getString(R.string.rate_app_title)

        rateInGooglePlaySetup()
        rateInAppSetup()
    }

    /** Rate In Google Play Setup */
    private fun rateInGooglePlaySetup() {
        binding.btnGoToRateInGooglePlay.setOnClickListener {
            RateApp.goToRateInGooglePlay(this@RateAppActivity)
        }
    }

    /** Rate In App Setup */
    private fun rateInAppSetup() {
        binding.tvMinInstallElapsedDays.text = RateApp.minInstallElapsedDays.toString()
        binding.tvMinInstallLaunchTimes.text = RateApp.minInstallLaunchTimes.toString()
        binding.tvMinRemindElapsedDays.text = RateApp.minRemindElapsedDays.toString()
        binding.tvMinRemindLaunchTimes.text = RateApp.minRemindLaunchTimes.toString()
        binding.tvShowAtEvent.text = RateApp.showAtEvent.toString()

        binding.tvCheckLogcat.alpha = 0f

        binding.btnCheckAndShow.setOnClickListener {

            binding.tvCheckLogcat.animate().alpha(1f).onAnimationEnd {
                binding.tvCheckLogcat.alpha = 0f
                binding.tvCheckLogcat.animate().alpha(1f).onAnimationEnd {
                    binding.tvCheckLogcat.alpha = 0f
                }
            }

            RateApp.checkAndShow(this@RateAppActivity)
        }
    }

}
