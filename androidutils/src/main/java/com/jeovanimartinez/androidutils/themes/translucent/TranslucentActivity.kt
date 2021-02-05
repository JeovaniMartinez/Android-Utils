package com.jeovanimartinez.androidutils.themes.translucent

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.jeovanimartinez.androidutils.R
import com.jeovanimartinez.androidutils.extensions.basictypes.mapValue
import com.jeovanimartinez.androidutils.extensions.nullability.whenNotNull

/**
 * Clase base para actividades con fondo translúcido.
 * Las actividades que hereden de esta clase deben tener el siguiente tema en el manifest: android:theme="@style/AndroidUtilsTheme.Translucent"
 * En el diseño de las actividades que hereden de esta clase deben tener un RelativeLayout como elemento raíz en el diseño para que ocupen el espacio completo, o bien un tamaño fijo.
 * */
open class TranslucentActivity : AppCompatActivity() {

    /**
     * Opacidad de la actividad, el valor debe estar en el rango de 0 a 1. Donde 0 es completamente transparente
     * y 1 es completamente opaca. Puede cambiarse en cualquier momento durante la ejecución de la actividad, y la
     * opacidad se vera reflejada inmediatamente. Se recomienda asignar inicialmente antes de llamar al
     * super.onCreate(savedInstanceState) en el onCreate() de la actividad que herede de esta clase.
     * */
    var activityOpacity = 0f
        set(value) {
            if (value < 0f || value > 1f) throw Exception("The opacity value must be between 0 and 1") // Se valida el valor
            field = value // Se asigna el valor a la variable
            window.setDimAmount(value) // Se ajusta la opacidad de la actividad
        }

    /** Cuando se crea la actividad, para asignar los valores iniciales */
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AndroidUtilsTheme_Translucent) // Se asigna el tema translúcido para poder usar la opacidad
        window.setDimAmount(activityOpacity) // Se asigna la opacidad al iniciar la actividad
        super.onCreate(savedInstanceState)
    }

    /** Al pausar la actividad, se agrega un fondo a la actividad para obtener una mejor vista en las animaciones */
    override fun onPause() {
        window.setBackgroundDrawable(generateBackgroundDrawable(this.window.attributes.dimAmount)) // Se asigna el fondo a la ventana
        super.onPause()
    }

    /** Al reanudar la actividad, se reasigna el fondo transparente, de modo que se mantenga la opacidad en base al valor de dimAmount */
    override fun onResume() {
        window.setBackgroundDrawable(generateBackgroundDrawable(0f)) // Se asigna el fondo a la ventana
        super.onResume()
    }

    /**
     * Configura el dim (atenuación) de una ventana (independiente a esta clase)
     * @param window ventana de la que se va a configurar el dim
     * Invocar a esta función cuando se vaya a mostrar una ventana encima de la actividad, para asegurarse que la atenuación
     * de la ventana a mostrar sea igual o mayor que la opacidad de la actividad, ya que si no se hace se genera
     * un efecto visual indeseable.
     * Por ejemplo, para un diálogo:
     *  val dialog = MaterialAlertDialogBuilder(this@AboutActivity).setTitle("DEMO").show();
     *  configureWindowDim(dialog.window);
     * */
    fun configureWindowDim(window: Window?) {
        val currentWindowDim = window?.attributes?.dimAmount
        currentWindowDim.whenNotNull {
            if (it < activityOpacity) {
                window?.setDimAmount(activityOpacity)
            }
        }
    }

    /** Genera un fondo negro para la actividad, con el [alpha] indicado (entre 0 y 1, que representa la opacidad) */
    private fun generateBackgroundDrawable(alpha: Float): GradientDrawable {
        val drawable = GradientDrawable() // Se genera el fondo
        drawable.shape = GradientDrawable.RECTANGLE
        // Se asigna el color, negro con la opacidad requerida
        drawable.setColor(Color.argb(alpha.mapValue(0f, 1f, 0f, 255f).toInt(), 0, 0, 0))
        return drawable
    }

}
