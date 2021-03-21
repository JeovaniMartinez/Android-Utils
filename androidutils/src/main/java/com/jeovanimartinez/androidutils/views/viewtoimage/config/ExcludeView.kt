package com.jeovanimartinez.androidutils.views.viewtoimage.config

import android.view.View

/**
 * Allows to define the configuration to exclude a children view when converting a view to an image.
 * @param view Children view of view group to exclude of the image.
 * @param mode Exclude mode based on [ExcludeMode]
 * */
data class ExcludeView(val view: View, val mode: ExcludeMode = ExcludeMode.HIDE)
