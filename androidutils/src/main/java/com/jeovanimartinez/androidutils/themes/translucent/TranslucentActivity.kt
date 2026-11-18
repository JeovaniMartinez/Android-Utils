@file:Suppress("unused")

package com.jeovanimartinez.androidutils.themes.translucent

import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.Surface
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jeovanimartinez.androidutils.R
import com.jeovanimartinez.androidutils.extensions.basictypes.mapValue
import com.jeovanimartinez.androidutils.extensions.nullability.whenNotNull

/**
 * Base class for activities with a translucent background.
 *
 * Activities that inherit from this class must have the following theme in the manifest:
 * ```
 *     android:theme="@style/AndroidUtilsTheme.Translucent"
 * ```
 * In the layout file, activities that inherit from TranslucentActivity must have a fixed size or a
 * RelativeLayout as the root element so that they occupy the entire screen space.
 * */
open class TranslucentActivity : AppCompatActivity() {

    /**
     * Activity opacity, the value must be in the range of 0 to 1. Where 0 is completely transparent and 1 is
     * completely opaque. It can be changed at any time during the execution of the activity, and the opacity
     * will be applied immediately.
     * */
    var activityOpacity = 0f
        set(value) {
            if (value < 0f || value > 1f) throw Exception("The opacity value must be between 0 and 1") // Validation
            field = value
            window.setDimAmount(value) // Adjust the activity opacity
        }

    /**
     * The activity's main view, which must be assigned to enable proper horizontal
     * positioning adjustments, particularly on modern Android versions where
     * edge-to-edge display is enabled by default.
     *
     * This property must be assigned after enabling edge-to-edge with `adjustEdgeToEdgeTranslucentActivity()`
     * and setting the activity's content view. It is required for the automatic position
     * adjustment based on the device orientation and display cutout.
     * */
    protected var mainLayout: View? = null
        set(value) {
            field = value
            value?.post { adjustViewPosition() }
        }

    /** When the activity is created, to assign the initial values */
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AndroidUtilsTheme_Translucent)
        window.setDimAmount(activityOpacity)
        super.onCreate(savedInstanceState)
    }

    /** On pausing the activity, adds a background to the activity for a better view in animations */
    override fun onPause() {
        // Adds background to the activity for a better view in animations
        window.setBackgroundDrawable(generateBackgroundDrawable(this.window.attributes.dimAmount))
        super.onPause()
    }

    /** On resuming activity, the activity background is set to transparent, so that opacity is keep based on the value of dimAmount */
    override fun onResume() {
        // The activity background is set to transparent, so that opacity is kept based on the value of dimAmount
        window.setBackgroundDrawable(generateBackgroundDrawable(0f))
        super.onResume()
    }

    /** Called when the device configuration changes */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        adjustViewPosition() // Adjusts the main layout's position after an orientation change
    }

    /**
     * Sets the dim (attenuation) of a window (independent of this class).
     *
     * Invoke this function when a window is to be shown above to this activity, to ensure that the attenuation of the
     * window to be shown is equal to or greater than the opacity of this activity, since if it is not done, an
     * undesirable visual effect is generated.
     *
     * ```
     *     // For example, for a dialog:
     *     val dialog = MaterialAlertDialogBuilder(this@AboutActivity).setTitle("DEMO").show();
     *     configureWindowDim(dialog.window);
     * ```
     *
     * @param window window to configure the dim of it.
     * */
    fun configureWindowDim(window: Window?) {
        val currentWindowDim = window?.attributes?.dimAmount
        currentWindowDim.whenNotNull {
            if (it < activityOpacity) {
                window?.setDimAmount(activityOpacity)
            }
        }
    }

    /** Generates a black background for the activity, with the indicated [alpha] (between 0 and 1, which represents the opacity) */
    private fun generateBackgroundDrawable(alpha: Float): GradientDrawable {
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.RECTANGLE
        // The black color is assigned, with the required capacity
        drawable.setColor(Color.argb(alpha.mapValue(0f, 1f, 0f, 255f).toInt(), 0, 0, 0))
        return drawable
    }

    /**
     * Adjusts the main layout's horizontal position according to the current device orientation
     * and display cutout.
     *
     * In landscape orientation, the layout is shifted horizontally to compensate for the display
     * cutout, taking the current display rotation into account.
     *
     * In portrait orientation, the horizontal translation is reset to zero.
     *
     * The adjustment is performed only when the main layout and window insets are available.
     * */
    private fun adjustViewPosition() {

        // Get the main layout and return if it has not been assigned
        val layout = mainLayout ?: return

        // Get the current display rotation
        val rotation = getCurrentDisplayRotation()

        // Retrieve the current window insets and return if they are not available
        val insets = ViewCompat.getRootWindowInsets(layout) ?: return

        // Get the display cutout insets
        val cutout = insets.getInsets(WindowInsetsCompat.Type.displayCutout())

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            // Adjust the horizontal position according to the display rotation
            if (rotation == Surface.ROTATION_270) {
                layout.translationX = -cutout.right.toFloat()
            } else if (rotation == Surface.ROTATION_90) {
                layout.translationX = cutout.right.toFloat()
            }

        } else {

            // Reset the horizontal translation in portrait orientation
            layout.translationX = 0f

        }

    }

    /**
     * Returns the current display rotation
     * */
    private fun getCurrentDisplayRotation(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            display?.rotation ?: Surface.ROTATION_0
        } else {
            @Suppress("DEPRECATION")
            windowManager.defaultDisplay.rotation
        }
    }

}
