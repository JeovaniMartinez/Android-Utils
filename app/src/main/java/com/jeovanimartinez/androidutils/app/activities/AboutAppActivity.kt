package com.jeovanimartinez.androidutils.app.activities

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.jeovanimartinez.androidutils.about.AboutApp
import com.jeovanimartinez.androidutils.about.config.AboutAppConfig
import com.jeovanimartinez.androidutils.activity.config.TaskDescriptionConfig
import com.jeovanimartinez.androidutils.app.BuildConfig
import com.jeovanimartinez.androidutils.app.R
import com.jeovanimartinez.androidutils.app.constants.Preferences
import com.jeovanimartinez.androidutils.app.databinding.ActivityAboutAppBinding
import com.jeovanimartinez.androidutils.extensions.activity.configureTaskDescription
import com.jeovanimartinez.androidutils.extensions.context.getColorCompat

/** AboutAppActivity */
class AboutAppActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutAppBinding
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configureTaskDescription(R.string.app_name, R.mipmap.ic_launcher, getColorCompat(R.color.md_theme_background))

        binding.appBar.btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.appBar.tvTitle.text = getString(R.string.about_app_title)

        preferences = getSharedPreferences(Preferences.GENERAL_PREFERENCES_FILE, Context.MODE_PRIVATE)

        generalSetup()
    }

    /** General setup */
    private fun generalSetup() {

        binding.btnToggleTheme.setOnClickListener {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                preferences.edit().putBoolean(Preferences.DARK_THEME_ENABLED, false).apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                preferences.edit().putBoolean(Preferences.DARK_THEME_ENABLED, true).apply()
            }
        }

        binding.btnShowAboutAppActivity.setOnClickListener {
            showAboutAppActivity()
        }

    }

    /** Shows the about app activity from the library */
    private fun showAboutAppActivity() {

        val aboutAppConfig = AboutAppConfig(
            appLogo = R.drawable.library_logo,
            appName = R.string.about_app_default_app_name,
            appVersion = BuildConfig.VERSION_NAME,
            creditsText = R.string.about_app_default_credits_text,
            creditsUrl = null,
            authorLogo = R.drawable.logo_jedemm_com,
            authorUrl = null,
            copyrightHolderName = "",
            termsAndPrivacyPolicyUrl = "https://jedemm.com/android-utils/terms-and-policy/license.html",
            helpUrl = null,
            contactEmail = null,
            showOpenSourceLicenses = true,
            style = null,
            TaskDescriptionConfig(R.string.app_name, R.mipmap.ic_launcher, getColorCompat(R.color.md_theme_background))
        )

        AboutApp.show(this@AboutAppActivity, aboutAppConfig)

    }

}
