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
import java.io.IOException


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
     * Exclude the list of secondary views of the image.
     * @param context Context.
     * @param view Main view (must be a view group).
     * @param viewsToExclude List of children views to exclude.
     *
     * @return A bitmap of the [view] excluding the [viewsToExclude]. If viewsToExclude is empty, return a image representation of the view.
     *
     * @throws IllegalArgumentException If any child view of the view cannot be excluded from the image.
     * */
    private fun excludeChildrenViews(context: Context, view: View, viewsToExclude: ArrayList<ExcludeView>): Bitmap {

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

        val extraPadding = 10 // Extra padding to add to be able to correctly remove the children views

        // Generate a bitmap from the view
        val viewBitmap = Bitmap.createBitmap(view.width + extraPadding, view.height + extraPadding, Bitmap.Config.ARGB_8888).applyCanvas {
            translate(-view.scrollX.toFloat(), -view.scrollY.toFloat())
            view.draw(this)
        }

        val viewCanvas = Canvas(viewBitmap)

        // STEP 1, EXCLUDE VIEWS WITH MODE HIDE

        val hideViews = viewsToExclude.filter { it.mode == ExcludeMode.HIDE }
        // If the view has a solid background color, that color is used, otherwise the pixels are cleared with alpha.
        val hideViewPaint = if (view.background is ColorDrawable) {
            log("hideViewPaint use color")
            Paint().apply { style = Paint.Style.FILL; color = (view.background as ColorDrawable).color }
        } else {
            log("hideViewPaint use PorterDuff.Mode.CLEAR")
            Paint().apply { style = Paint.Style.FILL; xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }
        }
        // val hideViewPaint = Paint().apply { style = Paint.Style.FILL; color = Color.parseColor("#B2CF0000"); } // For development purposes only
        hideViews.forEach {
            viewCanvas.drawRect(
                it.view.x - it.view.marginLeft,
                it.view.y - it.view.marginTop,
                it.view.x + it.view.width + it.view.marginRight,
                it.view.y + it.view.height + it.view.marginBottom,
                hideViewPaint
            )
        }
        log("Exclude ${hideViews.size} view(s) from the image with mode ExcludeMode.HIDE")

        FileUtils.saveBitmapToFile(context, viewBitmap, "STEP_1") // // For development purposes only

        val markColor = Color.RED // Color to mark the spaces to be removed from the image with cropping modes

        // STEP 2, EXCLUDE VIEWS WITH CROP VERTICALLY




        return viewBitmap
    }


}