package com.jeovanimartinez.androidutils.views.viewtoimage

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.graphics.applyCanvas
import androidx.core.view.*
import com.jeovanimartinez.androidutils.Base
import com.jeovanimartinez.androidutils.extensions.graphics.trimByBorderColor
import com.jeovanimartinez.androidutils.graphics.utils.CornerRadius
import com.jeovanimartinez.androidutils.graphics.utils.Margin
import com.jeovanimartinez.androidutils.graphics.utils.Padding
import com.jeovanimartinez.androidutils.views.viewtoimage.config.ExcludeMode
import com.jeovanimartinez.androidutils.views.viewtoimage.config.ExcludeView

/**
 * Utility class for converting views to images.
 * */
object ViewToImage : Base<ViewToImage>() {

    override val LOG_TAG = "ViewToImage"

    /**
     * Converts a view to a bitmap image.
     * @param view View from which the image will be generated.
     * @param backgroundColor Background color to apply to the image. Fills the entire [view] and [padding] if the view
     *        has no background, but if the view has a background, this color will only be visible inside the applied padding.
     * @param backgroundCornerRadius Corner radius for the background. It will only be appreciated if [backgroundColor] is not completely transparent and if the [padding]
     *        values are greater than zero.
     * @param trimBorders Determines whether the borders of the view are cropped, this process is performed before
     *        applying the margin and padding, and after excluding views (if applicable). To define the cropping
     *        area, the background color of the view is used.
     * @param padding Padding between the view edges and the margin internal edges. Values must be zero or positive,
     *        specified in pixels.
     * @param margin Margin between the padding external edges and the image edges. Values must be zero or positive,
     *        specified in pixels.
     * @param viewsToExclude If the view is a view group and has children views, it determines the children views
     *        to exclude when generating the image. The child views to be excluded must belong directly to the view.
     *        Child views within other child views cannot be excluded directly.
     *
     * @return A bitmap of the view.
     *
     * @throws IllegalStateException If the view is not already laid out.
     * @throws IllegalArgumentException If any child view of the view cannot be excluded from the image, or if the
     *         padding or margin values are invalid.
     * */
    fun convert(
        view: View,
        @ColorInt backgroundColor: Int = Color.TRANSPARENT,
        backgroundCornerRadius: CornerRadius = CornerRadius(0f),
        trimBorders: Boolean = false,
        padding: Padding = Padding(0f),
        margin: Margin = Margin(0f),
        viewsToExclude: ArrayList<ExcludeView> = arrayListOf()
    ): Bitmap {

        log("Started process to convert a view to a bitmap image")

        // Validations
        if (!ViewCompat.isLaidOut(view)) {
            throw IllegalStateException("The view needs to be laid out before converting it to an image")
        }
        require(padding.top >= 0 && padding.right >= 0 && padding.bottom >= 0 && padding.left >= 0) {
            "Padding values cannot be negative, the values must be equal to or greater than zero"
        }
        require(margin.top >= 0 && margin.right >= 0 && margin.bottom >= 0 && margin.left >= 0) {
            "Margin values cannot be negative, the values must be equal to or greater than zero"
        }

        val context = view.context

        val viewBitmap = excludeChildrenViews(context, view, viewsToExclude) // Exclude children views if it's necessary

        // FileUtils.saveBitmapToFile(context, viewBitmap, "CONVERT_STEP_0") // *** FOR DEVELOPMENT PURPOSES ONLY

        // Trim the image borders if it's necessary
        val viewBitmap2 = if (trimBorders) {
            // Margin is added to trim process to prevent loss of semi-transparent pixels
            val trimMargin = Margin(1f)
            when (view.background) {
                is ColorDrawable -> {
                    log("Image borders are trimmed based on the background color of the view")
                    viewBitmap.trimByBorderColor((view.background as ColorDrawable).color, trimMargin)
                }

                null -> {
                    log("Image borders are trimmed by Color.TRANSPARENT")
                    viewBitmap.trimByBorderColor(Color.TRANSPARENT, trimMargin)
                }

                else -> {
                    log("It's not possible to trim the image borders, because the view background is not an instance of color drawable")
                    viewBitmap
                }
            }
        } else {
            log("No need to trim image borders")
            viewBitmap
        }

        // FileUtils.saveBitmapToFile(context, viewBitmap2, "CONVERT_STEP_1") // *** FOR DEVELOPMENT PURPOSES ONLY

        // Final image applying margin and padding
        val viewBitmap3 = Bitmap.createBitmap(
            (viewBitmap2.width + padding.left + padding.right + margin.left + margin.right).toInt(),
            (viewBitmap2.height + padding.top + padding.bottom + margin.top + margin.bottom).toInt(),
            Bitmap.Config.ARGB_8888
        )

        log("Final image size: width = ${viewBitmap3.width} height = ${viewBitmap3.height}")

        val viewBitmap3Canvas = Canvas(viewBitmap3)

        // Draw the background color with the rounded corners if it's necessary
        if (backgroundColor != Color.TRANSPARENT) {
            val rect = RectF(
                margin.left,
                margin.top,
                padding.left + margin.left + viewBitmap2.width + padding.right,
                padding.top + margin.top + viewBitmap2.height + padding.bottom
            )
            val path = Path()
            path.addRoundRect(rect, backgroundCornerRadius.toRadii(), Path.Direction.CW)
            viewBitmap3Canvas.drawPath(path, Paint().apply { style = Paint.Style.FILL; color = backgroundColor; isAntiAlias = true })
        }

        viewBitmap3Canvas.drawBitmap(viewBitmap2, padding.left + margin.left, padding.top + margin.top, null)

        // FileUtils.saveBitmapToFile(context, viewBitmap3, "CONVERT_RESULT") // *** FOR DEVELOPMENT PURPOSES ONLY

        log("Finished process to convert a view to a bitmap image")

        return viewBitmap3
    }

    /**
     * Exclude the list of children views of the image.
     * @param context Context.
     * @param view Main view (must be a view group).
     * @param viewsToExclude List of children views to exclude.
     *
     * @return A bitmap of the [view] excluding the [viewsToExclude]. If viewsToExclude is empty, return an image representation of the view.
     *
     * @throws IllegalArgumentException If any child view of the view cannot be excluded from the image.
     * */
    private fun excludeChildrenViews(context: Context, view: View, viewsToExclude: ArrayList<ExcludeView>): Bitmap {

        if (viewsToExclude.isEmpty()) {
            log("There are no children views to exclude from the image")
            return view.drawToBitmap(Bitmap.Config.ARGB_8888)
        }

        if (view !is ViewGroup) {
            throw IllegalArgumentException("The view is not an instance of ViewGroup, children views cannot be excluded")
        }

        viewsToExclude.forEach {
            if (!view.contains(it.view)) {
                val viewName = context.resources.getResourceEntryName(view.id)
                val viewChildName = context.resources.getResourceEntryName(it.view.id)
                throw IllegalArgumentException("The $viewChildName child view cannot be excluded from the image because it does not belong to $viewName view")
            }
        }

        // FileUtils.saveBitmapToFile(context, view.drawToBitmap(Bitmap.Config.ARGB_8888), "EXCLUDE_VIEWS_STEP_0") // *** FOR DEVELOPMENT PURPOSES ONLY

        log("Started process to exclude children views")

        // Lists of children views to exclude, according to the exclusion mode
        val hideViews = viewsToExclude.filter { it.mode == ExcludeMode.HIDE }
        val cropVerticallyViews = viewsToExclude.filter { it.mode == ExcludeMode.CROP_VERTICALLY || it.mode == ExcludeMode.CROP_ALL }.sortedBy { it.view.y }
        val cropHorizontallyViews = viewsToExclude.filter { it.mode == ExcludeMode.CROP_HORIZONTALLY || it.mode == ExcludeMode.CROP_ALL }.sortedBy { it.view.x }

        log("Exclusion lists size: hideViews = ${hideViews.size} | cropVerticallyViews = ${cropVerticallyViews.size} | cropHorizontallyViews = ${cropHorizontallyViews.size} ")

        val markColor = Color.RED // Color to mark the spaces to be removed from the image with cropping modes
        val markPaint = Paint().apply { style = Paint.Style.FILL; color = markColor; } // Paint for cropping modes

        /* Extra auxiliary padding to be added in order to correctly remove child views. */
        // Padding right is added if vertical children views need to be cropped as the entire width is marked
        val extraRightPadding = if (cropVerticallyViews.isNotEmpty()) 10 else 0
        // Padding bottom is added if horizontal children views need to be cropped as the entire height is marked
        val extraBottomPadding = if (cropHorizontallyViews.isNotEmpty()) 10 else 0

        // Generate a bitmap from the view
        val viewBitmap = Bitmap.createBitmap(view.width + extraRightPadding, view.height + extraBottomPadding, Bitmap.Config.ARGB_8888).applyCanvas {
            translate(-view.scrollX.toFloat(), -view.scrollY.toFloat())
            view.draw(this)
        }

        log("View size: width = ${view.width} height = ${view.height} ")
        log("viewBitmap size: width = ${viewBitmap.width} height = ${viewBitmap.height} ")

        val viewCanvas = Canvas(viewBitmap)

        /* STEP 1, EXCLUDE VIEWS WITH MODE HIDE */

        if (hideViews.isNotEmpty()) {
            // If the view has a solid background color, that color is used, otherwise, the pixels are cleared with alpha.
            val hideViewPaint = if (view.background is ColorDrawable) {
                log("Use view background color for hideViewPaint")
                Paint().apply { style = Paint.Style.FILL; color = (view.background as ColorDrawable).color }
            } else {
                log("Use PorterDuff.Mode.CLEAR for hideViewPaint")
                Paint().apply { style = Paint.Style.FILL; xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }
            }
            // val hideViewPaint = Paint().apply { style = Paint.Style.FILL; color = Color.parseColor("#B2CF0000"); } // *** FOR DEVELOPMENT PURPOSES ONLY
            var hiddenViewsCount = 0
            hideViews.forEach {
                if (it.view.visibility == View.GONE || it.view.visibility == View.INVISIBLE) return@forEach // If it is not visible, there is no need to hide it
                // Use marginLeft and marginRight instead of marginStart and marginEnd for best results, even in RTL mode
                viewCanvas.drawRect(
                    it.view.x - if (it.includeMargin) it.view.marginLeft else 0,
                    it.view.y - if (it.includeMargin) it.view.marginTop else 0,
                    it.view.x + it.view.width + if (it.includeMargin) it.view.marginRight else 0,
                    it.view.y + it.view.height + if (it.includeMargin) it.view.marginBottom else 0,
                    hideViewPaint
                )
                hiddenViewsCount++
            }
            log("Excluded $hiddenViewsCount view(s) from the image with mode ExcludeMode.HIDE")

            // FileUtils.saveBitmapToFile(context, viewBitmap, "EXCLUDE_VIEWS_STEP_1") // *** FOR DEVELOPMENT PURPOSES ONLY
        }

        /* STEP 2, EXCLUDE VIEWS WITH CROP VERTICALLY */

        var viewBitmap2 = viewBitmap // Assignation initial

        if (cropVerticallyViews.isNotEmpty()) {
            var croppedVerticallyViewsCount = 0
            // First, the entire region to be deleted is marked
            cropVerticallyViews.forEach {
                if (it.view.visibility == View.GONE) return@forEach // Applies only in View.INVISIBLE and View.VISIBLE since in View.GONE the view does not occupy any space
                // NOTE: The padding is part of the view, so it is already included in the width or height
                viewCanvas.drawRect(
                    0f,
                    it.view.y - if (it.includeMargin) it.view.marginTop else 0,
                    viewBitmap.width.toFloat(),
                    it.view.y + it.view.height + if (it.includeMargin) it.view.marginBottom else 0,
                    markPaint
                )
                croppedVerticallyViewsCount++
            }
            val horizontalColoredArray = IntArray(viewBitmap.width) { markColor } // To compare to the mark color
            val horizontalBuffer = IntArray(viewBitmap.width) // To read the pixels from the image and compare them with horizontalColoredArray
            // Calculate the total pixels to be cropped vertically
            var allCropHeight = 0
            for (y in 0 until viewBitmap.height) {
                if (viewBitmap.getPixel(viewBitmap.width - 1, y) == markColor) allCropHeight++
            }
            viewBitmap2 = Bitmap.createBitmap(viewBitmap.width - extraRightPadding, viewBitmap.height - allCropHeight, Bitmap.Config.ARGB_8888)
            log("viewBitmap2 size: width = ${viewBitmap2.width} height = ${viewBitmap2.height} (allCropHeight = $allCropHeight)")
            var y = 0 // Position in y-axis for draw the pixels
            for (i in 0 until viewBitmap.height) {
                viewBitmap.getPixels(horizontalBuffer, 0, viewBitmap.width, 0, i, viewBitmap.width, 1) // It reads the entire width and one pixel height
                // Determines if the row of pixels should be preserved, and adds it to the bitmap if so
                if (!horizontalColoredArray.contentEquals(horizontalBuffer) && y < viewBitmap2.height) {
                    viewBitmap2.setPixels(horizontalBuffer, 0, viewBitmap.width, 0, y, viewBitmap.width - extraRightPadding, 1)
                    y++
                }
            }
            log("Excluded $croppedVerticallyViewsCount view(s) from the image with mode crop vertically")

            // FileUtils.saveBitmapToFile(context, viewBitmap, "EXCLUDE_VIEWS_STEP_2_A") // *** FOR DEVELOPMENT PURPOSES ONLY
            // FileUtils.saveBitmapToFile(context, viewBitmap2, "EXCLUDE_VIEWS_STEP_2_B") // *** FOR DEVELOPMENT PURPOSES ONLY
        }

        /* STEP 3, EXCLUDE VIEWS WITH CROP HORIZONTALLY */

        var viewBitmap3 = viewBitmap2 // Assignation initial

        if (cropHorizontallyViews.isNotEmpty()) {
            val viewBitmap2Canvas = Canvas(viewBitmap2) // To mark the area to be removed
            var croppedHorizontallyViewsCount = 0
            // First, the entire region to be deleted is marked
            cropHorizontallyViews.forEach {
                if (it.view.visibility == View.GONE) return@forEach // Applies only in View.INVISIBLE and View.VISIBLE since in View.GONE the view does not occupy any space
                // NOTE: The padding is part of the view, so it is already included in the width or height
                viewBitmap2Canvas.drawRect(
                    it.view.x - if (it.includeMargin) it.view.marginLeft else 0,
                    0f,
                    it.view.x + it.view.width + if (it.includeMargin) it.view.marginRight else 0,
                    viewBitmap2.height.toFloat(),
                    markPaint
                )
                croppedHorizontallyViewsCount++
            }
            val verticalColoredArray = IntArray(viewBitmap2.height) { markColor } // To compare to the mark color
            val verticalBuffer = IntArray(viewBitmap2.height) // To read the pixels from the image and compare them with verticalColoredArray
            // Calculate the total pixels to be cropped horizontally
            var allCropWidth = 0
            for (x in 0 until viewBitmap2.width) {
                if (viewBitmap2.getPixel(x, viewBitmap2.height - 1) == markColor) allCropWidth++
            }
            viewBitmap3 = Bitmap.createBitmap(viewBitmap2.width - allCropWidth, viewBitmap2.height - extraBottomPadding, Bitmap.Config.ARGB_8888)
            log("viewBitmap3 size: width = ${viewBitmap3.width} (allCropWidth = $allCropWidth) height = ${viewBitmap3.height}")
            var x = 0 // Position in x-axis to draw the pixels
            for (i in 0 until viewBitmap2.width) {
                viewBitmap2.getPixels(verticalBuffer, 0, 1, i, 0, 1, viewBitmap2.height) // It reads the entire width and one pixel height
                // Determines if the row of pixels should be preserved, and adds it to the bitmap if so
                if (!verticalColoredArray.contentEquals(verticalBuffer) && x < viewBitmap3.width) {
                    viewBitmap3.setPixels(verticalBuffer, 0, 1, x, 0, 1, viewBitmap2.height - extraBottomPadding)
                    x++
                }
            }
            log("Excluded $croppedHorizontallyViewsCount view(s) from the image with mode crop horizontally")

            // FileUtils.saveBitmapToFile(context, viewBitmap2, "EXCLUDE_VIEWS_STEP_3_A") // *** FOR DEVELOPMENT PURPOSES ONLY
            // FileUtils.saveBitmapToFile(context, viewBitmap3, "EXCLUDE_VIEWS_STEP_3_B") // *** FOR DEVELOPMENT PURPOSES ONLY
        }

        // ExcludeMode.CROP_ALL its processed in cropVerticallyViews and cropHorizontallyViews

        // FileUtils.saveBitmapToFile(context, viewBitmap3, "EXCLUDE_VIEWS_RESULT") // *** FOR DEVELOPMENT PURPOSES ONLY

        log("Finished process to exclude children views")

        return viewBitmap3
    }

}
