package com.jeovanimartinez.androidutils.extensions.activity

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.viewbinding.ViewBinding

/**
 * Se encarga de ajustar correctamente los margenes según la orientación para el edge-to-edge.
 * Tomando en cuenta las barras del sistema y los cortes de pantalla, como cuando el dispositivo
 * tiene la cámara debajo de la pantalla.
 * Invocar siempre en el onCreate y en el onConfigurationChanged de la actividad.
 * Referencia: https://developer.android.com/develop/ui/views/layout/edge-to-edge
 * @param binding Binding principal de la actividad
 * */
fun adjustEdgeToEdge(binding: ViewBinding) {

    // En versiones anteriores a la API 35 (de 34 para abajo) no se ocupa
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.VANILLA_ICE_CREAM) {
        return
    }

    ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
        val bars = insets.getInsets(
            WindowInsetsCompat.Type.systemBars() // Barras del sistema
                    or WindowInsetsCompat.Type.displayCutout() // O cortes de pantalla
        )



        view.updatePadding(
            left = bars.left,
            top = bars.top,
            right = bars.right,
            bottom = bars.bottom,
        )
        WindowInsetsCompat.CONSUMED
    }

}

/**
 * Configura el EdgeToEdge en actividades translúcidas, llamar siempre antes del super.onCreate(savedInstanceState)
 * */
fun adjustEdgeToEdgeTranslucentActivity(activity: ComponentActivity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
        activity.enableEdgeToEdge()
    }
}
