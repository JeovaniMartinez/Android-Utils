package com.jeovanimartinez.androidutils.views.viewtoimage

import android.content.Context
import android.graphics.*
import android.view.View
import androidx.core.graphics.applyCanvas
import androidx.core.view.*
import com.jeovanimartinez.androidutils.Base
import com.jeovanimartinez.androidutils.extensions.graphics.trimByBorderColor
import com.jeovanimartinez.androidutils.extensions.nullability.whenNotNull
import com.jeovanimartinez.androidutils.filesystem.FileUtils
import com.jeovanimartinez.androidutils.graphics.utils.Margin
import com.jeovanimartinez.androidutils.graphics.utils.Padding
import com.jeovanimartinez.androidutils.views.viewtoimage.config.ExcludeMode
import com.jeovanimartinez.androidutils.views.viewtoimage.config.ExcludeView
import com.jeovanimartinez.androidutils.watermark.Watermark


/**
 * Utility class for converting views to images
 * */
object ViewToImage : Base<ViewToImage>() {

    override val LOG_TAG = "ViewToImage"

    /**
     * Converts a view to a bitmap image.
     * @param context Context.
     * @param view View from which the image will be generated.
     * @param backgroundColor Background color to apply to the image.
     * @param padding Padding between the view and the image edges.
     * @param margin Margin between the view and the image edges.
     * @param watermarks List of watermarks to draw on the image.
     * @param viewsToExclude If the view has subviews, it determines the subviews to exclude when generating the image.
     * @return A bitmap of the view.
     * */
    fun convert(
        context: Context,
        view: View,
        backgroundColor: Int = Color.TRANSPARENT,
        padding: Padding = Padding(0f),
        margin: Margin = Margin(0f),
        watermarks: ArrayList<Watermark> = arrayListOf(),
        viewsToExclude: ArrayList<ExcludeView> = arrayListOf()
    ): Bitmap {

        log("Started process to convert a view to bitmap image")

        val viewBitmap = view.drawToBitmap(Bitmap.Config.ARGB_8888)
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


        val verticallyCrop = viewsToExclude.filter { it.excludeMode == ExcludeMode.CROP_VERTICALLY }.sortedBy { it.view.y }

        var croppedBitmap = Bitmap.createBitmap(viewBitmap.width,viewBitmap.height, Bitmap.Config.ARGB_8888)
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


        log("Finished process to convert a view to bitmap image")


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


}