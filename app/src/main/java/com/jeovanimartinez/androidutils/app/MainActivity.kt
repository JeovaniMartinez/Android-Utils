package com.jeovanimartinez.androidutils.app

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.webkit.URLUtil
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jeovanimartinez.androidutils.Base
import com.jeovanimartinez.androidutils.about.AboutApp
import com.jeovanimartinez.androidutils.about.AboutAppConfig
import com.jeovanimartinez.androidutils.activity.config.TaskDescriptionConfig
import com.jeovanimartinez.androidutils.extensions.activity.configureTaskDescription
import com.jeovanimartinez.androidutils.extensions.context.getColorCompat
import com.jeovanimartinez.androidutils.extensions.context.getFontCompat
import com.jeovanimartinez.androidutils.extensions.context.shortToast
import com.jeovanimartinez.androidutils.filesystem.FileUtils
import com.jeovanimartinez.androidutils.filesystem.TempFiles
import com.jeovanimartinez.androidutils.graphics.utils.Dimension
import com.jeovanimartinez.androidutils.moreapps.MoreApps
import com.jeovanimartinez.androidutils.reviews.RateApp
import com.jeovanimartinez.androidutils.watermark.Watermark
import com.jeovanimartinez.androidutils.watermark.WatermarkUtils
import com.jeovanimartinez.androidutils.watermark.config.WatermarkPosition
import com.jeovanimartinez.androidutils.watermark.config.WatermarkShadow
import com.jeovanimartinez.androidutils.web.SystemWebBrowser
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private var rateAppInitialized = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Base.logEnable = BuildConfig.DEBUG // Configure log

        initSetup()

        configureTaskDescription(R.string.app_name, R.mipmap.ic_launcher, getColorCompat(R.color.colorBackground))

    }

    /** Initial setup */
    private fun initSetup() {

        toggleThemeSetup()
        rateAppSetup()
        aboutAppSetup()
        translucentThemeSetup()
        fileUtilsSetup()
        moreAppsSetup()
        systemWebBrowserSetup()

        val context = this@MainActivity

        // Get the bitmap from image resource
        val bitmap = BitmapFactory.decodeResource(
            resources,
            R.drawable.watermark_base2,
            BitmapFactory.Options().apply { inMutable = true; inScaled = false }
        )


        val drawableWatermark = Watermark.Drawable(
            drawable = R.drawable.library_logo,
            position = WatermarkPosition.BOTTOM_LEFT,
            width = 80f,
            height = 80f,
            dx = 10f,
            dy = -5f,
            rotation = 0f,
            opacity = 0.8f,
            measurementDimension = Dimension.PX
        )

        val textWatermark = Watermark.Text(
            text = "Sample Watermark By Android Utils",
            textSize = 30f,
            textColor = Color.WHITE,
            position = WatermarkPosition.TOP_RIGHT,
            dx = -10f,
            dy = 10f,
            rotation = 0f,
            opacity = 0.65f,
            typeface = getFontCompat(R.font.fugaz_one_regular),
            shadow = WatermarkShadow(2f, 10f, 20f, Color.parseColor("#1976D2")),
            measurementDimension = Dimension.PX
        )


        // Draw the watermarks on the image
        WatermarkUtils.drawWatermarks(
            context,
            bitmap,
            arrayListOf(
                drawableWatermark,
                drawableWatermark.copy(dx = 95f, opacity = 0.5f),
                textWatermark,
            )
        )

        // Save into file using FileUtils
        FileUtils.saveBitmapToFile(
            context = context,
            bitmap = bitmap,
            fileName = "watermark-demo",
            format = Bitmap.CompressFormat.JPEG
        )


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

            if (rateAppInitialized) {
                shortToast("RateApp it's already initialized")
                return@setOnClickListener
            }

            RateApp.apply {
                minInstallElapsedDays = 10
                minInstallLaunchTimes = 10
                minRemindElapsedDays = 2
                minRemindLaunchTimes = 4
                showAtEvent = 2
                showNeverAskAgainButton = true
            }.init(this@MainActivity)

            rateAppInitialized = true

            shortToast("RateApp successfully initialized")

        }

        checkAndShowRateAppBtn.setOnClickListener {

            if (!rateAppInitialized) {
                shortToast("Please initialize RateApp before to click this button")
                return@setOnClickListener
            } else {

                shortToast("Check logcat")
                RateApp.checkAndShow(this@MainActivity)

            }

        }

        rateInGooglePlayBtn.setOnClickListener {
            RateApp.goToRateInGooglePlay(this@MainActivity)
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

    private fun translucentThemeSetup() {

        launchTranslucentActivityBtn.setOnClickListener {

            val opacity = (translucentActivityOpacity.progress / 100.0).toFloat()

            startActivity(
                Intent(this@MainActivity, TranslucentThemeDemo::class.java).putExtra("opacity", opacity),
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) ActivityOptions.makeSceneTransitionAnimation(this@MainActivity).toBundle() else null
            )
        }

    }

    private fun fileUtilsSetup() {

        saveBitmapToFileBtn.setOnClickListener {
            try {
                // Create a bitmap object for test the utility and draw a color and text on it.
                val bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                canvas.drawColor(Color.DKGRAY)
                canvas.drawText("Android Utils", 35f, 60f, Paint().apply { color = Color.WHITE; textSize = 50f; isAntiAlias = true })

                // Use FileUtils to save bitmap into image file.
                val result = FileUtils.saveBitmapToFile(this@MainActivity, bitmap)

                MaterialAlertDialogBuilder(this@MainActivity)
                    .setMessage("File saved: ${result.absolutePath}")
                    .setPositiveButton("Ok") { _, _ -> }
                    .show()

            } catch (e: IOException) {
                shortToast("IOException")
            }
        }

        clearTempFilesFolderBtn.setOnClickListener {
            TempFiles.clearTempFilesFolder(this@MainActivity)
            shortToast("clearTempFilesFolder() invoked")
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
