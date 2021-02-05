package com.jeovanimartinez.androidutils.reviews.rateinapp

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.transition.Fade
import android.view.View
import android.view.Window
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import com.jeovanimartinez.androidutils.R
import com.jeovanimartinez.androidutils.reviews.RateApp
import com.jeovanimartinez.androidutils.themes.translucent.TranslucentActivity
import kotlinx.android.synthetic.main.activity_rate_app.*

/** Actividad que se muestra en forma de diálogo para invitar al usuario a calificar la aplicación */
class RateAppActivity : TranslucentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.activityOpacity = 0.83f
        super.onCreate(savedInstanceState)
        configureTransitions()
        setContentView(R.layout.activity_rate_app)

        initSetup()

        RateInApp.log("Started RateAppActivity")
    }

    override fun onBackPressed() {
        RateInApp.log("User clicked back button")
        super.onBackPressed()
    }

    /** Configuración inicial y configuración de eventos */
    private fun initSetup() {
        configureTopShapeBackground()

        rateApp_noThanks.visibility = if (RateInApp.showNeverAskAgainButton) View.VISIBLE else View.GONE

        rateApp_rateNow.setOnClickListener {
            RateInApp.log("User clicked rateApp_rateNow button")
            RateApp.goToRateInGooglePlay(this@RateAppActivity) // Se usa la utilidad para dirigir al usuario Google Play
            supportFinishAfterTransition() // Necesario para que se muestre la transición de salida
        }

        rateApp_later.setOnClickListener {
            RateInApp.log("User clicked rateApp_later button")
            supportFinishAfterTransition()
        }

        rateApp_noThanks.setOnClickListener {

            RateInApp.log("User clicked rateApp_noThanks button")

            /*
            * Se guarda la preferencia de no mostrar nuevamente el diálogo
            * Se debe usar las mismas claves que en RateInApp.Preferences, solo que aquí se usan directamente para evitar
            * exponer RateInApp.Preferences y dejarlo privado. Si cambia alguna clave en el objeto RateInApp.Preferences, ajustarla aquí también
            * */
            val sharedPreferences = getSharedPreferences("rate_in_app_preferences", Context.MODE_PRIVATE) // Se crea la instancia del objeto para manipular las preferencias
            sharedPreferences.edit().putBoolean("rate_in_app_never_show_again", true).apply()
            RateInApp.log("Set rate_in_app_never_show_again to true and saved in preferences")

            supportFinishAfterTransition()
        }

    }

    // Referencia: https://stackoverflow.com/a/11376610
    /**Asigna el color de fondo de la tarjeta al fondo de la forma superior, para mantener soporte para tema claro y oscuro */
    private fun configureTopShapeBackground() {
        val topShapeBackground = AppCompatResources.getDrawable(this@RateAppActivity, R.drawable.rate_app_top_shape)
        val finalTopShapeBackground = DrawableCompat.wrap(topShapeBackground!!)
        DrawableCompat.setTint(finalTopShapeBackground, rateApp_card.cardBackgroundColor.defaultColor)

        rateApp_topShape.background = finalTopShapeBackground
    }

    /** Configura la transición de entrada y salida */
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
