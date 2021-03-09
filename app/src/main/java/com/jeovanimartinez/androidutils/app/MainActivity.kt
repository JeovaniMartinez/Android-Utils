package com.jeovanimartinez.androidutils.app

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.jeovanimartinez.androidutils.Base
import com.jeovanimartinez.androidutils.about.AboutApp
import com.jeovanimartinez.androidutils.about.AboutAppConfig
import com.jeovanimartinez.androidutils.activity.config.TaskDescriptionConfig
import com.jeovanimartinez.androidutils.extensions.activity.configureTaskDescription
import com.jeovanimartinez.androidutils.extensions.context.getColorCompat
import com.jeovanimartinez.androidutils.extensions.context.shortToast
import com.jeovanimartinez.androidutils.filesystem.FileUtils
import com.jeovanimartinez.androidutils.graphics.utils.Dimension
import com.jeovanimartinez.androidutils.moreapps.MoreApps
import com.jeovanimartinez.androidutils.reviews.RateApp
import com.jeovanimartinez.androidutils.watermark.Watermark
import com.jeovanimartinez.androidutils.watermark.WatermarkUtils
import com.jeovanimartinez.androidutils.watermark.config.WatermarkPosition
import com.jeovanimartinez.androidutils.watermark.config.WatermarkShadow
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private var rateAppConfigured = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Base.logEnable = BuildConfig.DEBUG // Se habilita el log en desarrollo, y se deshabilita en producción

        initSetup()


        configureTaskDescription(R.string.app_name, R.mipmap.ic_launcher, getColorCompat(R.color.colorBackground))

        Handler(Looper.getMainLooper()).postDelayed({
            try {

                // Create a bitmap object for test the utility and draw a color and text on it.
                val bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                canvas.drawColor(Color.DKGRAY)
                canvas.drawText("Android Utils", 35f, 60f, Paint().apply { color = Color.WHITE; textSize = 50f; isAntiAlias = true })

// Use FileUtils to save bitmap into image file.
                FileUtils.saveBitmapToFile(this@MainActivity, bitmap, "test", filesDir.absolutePath, Bitmap.CompressFormat.PNG, 100)


            } catch (e: IOException) {
                shortToast("ERROR")
                e.printStackTrace()
            }


        }, 10)


        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadii = floatArrayOf(8f, 8f, 8f, 8f, 0f, 0f, 0f, 0f)
        shape.setColor(Color.RED)


        val bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888)
        val watermark = Watermark.Drawable(
            R.drawable.library_logo,
            WatermarkPosition.BOTTOM_RIGHT,
            200f,
            200f,
            -10f,
            -10f,
            0f,
            0.5f,
            Dimension.PX
        )

        val tw = Watermark.Text(
            "Hola Mundo",
            60f,
            Color.BLACK,
            WatermarkPosition.MIDDLE_LEFT,
            10f,
            0f,
            0f,
            1f,
            null,
            WatermarkShadow(2f,3f,3f, Color.parseColor("#811976D2")),
            Dimension.DP
        )

        WatermarkUtils.drawWatermarks(this@MainActivity, bitmap, arrayListOf(tw, watermark))
        //FileUtils.saveBitmapToFile(this@MainActivity, bitmap, "Test1")

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

            MoreApps.apply { developerId = "GitHub" }.showAppListInGooglePlay(this@MainActivity)

            // Modo compacto
            // MoreAppsGPlay.showAppList(this@MainActivity)

        }

        // Iniciar y configurar la utilidad para mostrar el flujo para calificar la app
        initRateApp.setOnClickListener {

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

        // Muestra el flujo para calificar la app (si se cumplen las condiciones de la configuración)
        checkAndShowRateApp.setOnClickListener {

            if (!rateAppConfigured) {
                shortToast("Please initialize RateApp before to click this button")
                return@setOnClickListener
            }

            RateApp.checkAndShow(this@MainActivity)

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
                TaskDescriptionConfig(R.string.app_name, R.mipmap.ic_launcher, getColorCompat(R.color.colorBackground))
            )

            AboutApp.show(this@MainActivity, aboutAppConfig)

        }

        openUrl.setOnClickListener {

            val bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888)
            val c = Canvas(bitmap)
            c.drawColor(Color.RED)
            FileUtils.saveBitmapToFile(this@MainActivity, bitmap)

            //FileUtils.saveBitmapToFile(this@MainActivity, bitmap, "jeo.jpeg", "/otro//")

            //SystemWebBrowser.openUrl(this@MainActivity, "https://jedemm.com", "jedemm_website")

            // Modo compacto
            //SystemWebBrowser.openUrl(this@MainActivity, "https://jedemm.com")

        }

    }

}
