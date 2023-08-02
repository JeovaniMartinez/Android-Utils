package com.jeovanimartinez.androidutils.app.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jeovanimartinez.androidutils.app.R
import com.jeovanimartinez.androidutils.app.constants.Preferences
import com.jeovanimartinez.androidutils.app.databinding.ActivityRateAppBinding
import com.jeovanimartinez.androidutils.extensions.activity.configureTaskDescription
import com.jeovanimartinez.androidutils.extensions.context.getColorCompat
import com.jeovanimartinez.androidutils.extensions.view.onAnimationEnd
import com.jeovanimartinez.androidutils.reviews.RateApp

/** RateAppActivity */
class RateAppActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRateAppBinding
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRateAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configureTaskDescription(R.string.app_name, R.mipmap.ic_launcher, getColorCompat(R.color.md_theme_background))

        binding.appBar.btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.appBar.tvTitle.text = getString(R.string.rate_app_title)

        preferences = getSharedPreferences(Preferences.RATE_APP_PREFERENCES_FILE, Context.MODE_PRIVATE)

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
    @SuppressLint("ApplySharedPref")
    private fun rateInAppSetup() {

        // Configuration general

        binding.layoutRateInAppMainContent.visibility = View.VISIBLE
        binding.layoutRateInAppEditConfig.visibility = View.GONE

        binding.tvMinInstallElapsedDays.text = RateApp.minInstallElapsedDays.toString()
        binding.tvMinInstallLaunchTimes.text = RateApp.minInstallLaunchTimes.toString()
        binding.tvMinRemindElapsedDays.text = RateApp.minRemindElapsedDays.toString()
        binding.tvMinRemindLaunchTimes.text = RateApp.minRemindLaunchTimes.toString()
        binding.tvShowAtEvent.text = RateApp.showAtEvent.toString()

        binding.tvCheckLogcat.alpha = 0f

        // Buttons actions

        binding.btnEditConfiguration.setOnClickListener {
            binding.layoutRateInAppMainContent.visibility = View.GONE
            binding.layoutRateInAppEditConfig.visibility = View.VISIBLE
            binding.etMinInstallElapsedDays.setText(RateApp.minInstallElapsedDays.toString())
            binding.etMinInstallLaunchTimes.setText(RateApp.minInstallLaunchTimes.toString())
            binding.etMinRemindElapsedDays.setText(RateApp.minRemindElapsedDays.toString())
            binding.etMinRemindLaunchTimes.setText(RateApp.minRemindLaunchTimes.toString())
            binding.etShowAtEvent.setText(RateApp.showAtEvent.toString())
        }

        binding.btnCheckAndShow.setOnClickListener {
            binding.tvCheckLogcat.animate().alpha(1f).onAnimationEnd {
                binding.tvCheckLogcat.alpha = 0f
                binding.tvCheckLogcat.animate().alpha(1f).onAnimationEnd {
                    binding.tvCheckLogcat.alpha = 0f
                }
            }
            RateApp.checkAndShow(this@RateAppActivity)
        }

        binding.btnEditConfigurationCancel.setOnClickListener {
            binding.layoutRateInAppMainContent.visibility = View.VISIBLE
            binding.layoutRateInAppEditConfig.visibility = View.GONE
        }

        binding.btnEditConfigurationSave.setOnClickListener {

            // Declare variables and initial values
            val minInstallElapsedDays: Int
            val minInstallLaunchTimes: Int
            val minRemindElapsedDays: Int
            val minRemindLaunchTimes: Int
            val showAtEvent: Int

            // Convert and validate the EditText values
            try {
                minInstallElapsedDays = binding.etMinInstallElapsedDays.text.toString().toInt()
                minInstallLaunchTimes = binding.etMinInstallLaunchTimes.text.toString().toInt()
                minRemindElapsedDays = binding.etMinRemindElapsedDays.text.toString().toInt()
                minRemindLaunchTimes = binding.etMinRemindLaunchTimes.text.toString().toInt()
                showAtEvent = binding.etShowAtEvent.text.toString().toInt()

                if (minInstallElapsedDays < 0 || minInstallLaunchTimes < 1 || minRemindElapsedDays < 0 || minRemindLaunchTimes < 1 || showAtEvent < 1) {
                    throw IllegalArgumentException("Invalid config value")
                }

            } catch (err: Exception) {
                MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.rate_app_incorrect_values_title)
                    .setMessage(R.string.rate_app_incorrect_values_msg)
                    .setPositiveButton(R.string.ok, null)
                    .show()
                return@setOnClickListener
            }

            // The values are saved, and a confirmation message is shown

            with(preferences.edit()) {
                putInt(Preferences.RATE_APP_MIN_INSTALL_ELAPSED_DAYS, minInstallElapsedDays)
                putInt(Preferences.RATE_APP_MIN_INSTALL_LAUNCH_TIMES, minInstallLaunchTimes)
                putInt(Preferences.RATE_APP_MIN_REMIND_ELAPSED_DAYS, minRemindElapsedDays)
                putInt(Preferences.RATE_APP_MIN_REMIND_LAUNCH_TIMES, minRemindLaunchTimes)
                putInt(Preferences.RATE_APP_SHOW_AT_EVENT, showAtEvent)
                apply()
            }

            MaterialAlertDialogBuilder(this)
                .setMessage(R.string.rate_app_edit_configuration_done_msg)
                .setPositiveButton(R.string.ok) { _, _ ->
                    binding.layoutRateInAppMainContent.visibility = View.VISIBLE
                    binding.layoutRateInAppEditConfig.visibility = View.GONE
                }
                .setNeutralButton(R.string.rate_app_restart_app_now) { _, _ ->
                    val generalPreferences = getSharedPreferences(Preferences.GENERAL_PREFERENCES_FILE, Context.MODE_PRIVATE)
                    generalPreferences.edit().putBoolean(Preferences.LAUNCH_RATE_APP_ACTIVITY_ON_START, true).commit()
                    triggerRestart(this@RateAppActivity)
                }
                .setCancelable(false)
                .show()

        }
    }

    /** Restart the app */
    private fun triggerRestart(context: Activity) {
        // Reference: https://gist.github.com/easterapps/7127ce0749cfce2edf083e55b6eecec5
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        @Suppress("USELESS_IS_CHECK")
        if (context is Activity) {
            @Suppress("USELESS_CAST")
            (context as Activity).finish()
        }
        Runtime.getRuntime().exit(0)
    }

}
