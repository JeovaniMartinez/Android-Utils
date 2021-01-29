package com.jeovanimartinez.androidutils.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.jeovanimartinez.androidutils.about.AboutApp
import com.jeovanimartinez.androidutils.extensions.configureTaskDescription
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

        configureTaskDescription(R.string.app_name, R.mipmap.ic_launcher, R.color.teal_200)
    }

    private fun initSetup() {

        // Alternar los temas
        toggleTheme.setOnClickListener {

            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }

        }

        // Calificar en google play
        rateInGooglePlay.setOnClickListener {

            RateApp.goToRateInGooglePlay(this@MainActivity)

        }

        // Más aplicaciones
        moreApps.setOnClickListener {

            MoreAppsGPlay.showAppList(this@MainActivity)

        }

        // Iniciar y configurar la utilidad para mostrar el flujo para calificar la app
        initRateInApp.setOnClickListener {

            RateInApp
                .setMinInstallElapsedDays(0)
                .setMinInstallLaunchTimes(1)
                .setMinRemindElapsedDays(0)
                .setMinRemindLaunchTimes(1)
                .setShowAtEvent(1)
                .setShowNeverAskAgainButton(true)
                .init(this@MainActivity)

            rateInAppConfigured = true

        }

        // Muestra el flujo para calificar la app (si se cumplen las condiciones de la configuración)
        checkAndShowRateInApp.setOnClickListener {

            if (!rateInAppConfigured) {
                Toast.makeText(this@MainActivity, "Please initialize RateInApp before to click this button", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            RateInApp.checkAndShow(this@MainActivity)

        }

        // Muestra la actividad de acerca de
        about.setOnClickListener {

            AboutApp.apply {
                backgroundColor = R.color.colorBackground
                appIcon = R.drawable.library_logo
                appName = R.string.about_app_app_name
                appVersionName = BuildConfig.VERSION_NAME
                authorName = R.string.about_app_author_name
                authorLink = R.string.about_app_author_link
                companyLogo = R.drawable.logo_jedemm_com
                companyName = R.string.about_app_company_name
                companyLink = R.string.about_app_company_link
                termsAndPrivacyPolicyLink = R.string.terms_and_privacy_policy_link
                termsAndPrivacyPolicyTextColor = R.color.colorTermsAndPrivacyPolicyText
                showOpenSourceLicenses = true
            }.show(this@MainActivity)

        }

    }

}
