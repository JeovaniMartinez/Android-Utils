package com.jeovanimartinez.androidutils

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.jeovanimartinez.androidutils.extensions.dimension.dp2px
import com.jeovanimartinez.androidutils.filesystem.tempfiles.TempFiles
import com.jeovanimartinez.androidutils.views.utils.Padding
import com.jeovanimartinez.androidutils.views.viewtoimage.ViewToImage
import com.jeovanimartinez.androidutils.views.viewtoimage.ViewToImageConfig
import com.jeovanimartinez.androidutils.views.viewtoimage.watermark.TextWatermark
import com.jeovanimartinez.androidutils.views.viewtoimage.watermark.WatermarkPosition
import com.jeovanimartinez.androidutils.views.viewtoimage.watermark.WatermarkRotation
import java.io.File

fun tmpwatermark(context: Context, view: View, font: Int) {


    // BORRAR
    val files = File(context.filesDir, "androidutils_tempfiles").listFiles()
    files?.forEach {
        it.delete()
    }

    val generate = { watermark: TextWatermark, fileName: String ->
        val config = ViewToImageConfig(view, Color.WHITE, Padding(50f, 150f), arrayListOf(watermark))
        TempFiles.saveBitmapToFile(context, ViewToImage.convert(context, config), fileName)
    }

    //aaa yy II LL Ü Ó
    val watermarkBase = TextWatermark("By Jeovani Martínez", 12f, Color.BLACK, WatermarkPosition.TOP_LEFT, 0f, 0f, WatermarkRotation.DEG_0, 1f, font)

    generate(watermarkBase.copy(position = WatermarkPosition.TOP_LEFT, rotation = WatermarkRotation.DEG_0), "TOP_LEFT-DEG_0")
    generate(watermarkBase.copy(position = WatermarkPosition.TOP_LEFT, rotation = WatermarkRotation.DEG_90), "TOP_LEFT-DEG_90")
    generate(watermarkBase.copy(position = WatermarkPosition.TOP_LEFT, rotation = WatermarkRotation.DEG_180), "TOP_LEFT-DEG_180")
    generate(watermarkBase.copy(position = WatermarkPosition.TOP_LEFT, rotation = WatermarkRotation.DEG_270), "TOP_LEFT-DEG_270")

//    generate(watermarkBase.copy(position = WatermarkPosition.TOP_LEFT, rotation = WatermarkRotation.DEG_0, offsetX = 10f, offsetY = 10f), "TOP_LEFT-DEG_0-OFFSET")
//    generate(watermarkBase.copy(position = WatermarkPosition.TOP_LEFT, rotation = WatermarkRotation.DEG_90, offsetX = 10f, offsetY = 10f), "TOP_LEFT-DEG_90-OFFSET")
//    generate(watermarkBase.copy(position = WatermarkPosition.TOP_LEFT, rotation = WatermarkRotation.DEG_180, offsetX = 10f, offsetY = 10f), "TOP_LEFT-DEG_180-OFFSET")
//    generate(watermarkBase.copy(position = WatermarkPosition.TOP_LEFT, rotation = WatermarkRotation.DEG_270, offsetX = 10f, offsetY = 10f), "TOP_LEFT-DEG_270-OFFSET")

    generate(watermarkBase.copy(position = WatermarkPosition.MIDDLE_LEFT, rotation = WatermarkRotation.DEG_0), "MIDDLE_LEFT-DEG_0")
    generate(watermarkBase.copy(position = WatermarkPosition.MIDDLE_LEFT, rotation = WatermarkRotation.DEG_90), "MIDDLE_LEFT-DEG_90")
    generate(watermarkBase.copy(position = WatermarkPosition.MIDDLE_LEFT, rotation = WatermarkRotation.DEG_180), "MIDDLE_LEFT-DEG_180")
    generate(watermarkBase.copy(position = WatermarkPosition.MIDDLE_LEFT, rotation = WatermarkRotation.DEG_270), "MIDDLE_LEFT-DEG_270")

      generate(watermarkBase.copy(position = WatermarkPosition.MIDDLE_LEFT, rotation = WatermarkRotation.DEG_0, offsetX = 20f, offsetY = 0f), "MIDDLE_LEFT-DEG_0-OFFSET")
    generate(watermarkBase.copy(position = WatermarkPosition.MIDDLE_LEFT, rotation = WatermarkRotation.DEG_90, offsetX = 20f, offsetY = 0f), "MIDDLE_LEFT-DEG_90-OFFSET")
    generate(watermarkBase.copy(position = WatermarkPosition.MIDDLE_LEFT, rotation = WatermarkRotation.DEG_180, offsetX = 20f, offsetY = 0f), "MIDDLE_LEFT-DEG_180-OFFSET")
    generate(watermarkBase.copy(position = WatermarkPosition.MIDDLE_LEFT, rotation = WatermarkRotation.DEG_270, offsetX = 20f, offsetY = 0f), "MIDDLE_LEFT-DEG_270-OFFSET")


}