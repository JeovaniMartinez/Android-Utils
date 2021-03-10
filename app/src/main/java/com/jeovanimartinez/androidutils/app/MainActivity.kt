package com.jeovanimartinez.androidutils.app

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.webkit.URLUtil
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.jeovanimartinez.androidutils.Base
import com.jeovanimartinez.androidutils.about.AboutApp
import com.jeovanimartinez.androidutils.about.AboutAppConfig
import com.jeovanimartinez.androidutils.activity.config.TaskDescriptionConfig
import com.jeovanimartinez.androidutils.extensions.activity.configureTaskDescription
import com.jeovanimartinez.androidutils.extensions.context.getColorCompat
import com.jeovanimartinez.androidutils.extensions.context.shortToast
import com.jeovanimartinez.androidutils.moreapps.MoreApps
import com.jeovanimartinez.androidutils.reviews.RateApp
import com.jeovanimartinez.androidutils.web.SystemWebBrowser
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var rateAppConfigured = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Base.logEnable = BuildConfig.DEBUG // Configure log

        initSetup()

        configureTaskDescription(R.string.app_name, R.mipmap.ic_launcher, getColorCompat(R.color.colorBackground))

    }

    private fun initSetup() {

        toggleThemeSetup()
        translucentThemeSetup()
        rateAppSetup()
        aboutAppSetup()
        moreAppsSetup()
        systemWebBrowserSetup()

    }

    private fun toggleThemeSetup() {

        toggleThemeBtn.setOnClickListener {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }

    }

    private fun rateAppSetup() {

        initRateAppBtn.setOnClickListener {

            RateApp.apply {
                minInstallElapsedDays = 10
                minInstallLaunchTimes = 10
                minRemindElapsedDays = 2
                minRemindLaunchTimes = 4
                showAtEvent = 2
                showNeverAskAgainButton = true
            }.init(this@MainActivity)

            rateAppConfigured = true

        }

        checkAndShowRateAppBtn.setOnClickListener {

            if (!rateAppConfigured) {
                shortToast("Please initialize RateApp before to click this button")
                return@setOnClickListener
            }

            RateApp.checkAndShow(this@MainActivity)

        }

        rateInGooglePlayBtn.setOnClickListener {
            RateApp.goToRateInGooglePlay(this@MainActivity)
        }

    }

    private fun translucentThemeSetup() {

        launchTranslucentActivityBtn.setOnClickListener {

            val opacity = (translucentActivityOpacity.progress / 100.0).toFloat()

            startActivity(
                Intent(this@MainActivity, TranslucentThemeDemo::class.java).putExtra("opacity", opacity),
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) ActivityOptions.makeSceneTransitionAnimation(this@MainActivity).toBundle() else null
            )
        }

    }

    private fun aboutAppSetup() {

        aboutAppBtn.setOnClickListener {

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
                TaskDescriptionConfig(R.string.app_name, R.mipmap.ic_launcher, getColorCompat(R.color.colorBackground))
            )

            AboutApp.show(this@MainActivity, aboutAppConfig)

        }

    }

    private fun moreAppsSetup() {

        moreAppsBtn.setOnClickListener {

            MoreApps.apply { developerId = "Jedemm+Technologies" }.showAppListInGooglePlay(this@MainActivity)

        }

    }

    private fun systemWebBrowserSetup() {

        openUrlBtn.setOnClickListener {

            val url = openUrlEt.text.toString()

            if (!URLUtil.isValidUrl(url)) {
                shortToast("Please enter a valid URL")
                return@setOnClickListener
            }

            SystemWebBrowser.openUrl(this@MainActivity, url, "test")
        }

    }


}
