package com.jeovanimartinez.androidutils.views.viewtoimage.config

import android.graphics.Color
import android.view.View
import androidx.annotation.ColorInt

/**
 * Allows to define the configuration to exclude a children view when converting a view to an image.
 * @param view Children view of view group to exclude of the image.
 * @param mode Exclude mode based on [ExcludeMode]
 * @param padding When the space that the children view occupies in the view is cropped, a padding can
 *        be added, this property determines the size of the padding. The property apply for all exclusion
 *        modes, except for ExcludeMode.HIDE.
 * @param paddingFillColor Fill color for the padding. Only applicable if padding is greater than zero.
 * */
data class ExcludeView(
    val view: View,
    val mode: ExcludeMode = ExcludeMode.HIDE,
    val padding: Float = 0f,
    @ColorInt val paddingFillColor: Int = Color.TRANSPARENT
)
