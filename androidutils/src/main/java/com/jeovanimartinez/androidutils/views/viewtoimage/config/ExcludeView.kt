package com.jeovanimartinez.androidutils.views.viewtoimage.config

import android.view.View

/**
 * Allows to defining the configuration to exclude a children view when converting a view to an image.
 * @param view Child view of view group to exclude of the image.
 * @param mode Exclude mode based on [ExcludeMode]
 * @param includeMargin Determines if the margin is included in the size of the child view, so if
 *        it is true, the view will be excluded including its margins.
 * */
data class ExcludeView(val view: View, val mode: ExcludeMode = ExcludeMode.HIDE, val includeMargin: Boolean = false)
