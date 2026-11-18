package com.jeovanimartinez.androidutils.extensions.activity

import android.os.Build
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.viewbinding.ViewBinding

/**
 * Adjusts the activity's content padding to accommodate system bars and display cutouts
 * when using edge-to-edge display.
 *
 * The padding is automatically updated whenever Android dispatches new window insets,
 * including changes caused by device orientation.
 *
 * This function should be called only once from the activity's `onCreate()` method,
 * immediately after inflating the view binding and calling `setContentView()`.
 *
 * Example:
 * ```
 * binding = ActivityMainBinding.inflate(layoutInflater)
 * setContentView(binding.root)
 * adjustEdgeToEdge(binding)
 * ```
 *
 * Reference: https://developer.android.com/develop/ui/views/layout/edge-to-edge
 *
 * @param binding The activity's root view binding.
 */
fun adjustEdgeToEdge(binding: ViewBinding) {

    // Not required on Android versions below API 35.
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.VANILLA_ICE_CREAM) {
        return
    }

    ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->

        val bars = insets.getInsets(
            WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout() // System bars or display cutouts
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
