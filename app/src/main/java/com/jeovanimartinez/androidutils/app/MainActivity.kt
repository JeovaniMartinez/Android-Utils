package com.jeovanimartinez.androidutils.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import com.jeovanimartinez.androidutils.Base
import com.jeovanimartinez.androidutils.about.AboutApp
import com.jeovanimartinez.androidutils.about.AboutAppConfig
import com.jeovanimartinez.androidutils.extensions.activity.configureTaskDescription
import com.jeovanimartinez.androidutils.extensions.context.getColorCompat
import com.jeovanimartinez.androidutils.extensions.context.shortToast
import com.jeovanimartinez.androidutils.moreapps.MoreAppsGPlay
import com.jeovanimartinez.androidutils.reviews.RateApp
import com.jeovanimartinez.androidutils.reviews.rateinapp.RateInApp
import com.jeovanimartinez.androidutils.tmpwatermark
import com.jeovanimartinez.androidutils.web.SystemWebBrowser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var rateInAppConfigured = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Base.logEnable = BuildConfig.DEBUG // Se habilita el log en desarrollo, y se deshabilita en producción

        initSetup()

        configureTaskDescription(R.string.app_name, R.mipmap.ic_launcher, getColorCompat(R.color.colorBackground))

        Handler(Looper.getMainLooper()).postDelayed({
            tmpwatermark(this@MainActivity, viewDemo, R.font.fugaz_one_regular)
        }, 200)
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

            MoreAppsGPlay.apply { developerId = "GitHub" }.showAppList(this@MainActivity)

            // Modo compacto
            // MoreAppsGPlay.showAppList(this@MainActivity)

        }

        // Iniciar y configurar la utilidad para mostrar el flujo para calificar la app
        initRateInApp.setOnClickListener {

            RateInApp.apply {
                minInstallElapsedDays = 10
                minInstallLaunchTimes = 10
                minRemindElapsedDays = 2
                minRemindLaunchTimes = 4
                showAtEvent = 2
                showNeverAskAgainButton = true
            }.init(this@MainActivity)

            rateInAppConfigured = true

        }

        // Muestra el flujo para calificar la app (si se cumplen las condiciones de la configuración)
        checkAndShowRateInApp.setOnClickListener {

            if (!rateInAppConfigured) {
                shortToast("Please initialize RateInApp before to click this button")
                return@setOnClickListener
            }

            RateInApp.checkAndShow(this@MainActivity)

        }

        // Muestra la actividad de acerca de
        about.setOnClickListener {

            val aboutAppConfig = AboutAppConfig(
                backgroundColor = getColorCompat(R.color.colorBackground),
                iconsColor = getColorCompat(R.color.colorIcon),
                appIcon = R.drawable.library_logo,
                appName = R.string.about_app_app_name,
                appVersionName = BuildConfig.VERSION_NAME,
                authorName = R.string.about_app_author_name,
                authorLink = R.string.about_app_author_link,
                companyLogo = R.drawable.logo_jedemm_com,
                companyName = R.string.about_app_company_name,
                companyLink = R.string.about_app_company_link,
                termsAndPrivacyPolicyLink = R.string.about_app_terms_and_policy_link,
                termsAndPrivacyPolicyTextColor = getColorCompat(R.color.colorTermsAndPrivacyPolicyText),
                showOpenSourceLicenses = true,
                taskDescriptionTitle = R.string.app_name,
                taskDescriptionIcon = R.mipmap.ic_launcher,
                taskDescriptionColor = getColorCompat(R.color.colorBackground)
            )

            AboutApp.show(this@MainActivity, aboutAppConfig)

        }

        openUrl.setOnClickListener {

            SystemWebBrowser.openUrl(this@MainActivity, "https://jedemm.com", "jedemm_website")

            // Modo compacto
            //SystemWebBrowser.openUrl(this@MainActivity, "https://jedemm.com")

        }

    }

}
