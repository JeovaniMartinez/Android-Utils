package com.jeovanimartinez.androidutils.views.viewtoimage.config

/**
 * Sets the mode to exclude a child view when converting a view group to an image.
 * */
enum class ExcludeMode {

    /**
     * Hides the child view by replacing all the space it occupies with the background color of the view,
     * the image of the view keeps its original size.
     * */
    HIDE,

    /**
     * Crops the image of the view vertically, deleting all the space occupied by the child view, so the
     * resulting image will have a smaller height than the original view.
     * */
    CROP_VERTICALLY,

    /**
     * Crops the image of the view horizontally, deleting all the space occupied by the child view, so the
     * resulting image will have a smaller width than the original view.
     * */
    CROP_HORIZONTALLY,

    /**
     * Crops the image of the view vertically and horizontally, deleting all the space occupied by the child
     * view, so the resulting image will have a smaller height and width than the original view.
     * */
    CROP_ALL,

}
