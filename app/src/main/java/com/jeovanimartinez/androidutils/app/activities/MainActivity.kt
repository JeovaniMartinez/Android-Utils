package com.jeovanimartinez.androidutils.app.activities

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.jeovanimartinez.androidutils.app.R
import com.jeovanimartinez.androidutils.app.constants.Preferences
import com.jeovanimartinez.androidutils.app.databinding.ActivityMainBinding
import com.jeovanimartinez.androidutils.extensions.activity.configureTaskDescription
import com.jeovanimartinez.androidutils.extensions.context.getColorCompat
import com.jeovanimartinez.androidutils.web.SystemWebBrowser

/** MainActivity */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configureTaskDescription(R.string.app_name, R.mipmap.ic_launcher, getColorCompat(R.color.colorBackground))

        initialSetup()

    }

    /** Initial setup */
    private fun initialSetup() {

        binding.btnWebSite.setOnClickListener {
            SystemWebBrowser.openUrl(this, "https://jeovanimartinez.github.io/Android-Utils/")
        }

        binding.btnGitHub.setOnClickListener {
            SystemWebBrowser.openUrl(this, "https://github.com/JeovaniMartinez/Android-Utils")
        }

        binding.btnToggleTheme.setOnClickListener {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                getSharedPreferences(Preferences.THEME_PREFERENCES_FILE, Context.MODE_PRIVATE).edit().putBoolean(Preferences.THEME_DARK_THEME_ENABLED, false).apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                getSharedPreferences(Preferences.THEME_PREFERENCES_FILE, Context.MODE_PRIVATE).edit().putBoolean(Preferences.THEME_DARK_THEME_ENABLED, true).apply()
            }
        }

    }

}
