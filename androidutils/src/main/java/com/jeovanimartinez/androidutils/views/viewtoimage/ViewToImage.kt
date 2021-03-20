package com.jeovanimartinez.androidutils.views.viewtoimage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.view.View
import androidx.core.graphics.applyCanvas
import androidx.core.view.ViewCompat
import androidx.core.view.drawToBitmap
import com.jeovanimartinez.androidutils.Base
import com.jeovanimartinez.androidutils.filesystem.FileUtils
import com.jeovanimartinez.androidutils.graphics.utils.Margin
import com.jeovanimartinez.androidutils.graphics.utils.Padding
import com.jeovanimartinez.androidutils.watermark.Watermark


/**
 * Utility class for converting views to images
 * */
object ViewToImage : Base<ViewToImage>() {

    override val LOG_TAG = "ViewToImage"

    /**
     * Class with configuration to exclude subviews when converting a view to an image.
     * @param view View to be hidden.
     * @param cropImageVertically Determines whether hiding the view, will crop all the space it occupies vertically in the image.
     * @param cropImageHorizontally Determines whether hiding the view, will crop all the space it occupies horizontally in the image.
     * */
    data class ExcludeView(val view: View, val cropImageVertically: Boolean = false, val cropImageHorizontally: Boolean = false)

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

        // Converts the view to bitmap using the code of view.drawToBitmap()
        if (!ViewCompat.isLaidOut(view)) {
            throw IllegalStateException("View needs to be laid out before convert it to image")
        }
        val viewBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888).applyCanvas {
            translate(-view.scrollX.toFloat(), -view.scrollY.toFloat())
            drawColor(backgroundColor)
            view.draw(this)
        }

        FileUtils.saveBitmapToFile(context, viewBitmap, "test")

        log("Finished process to convert a view to bitmap image")

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