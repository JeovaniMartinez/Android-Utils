package com.jeovanimartinez.androidutils.reviews.rateinapp

import android.os.Build
import android.os.Bundle
import android.transition.Fade
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import com.jeovanimartinez.androidutils.R
import kotlinx.android.synthetic.main.activity_rate_app.*

/** Actividad que se muestra en forma de diálogo para invitar al usuario a calificar la aplicación */
class RateAppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureTransitions()
        setContentView(R.layout.activity_rate_app)

        initSetup()
    }

    /** Configuración inicial */
    private fun initSetup() {
        configureTopShapeBackground()
    }

    /**
     * Asigna el color de fondo de la tarjeta al fondo de la forma superior, para mantener soporte para tema claro y oscuro
     * Referencia: https://stackoverflow.com/questions/11376516/change-drawable-color-programmatically/11376812
     * */
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
