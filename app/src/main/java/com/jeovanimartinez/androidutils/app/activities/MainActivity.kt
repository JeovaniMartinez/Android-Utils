package com.jeovanimartinez.androidutils.app.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.jeovanimartinez.androidutils.app.BuildConfig
import com.jeovanimartinez.androidutils.app.R
import com.jeovanimartinez.androidutils.app.constants.Preferences
import com.jeovanimartinez.androidutils.app.databinding.ActivityMainBinding
import com.jeovanimartinez.androidutils.extensions.activity.configureTaskDescription
import com.jeovanimartinez.androidutils.extensions.context.getColorCompat
import com.jeovanimartinez.androidutils.extensions.context.shortToast
import com.jeovanimartinez.androidutils.web.SystemWebBrowser

/** MainActivity */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen() // Required for show the splash screen
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configureTaskDescription(R.string.app_name, R.mipmap.ic_launcher, getColorCompat(R.color.md_theme_background))

        preferences = getSharedPreferences(Preferences.GENERAL_PREFERENCES_FILE, Context.MODE_PRIVATE)

        initialSetup()
        libraryUtilitiesMenuSetup()

    }

    /** Initial setup */
    private fun initialSetup() {

        binding.btnWebSite.setOnClickListener {
            SystemWebBrowser.openUrl(this, "https://jeovanimartinez.github.io/Android-Utils/")
        }

        binding.btnGitHub.setOnClickListener {
            SystemWebBrowser.openUrl(this, "https://github.com/JeovaniMartinez/Android-Utils")
        }

        binding.btnInfo.setOnClickListener {
            showInfoDialog()
        }

        binding.btnToggleTheme.setOnClickListener {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                preferences.edit().putBoolean(Preferences.DARK_THEME_ENABLED, false).apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                preferences.edit().putBoolean(Preferences.DARK_THEME_ENABLED, true).apply()
            }
        }

        binding.btnGoToUtility.setOnClickListener {

            // The default option is watermark, position 1
            when (preferences.getInt(Preferences.UTILITIES_MENU_SELECTED_INDEX, 1)) {
                0 -> {
                    startActivity(Intent(this, RateAppActivity::class.java))
                }

                1 -> {
                    startActivity(Intent(this, WatermarkActivity::class.java))
                }

                2 -> {
                    shortToast("View To Image")
                }

                3 -> {
                    shortToast("About App")
                }

                4 -> {
                    shortToast("Premium App")
                }
            }
        }

        if (!preferences.getBoolean(Preferences.INFO_DIALOG_SHOWN_ON_APP_FIRST_LAUNCH, false)) {
            preferences.edit().putBoolean(Preferences.INFO_DIALOG_SHOWN_ON_APP_FIRST_LAUNCH, true).apply()
            showInfoDialog()
        }

    }

    /** Library Utilities Menu Setup */
    private fun libraryUtilitiesMenuSetup() {

        val libraryUtilitiesList = resources.getStringArray(R.array.library_utilities_array) // Get the list from string resources
        val libraryUtilitiesMenu = binding.menuLibraryUtilities.editText as MaterialAutoCompleteTextView // Object to manipulate the menu

        // Set selected item
        try {
            // If the index exists in the preferences and it is within the range of the array
            val index = preferences.getInt(Preferences.UTILITIES_MENU_SELECTED_INDEX, 1) // Default (watermark)
            libraryUtilitiesMenu.setText(libraryUtilitiesList[index])
        } catch (e: Exception) {
            libraryUtilitiesMenu.setText(libraryUtilitiesList[1]) // Default (watermark)
        }

        // The list is assigned, always after indicating the selected element
        libraryUtilitiesMenu.setSimpleItems(libraryUtilitiesList)

        // When the selected item changes
        libraryUtilitiesMenu.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            preferences.edit().putInt(Preferences.UTILITIES_MENU_SELECTED_INDEX, position).apply()
        }

    }

    /** Show the info dialog */
    private fun showInfoDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.app_name)
            .setMessage("${getString(R.string.app_description)}\n\n${getString(R.string.app_credits, BuildConfig.VERSION_NAME)}")
            .setPositiveButton(R.string.ok, null)
            .setNeutralButton(R.string.library_docs) { _, _ ->
                SystemWebBrowser.openUrl(this, "https://jeovanimartinez.github.io/Android-Utils/docs/")
            }
            .show()
    }

}
