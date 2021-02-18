package com.jeovanimartinez.androidutils

import android.graphics.Color
import android.view.View
import com.jeovanimartinez.androidutils.filesystem.tempfiles.TempFiles
import com.jeovanimartinez.androidutils.views.utils.Padding
import com.jeovanimartinez.androidutils.views.viewtoimage.ViewToImage
import com.jeovanimartinez.androidutils.views.viewtoimage.ViewToImageConfig
import com.jeovanimartinez.androidutils.views.viewtoimage.watermark.TextWatermark
import com.jeovanimartinez.androidutils.views.viewtoimage.watermark.WatermarkPosition
import com.jeovanimartinez.androidutils.views.viewtoimage.watermark.WatermarkRotation

fun tmp() {

    val context = this@MainActivity

    val view = View(context)

    val generate = { watermark: TextWatermark, fileName: String ->
        val config = ViewToImageConfig(view, Color.WHITE, Padding(15f), arrayListOf(watermark))
        TempFiles.saveBitmapToFile(context, ViewToImage.convert(context, config), fileName)
    }

    val watermarkBase = TextWatermark("Test", 12f, Color.BLACK, WatermarkPosition.TOP_LEFT, 0f, 0f, WatermarkRotation.DEG_0, 1f, null)

    generate(watermarkBase.copy(position = WatermarkPosition.TOP_LEFT, rotation = WatermarkRotation.DEG_0), "TOP_LEFT-DEG_0")
    generate(watermarkBase.copy(position = WatermarkPosition.TOP_LEFT, rotation = WatermarkRotation.DEG_90), "TOP_LEFT-DEG_90")
    generate(watermarkBase.copy(position = WatermarkPosition.TOP_LEFT, rotation = WatermarkRotation.DEG_180), "TOP_LEFT-DEG_180")
    generate(watermarkBase.copy(position = WatermarkPosition.TOP_LEFT, rotation = WatermarkRotation.DEG_270), "TOP_LEFT-DEG_270")

    generate(watermarkBase.copy(position = WatermarkPosition.TOP_LEFT, rotation = WatermarkRotation.DEG_0, offsetX = 10f, offsetY = 20f), "TOP_LEFT-DEG_0-OFFSET")
    generate(watermarkBase.copy(position = WatermarkPosition.TOP_LEFT, rotation = WatermarkRotation.DEG_90, offsetX = 10f, offsetY = 20f), "TOP_LEFT-DEG_90-OFFSET")
    generate(watermarkBase.copy(position = WatermarkPosition.TOP_LEFT, rotation = WatermarkRotation.DEG_180, offsetX = 10f, offsetY = 20f), "TOP_LEFT-DEG_180-OFFSET")
    generate(watermarkBase.copy(position = WatermarkPosition.TOP_LEFT, rotation = WatermarkRotation.DEG_270, offsetX = 10f, offsetY = 20f), "TOP_LEFT-DEG_270-OFFSET")


}