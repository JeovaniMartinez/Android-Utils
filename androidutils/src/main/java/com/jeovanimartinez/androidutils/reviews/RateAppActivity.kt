package com.jeovanimartinez.androidutils.reviews

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.transition.Fade
import android.view.View
import android.view.Window
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import com.jeovanimartinez.androidutils.R
import com.jeovanimartinez.androidutils.themes.translucent.TranslucentActivity
import kotlinx.android.synthetic.main.activity_rate_app.*

/** Activity to invite the user to rate the app */
class RateAppActivity : TranslucentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.activityOpacity = 0.83f
        super.onCreate(savedInstanceState)
        configureTransitions()
        setContentView(R.layout.activity_rate_app)

        initSetup()

        RateApp.log("Started RateAppActivity")
    }

    override fun onBackPressed() {
        RateApp.log("User clicked back button")
        super.onBackPressed()
    }

    /** Initial setup */
    private fun initSetup() {
        configureTopShapeBackground()

        rateApp_noThanks.visibility = if (RateApp.showNeverAskAgainButton) View.VISIBLE else View.GONE

        rateApp_rateNow.setOnClickListener {
            RateApp.log("User clicked rateApp_rateNow button")
            RateApp.goToRateInGooglePlay(this@RateAppActivity)
            supportFinishAfterTransition()
        }

        rateApp_later.setOnClickListener {
            RateApp.log("User clicked rateApp_later button")
            supportFinishAfterTransition()
        }

        rateApp_noThanks.setOnClickListener {

            RateApp.log("User clicked rateApp_noThanks button")

            val sharedPreferences = getSharedPreferences(RateApp.Preferences.KEY, Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean(RateApp.Preferences.NEVER_SHOW_AGAIN, true).apply()
            RateApp.log("Set rate_app_never_show_again to true and saved in preferences")

            supportFinishAfterTransition()
        }

    }

    // Reference: https://stackoverflow.com/a/11376610
    /** Assign the background color of the card to the background of the top shape, to keep support for light and dark theme */
    private fun configureTopShapeBackground() {
        val topShapeBackground = AppCompatResources.getDrawable(this@RateAppActivity, R.drawable.rate_app_top_shape)
        val finalTopShapeBackground = DrawableCompat.wrap(topShapeBackground!!)
        DrawableCompat.setTint(finalTopShapeBackground, rateApp_card.cardBackgroundColor.defaultColor)

        rateApp_topShape.background = finalTopShapeBackground
    }

    /** Set the activity transitions */
    private fun configureTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            with(window) {
                requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
                enterTransition = Fade().setDuration(200)
                exitTransition = Fade().setDuration(200)
            }
        }
    }

}
