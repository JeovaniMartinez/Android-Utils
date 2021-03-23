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
import com.jeovanimartinez.androidutils.filesystem.FileUtils
import com.jeovanimartinez.androidutils.graphics.utils.Margin
import com.jeovanimartinez.androidutils.graphics.utils.Padding
import com.jeovanimartinez.androidutils.views.viewtoimage.config.ExcludeMode
import com.jeovanimartinez.androidutils.views.viewtoimage.config.ExcludeView
import com.jeovanimartinez.androidutils.watermark.Watermark


/**
 * Utility class for converting views to images.
 * */
object ViewToImage : Base<ViewToImage>() {

    override val LOG_TAG = "ViewToImage"

    /**
     * Converts a view to a bitmap image.
     * @param context Context.
     * @param view View from which the image will be generated.
     * @param backgroundColor Background color to apply to the image.
     * @param trimBorders Determines whether before applying margin and padding the borders of the view are cropped.
     *        To define the cropping area, use the background color of the view.
     * @param padding Padding between the view and the image edges.
     * @param margin Margin between the view and the image edges.
     * @param viewsToExclude If the view is a view group and has a children views, it determines the children's views
     *        to exclude when generating the image.
     * @param watermarks List of watermarks to draw on the image.
     *
     * @return A bitmap of the view.
     *
     * @throws IllegalArgumentException If any child view of the view cannot be excluded from the image.
     * */
    fun convert(
        context: Context,
        view: View,
        @ColorInt backgroundColor: Int = Color.TRANSPARENT,
        trimBorders: Boolean = false,
        padding: Padding = Padding(0f),
        margin: Margin = Margin(0f),
        viewsToExclude: ArrayList<ExcludeView> = arrayListOf(),
        watermarks: ArrayList<Watermark> = arrayListOf()
    ): Bitmap {

        log("Started process to convert a view to a bitmap image")

        if (!ViewCompat.isLaidOut(view)) {
            throw IllegalStateException("View needs to be laid out before calling drawToBitmap()")
        }


        val viewBitmap = excludeChildrenViews(context, view, viewsToExclude)

        FileUtils.saveBitmapToFile(context, viewBitmap, "step1")

        val viewCanvas = Canvas(viewBitmap)

        return Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)


        //val excludeViewPaint = Paint().apply { style = Paint.Style.FILL; xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }
        val excludeViewPaint = Paint().apply { style = Paint.Style.FILL; color = Color.RED; } // For development purposes only

        var heExclude = 0f

        // Replace the view location with transparent pixels, including margin and padding
        viewsToExclude.forEach {
            if (it.view.visibility == View.GONE) return@forEach
            // NOTE: The padding is part of the view, so it is already included in the width or height
            viewCanvas.drawRect(
                0f,//it.view.x - it.view.marginLeft,
                it.view.y - it.view.marginTop,
                viewBitmap.width.toFloat(),//it.view.x + it.view.width + it.view.marginRight,
                it.view.y + it.view.height + it.view.marginBottom,
                excludeViewPaint
            )

            heExclude += it.view.height + it.view.marginTop + it.view.marginBottom
        }

        var colored = IntArray(viewBitmap.width) { Color.RED }
        var buffer = IntArray(viewBitmap.width)

        val b = Bitmap.createBitmap(viewBitmap.width, (viewBitmap.height - heExclude).toInt(), Bitmap.Config.ARGB_8888)

        var y = 0

        for (i in 0 until viewBitmap.height) {
            viewBitmap.getPixels(buffer, 0, viewBitmap.width, 0, i, viewBitmap.width, 1)
            if (!colored.contentEquals(buffer)) {
                b.setPixels(buffer, 0, viewBitmap.width, 0, y, viewBitmap.width, 1)
                y++
            }
        }

        FileUtils.saveBitmapToFile(context, viewBitmap, "original")
        FileUtils.saveBitmapToFile(context, b, "proceced")


        val verticallyCrop = viewsToExclude.filter { it.mode == ExcludeMode.CROP_VERTICALLY }.sortedBy { it.view.y }

        var croppedBitmap = Bitmap.createBitmap(viewBitmap.width, viewBitmap.height, Bitmap.Config.ARGB_8888)
        Canvas(croppedBitmap).drawBitmap(viewBitmap, 0f, 0f, null)

        var ya = false

        var h = 0

        /*verticallyCrop.forEach {
            if (!ya) {

                var topBitmap: Bitmap? = null

                var topBitmapHeight = (it.view.y - it.view.marginTop - h).toInt()
                if (topBitmapHeight > 0) {
                    topBitmap = Bitmap.createBitmap(croppedBitmap.width, topBitmapHeight, Bitmap.Config.ARGB_8888)
                    Canvas(topBitmap).drawBitmap(croppedBitmap, 0f, 0f -h, null)

                    FileUtils.saveBitmapToFile(context, topBitmap, "top")
                }

                if (topBitmapHeight < 0) topBitmapHeight = 0

                var bottomBitmap: Bitmap? = null
                var bottomBitmapHeight = croppedBitmap.height - topBitmapHeight - it.view.height - it.view.marginTop - it.view.marginBottom
                if (bottomBitmapHeight > 0) {
                    bottomBitmap = Bitmap.createBitmap(croppedBitmap.width, bottomBitmapHeight, Bitmap.Config.ARGB_8888)
                    Canvas(bottomBitmap).drawBitmap(croppedBitmap, 0f, -(it.view.y + it.view.marginBottom + it.view.height), null)

                    FileUtils.saveBitmapToFile(context, bottomBitmap, "bottom")
                }

                if (bottomBitmapHeight < 0) bottomBitmapHeight = 0

                croppedBitmap = Bitmap.createBitmap(croppedBitmap.width, topBitmapHeight + bottomBitmapHeight, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(croppedBitmap)

                topBitmap.whenNotNull { bitmap ->
                    canvas.drawBitmap(bitmap, 0f, 0f - h, null)
                }

                bottomBitmap.whenNotNull { bitmap ->
                    canvas.drawBitmap(bitmap, 0f, topBitmapHeight.toFloat() - h, null)
                }

                h = it.view.height - it.view.marginTop - it.view.marginBottom

                //ya = true
            }
        }*/


        //val horizontallyCrop = viewsToExclude.filter { it.cropImageHorizontally }.sortedBy { it.view.x }


        // TMPPP


        //log("Finished process to convert a view to bitmap image")


        //if (viewsToExclude.size > 0) log("We proceed to exclude ${viewsToExclude.size} views") else log("There are no views to exclude")

        /*
        val imageWidth = (view.width + padding.left + padding.right).toInt()
        val imageHeight = (view.height + padding.top + padding.bottom).toInt()

        /*if (!ViewCompat.isLaidOut(view)) {
            throw IllegalStateException("View needs to be laid out before calling drawToBitmap()")
        }*/


        val viewBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888).applyCanvas {

            translate(-view.scrollX.toFloat(), -view.scrollY.toFloat())
            drawColor(backgroundColor)
            view.draw(this)
        }

        val image = Bitmap.createBitmap(imageWidth, imageHeight, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(image)


        canvas.drawBitmap(viewBitmap, padding.left, padding.top, null)


        val obj = view.findViewById<View>(id)

        val shape = GradientDrawable()
            .apply { shape = GradientDrawable.RECTANGLE; setColor(Color.RED) }

        WatermarkUtils.drawWatermark(
            context, image, Watermark.Drawable(
                drawable = shape,
                position = WatermarkPosition.TOP_LEFT,
                width = obj.width.toFloat() + obj.marginStart + obj.marginEnd,
                height = obj.height.toFloat() + obj.marginStart + obj.marginEnd,
                dx = obj.x - obj.marginStart,
                dy = obj.y - obj.marginTop,
                rotation = 0f,
                opacity = 1f,
                measurementDimension = Dimension.PX
            )
        )
*/

        return Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
    }

    /**
     * Exclude the list of children views of the image.
     * @param context Context.
     * @param view Main view (must be a view group).
     * @param viewsToExclude List of children views to exclude.
     *
     * @return A bitmap of the [view] excluding the [viewsToExclude]. If viewsToExclude is empty, return a image representation of the view.
     *
     * @throws IllegalArgumentException If any child view of the view cannot be excluded from the image.
     * */
    private fun excludeChildrenViews(context: Context, view: View, viewsToExclude: ArrayList<ExcludeView>): Bitmap {

        if (!ViewCompat.isLaidOut(view)) {
            throw IllegalStateException("View needs to be laid out before convert it to an image")
        }

        if (viewsToExclude.isEmpty()) {
            log("There are no children views to exclude from the image")
            return view.drawToBitmap(Bitmap.Config.ARGB_8888)
        }

        if (view !is ViewGroup) {
            throw IllegalArgumentException("The view is not a instance of ViewGroup, children views cannot be excluded")
        }

        viewsToExclude.forEach {
            if (!view.contains(it.view)) {
                val viewName = context.resources.getResourceEntryName(view.id)
                val viewChildName = context.resources.getResourceEntryName(it.view.id)
                throw IllegalArgumentException("The $viewChildName child view cannot be excluded from the image because it does not belong to $viewName view")
            }
        }

        FileUtils.saveBitmapToFile(context, view.drawToBitmap(Bitmap.Config.ARGB_8888), "STEP_0") // *** FOR DEVELOPMENT PURPOSES ONLY

        log("Started process to exclude children views")

        // Lists of children views to exclude, according to the exclusion mode
        val hideViews = viewsToExclude.filter { it.mode == ExcludeMode.HIDE }
        val cropVerticallyViews = viewsToExclude.filter { it.mode == ExcludeMode.CROP_VERTICALLY || it.mode == ExcludeMode.CROP_ALL }.sortedBy { it.view.y }
        val cropHorizontallyViews = viewsToExclude.filter { it.mode == ExcludeMode.CROP_HORIZONTALLY || it.mode == ExcludeMode.CROP_ALL }.sortedBy { it.view.x }

        log("Exclusion lists size: hideViews = ${hideViews.size} | cropVerticallyViews = ${cropVerticallyViews.size} | cropHorizontallyViews = ${cropHorizontallyViews.size} ")

        val markColor = Color.RED // Color to mark the spaces to be removed from the image with cropping modes
        val markPaint = Paint().apply { style = Paint.Style.FILL; color = markColor; } // Paint for cropping modes

        // Extra padding to add to be able to correctly remove the children views
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

        // STEP 1, EXCLUDE VIEWS WITH MODE HIDE

        if (hideViews.isNotEmpty()) {
            // If the view has a solid background color, that color is used, otherwise the pixels are cleared with alpha.
            val hideViewPaint = if (view.background is ColorDrawable) {
                log("Use view background color for hideViewPaint")
                Paint().apply { style = Paint.Style.FILL; color = (view.background as ColorDrawable).color }
            } else {
                log("Use PorterDuff.Mode.CLEAR for hideViewPaint")
                Paint().apply { style = Paint.Style.FILL; xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }
            }
            // val hideViewPaint = Paint().apply { style = Paint.Style.FILL; color = Color.parseColor("#B2CF0000"); } // *** FOR DEVELOPMENT PURPOSES ONLY
            hideViews.forEach {
                if (it.view.visibility == View.GONE) return@forEach
                // Use marginLeft and marginRight instead of marginStart and marginEnd to best results, even in RTL mode
                viewCanvas.drawRect(
                    it.view.x - it.view.marginLeft,
                    it.view.y - it.view.marginTop,
                    it.view.x + it.view.width + it.view.marginRight,
                    it.view.y + it.view.height + it.view.marginBottom,
                    hideViewPaint
                )
            }
            log("Excluded ${hideViews.size} view(s) from the image with mode ExcludeMode.HIDE")

            FileUtils.saveBitmapToFile(context, viewBitmap, "STEP_1") // *** FOR DEVELOPMENT PURPOSES ONLY
        }

        // STEP 2, EXCLUDE VIEWS WITH CROP VERTICALLY

        var viewBitmap2 = viewBitmap // Assignation initial

        if (cropVerticallyViews.isNotEmpty()) {
            // First the entire region to be deleted is marked
            cropVerticallyViews.forEach {
                if (it.view.visibility == View.GONE) return@forEach
                // NOTE: The padding is part of the view, so it is already included in the width or height
                viewCanvas.drawRect(
                    0f,
                    it.view.y - it.view.marginTop,
                    viewBitmap.width.toFloat(),
                    it.view.y + it.view.height + it.view.marginBottom,
                    markPaint
                )
            }
            val horizontalColoredArray = IntArray(viewBitmap.width) { markColor } // To compare to the mark color
            val horizontalBuffer = IntArray(viewBitmap.width) // To read the pixels from the image and compare with horizontalColoredArray
            // Calculate the total pixels to be cropped vertically
            var allCropHeight = 0
            for (y in 0 until viewBitmap.height) {
                if (viewBitmap.getPixel(0, y) == markColor) allCropHeight++
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
            log("Excluded ${cropVerticallyViews.size} view(s) from the image with mode crop vertically")

            FileUtils.saveBitmapToFile(context, viewBitmap, "STEP_2_A") // *** FOR DEVELOPMENT PURPOSES ONLY
            FileUtils.saveBitmapToFile(context, viewBitmap2, "STEP_2_B") // *** FOR DEVELOPMENT PURPOSES ONLY
        }

        // STEP 3, EXCLUDE VIEWS WITH CROP HORIZONTALLY

        var viewBitmap3 = viewBitmap2 // Assignation initial

        if (cropHorizontallyViews.isNotEmpty()) {
            val viewBitmap2Canvas = Canvas(viewBitmap2) // To mark the area to be remove
            // First the entire region to be deleted is marked
            cropHorizontallyViews.forEach {
                if (it.view.visibility == View.GONE) return@forEach
                // NOTE: The padding is part of the view, so it is already included in the width or height
                viewBitmap2Canvas.drawRect(
                    it.view.x - it.view.marginLeft,
                    0f,
                    it.view.x + it.view.width + it.view.marginRight,
                    viewBitmap2.height.toFloat(),
                    markPaint
                )
            }
            val verticalColoredArray = IntArray(viewBitmap2.height) { markColor } // To compare to the mark color
            val verticalBuffer = IntArray(viewBitmap2.height) // To read the pixels from the image and compare with verticalColoredArray
            // Calculate the total pixels to be cropped horizontally
            var allCropWidth = 0
            for (x in 0 until viewBitmap2.width) {
                if (viewBitmap2.getPixel(x, 0) == markColor) allCropWidth++
            }
            viewBitmap3 = Bitmap.createBitmap(viewBitmap2.width - allCropWidth, viewBitmap2.height - extraBottomPadding, Bitmap.Config.ARGB_8888)
            log("viewBitmap3 size: width = ${viewBitmap3.width} (allCropWidth = $allCropWidth) height = ${viewBitmap3.height}")
            var x = 0 // Position in x-axis for draw the pixels
            for (i in 0 until viewBitmap2.width) {
                viewBitmap2.getPixels(verticalBuffer, 0, 1, i, 0, 1, viewBitmap2.height) // It reads the entire width and one pixel height
                // Determines if the row of pixels should be preserved, and adds it to the bitmap if so
                if (!verticalColoredArray.contentEquals(verticalBuffer) && x < viewBitmap3.width) {
                    viewBitmap3.setPixels(verticalBuffer, 0, 1, x, 0, 1, viewBitmap2.height - extraBottomPadding)
                    x++
                }
            }
            log("Excluded ${cropHorizontallyViews.size} view(s) from the image with mode crop horizontally")

            FileUtils.saveBitmapToFile(context, viewBitmap2, "STEP_3_A") // *** FOR DEVELOPMENT PURPOSES ONLY
            FileUtils.saveBitmapToFile(context, viewBitmap3, "STEP_3_B") // *** FOR DEVELOPMENT PURPOSES ONLY
        }

        // ExcludeMode.CROP_ALL its processed in cropVerticallyViews and cropHorizontallyViews

        FileUtils.saveBitmapToFile(context, viewBitmap3, "STEP_NA_RESULT") // *** FOR DEVELOPMENT PURPOSES ONLY

        return viewBitmap3
    }


}