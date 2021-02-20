package com.jeovanimartinez.androidutils

import android.content.Context
import android.graphics.Color
import android.view.View
import com.jeovanimartinez.androidutils.filesystem.tempfiles.TempFiles
import com.jeovanimartinez.androidutils.views.utils.Padding
import com.jeovanimartinez.androidutils.views.viewtoimage.ViewToImage
import com.jeovanimartinez.androidutils.views.viewtoimage.ViewToImageConfig
import com.jeovanimartinez.androidutils.views.viewtoimage.watermark.TextWatermark
import com.jeovanimartinez.androidutils.views.viewtoimage.watermark.WatermarkPosition
import com.jeovanimartinez.androidutils.views.viewtoimage.watermark.WatermarkRotation
import java.io.File

fun testViewToImage(context: Context, view: View, font: Int? = null) {


    // Primero se eliminan todos los archivos de la carpeta temporal
    val files = File(context.filesDir, "androidutils_tempfiles").listFiles()
    files?.forEach {
        it.delete()
    }

    val padding = Padding(100f, 100f)

    val configDemo = ViewToImageConfig(view, Color.WHITE, padding, arrayListOf(
        TextWatermark("[0]", 12f, Color.BLACK, WatermarkPosition.TOP_LEFT, 20f, 20f, WatermarkRotation.DEG_0, 1f, font),
        TextWatermark("[1]", 12f, Color.BLACK, WatermarkPosition.TOP_CENTER, 0f, 20f, WatermarkRotation.DEG_0, 1f, font),
        TextWatermark("[2]", 12f, Color.BLACK, WatermarkPosition.TOP_RIGHT, -20f, 20f, WatermarkRotation.DEG_0, 1f, font),
        TextWatermark("[3]", 12f, Color.BLACK, WatermarkPosition.MIDDLE_LEFT, 20f, 0f, WatermarkRotation.DEG_0, 1f, font),
        TextWatermark("[4]", 12f, Color.BLACK, WatermarkPosition.MIDDLE_CENTER, 0f, 0f, WatermarkRotation.DEG_0, 1f, font),
        TextWatermark("[5]", 12f, Color.BLACK, WatermarkPosition.MIDDLE_RIGHT, -20f, 0f, WatermarkRotation.DEG_0, 1f, font),
        TextWatermark("[6]", 12f, Color.BLACK, WatermarkPosition.BOTTOM_LEFT, 20f, -20f, WatermarkRotation.DEG_0, 1f, font),
        TextWatermark("[7]", 12f, Color.BLACK, WatermarkPosition.BOTTOM_CENTER, 0f, -20f, WatermarkRotation.DEG_0, 1f, font),
        TextWatermark("[8]", 12f, Color.BLACK, WatermarkPosition.BOTTOM_RIGHT, -20f, -20f, WatermarkRotation.DEG_0, 1f, font)
    ))
    TempFiles.saveBitmapToFile(context, ViewToImage.convert(context, configDemo), "Demo")

    val config = ViewToImageConfig(view, Color.TRANSPARENT, padding, arrayListOf(
        TextWatermark("[0]", 12f, Color.BLACK, WatermarkPosition.TOP_LEFT, 0f, 0f, WatermarkRotation.DEG_0, .8f, font),
        TextWatermark("[1]", 12f, Color.BLACK, WatermarkPosition.TOP_CENTER, 0f, 0f, WatermarkRotation.DEG_0, 0.8f, font),
        TextWatermark("[2]", 12f, Color.BLACK, WatermarkPosition.TOP_RIGHT, 0f, 0f, WatermarkRotation.DEG_0, .5f, font),
        TextWatermark("[3]", 12f, Color.BLACK, WatermarkPosition.MIDDLE_LEFT, 0f, 0f, WatermarkRotation.DEG_0, .99f, font),
        TextWatermark("[4]", 12f, Color.BLACK, WatermarkPosition.MIDDLE_CENTER, 0f, 0f, WatermarkRotation.DEG_0, 0.4f, font),
        TextWatermark("[5]", 12f, Color.BLACK, WatermarkPosition.MIDDLE_RIGHT, 0f, 0f, WatermarkRotation.DEG_0, 0.7f, font),
        TextWatermark("[6]", 12f, Color.BLACK, WatermarkPosition.BOTTOM_LEFT, 0f, 0f, WatermarkRotation.DEG_0, 1f, font),
        TextWatermark("[7]", 12f, Color.BLACK, WatermarkPosition.BOTTOM_CENTER, 0f, 0f, WatermarkRotation.DEG_0, 1f, font),
        TextWatermark("[8]", 12f, Color.BLACK, WatermarkPosition.BOTTOM_RIGHT, 0f, 0f, WatermarkRotation.DEG_0, 1f, font)
    ))
    TempFiles.saveBitmapToFile(context, ViewToImage.convert(context, config), "T1")

    val config2 = ViewToImageConfig(view, Color.WHITE, padding, arrayListOf(
        TextWatermark("[0]", 12f, Color.BLACK, WatermarkPosition.TOP_LEFT, 0f, 0f, WatermarkRotation.DEG_90, 1f, font),
        TextWatermark("[1]", 12f, Color.BLACK, WatermarkPosition.TOP_CENTER, 0f, 0f, WatermarkRotation.DEG_90, 1f, font),
        TextWatermark("[2]", 12f, Color.BLACK, WatermarkPosition.TOP_RIGHT, 0f, 0f, WatermarkRotation.DEG_90, 1f, font),
        TextWatermark("[3]", 12f, Color.BLACK, WatermarkPosition.MIDDLE_LEFT, 0f, 0f, WatermarkRotation.DEG_90, 1f, font),
        TextWatermark("[4]", 12f, Color.BLACK, WatermarkPosition.MIDDLE_CENTER, 0f, 0f, WatermarkRotation.DEG_90, 1f, font),
        TextWatermark("[5]", 12f, Color.BLACK, WatermarkPosition.MIDDLE_RIGHT, 0f, 0f, WatermarkRotation.DEG_90, 1f, font),
        TextWatermark("[6]", 12f, Color.BLACK, WatermarkPosition.BOTTOM_LEFT, 0f, 0f, WatermarkRotation.DEG_90, 1f, font),
        TextWatermark("[7]", 12f, Color.BLACK, WatermarkPosition.BOTTOM_CENTER, 0f, 0f, WatermarkRotation.DEG_90, 1f, font),
        TextWatermark("[8]", 12f, Color.BLACK, WatermarkPosition.BOTTOM_RIGHT, 0f, 0f, WatermarkRotation.DEG_90, 1f, font)
    ))
    TempFiles.saveBitmapToFile(context, ViewToImage.convert(context, config2), "T2")


    val config3 = ViewToImageConfig(view, Color.WHITE, padding, arrayListOf(
        TextWatermark("[0]", 12f, Color.BLACK, WatermarkPosition.TOP_LEFT, 0f, 0f, WatermarkRotation.DEG_180, 1f, font),
        TextWatermark("[1]", 12f, Color.BLACK, WatermarkPosition.TOP_CENTER, 0f, 0f, WatermarkRotation.DEG_180, 1f, font),
        TextWatermark("[2]", 12f, Color.BLACK, WatermarkPosition.TOP_RIGHT, 0f, 0f, WatermarkRotation.DEG_180, 1f, font),
        TextWatermark("[3]", 12f, Color.BLACK, WatermarkPosition.MIDDLE_LEFT, 0f, 0f, WatermarkRotation.DEG_180, 1f, font),
        TextWatermark("[4]", 12f, Color.BLACK, WatermarkPosition.MIDDLE_CENTER, 0f, 0f, WatermarkRotation.DEG_180, 1f, font),
        TextWatermark("[5]", 12f, Color.BLACK, WatermarkPosition.MIDDLE_RIGHT, 0f, 0f, WatermarkRotation.DEG_180, 1f, font),
        TextWatermark("[6]", 12f, Color.BLACK, WatermarkPosition.BOTTOM_LEFT, 0f, 0f, WatermarkRotation.DEG_180, 1f, font),
        TextWatermark("[7]", 12f, Color.BLACK, WatermarkPosition.BOTTOM_CENTER, 0f, 0f, WatermarkRotation.DEG_180, 1f, font),
        TextWatermark("[8]", 12f, Color.BLACK, WatermarkPosition.BOTTOM_RIGHT, 0f, 0f, WatermarkRotation.DEG_180, 1f, font)
    ))
    TempFiles.saveBitmapToFile(context, ViewToImage.convert(context, config3), "T3")

    val config4 = ViewToImageConfig(view, Color.WHITE, padding, arrayListOf(
        TextWatermark("[0]", 12f, Color.BLACK, WatermarkPosition.TOP_LEFT, 0f, 0f, WatermarkRotation.DEG_270, 1f, font),
        TextWatermark("[1]", 12f, Color.BLACK, WatermarkPosition.TOP_CENTER, 0f, 0f, WatermarkRotation.DEG_270, 1f, font),
        TextWatermark("[2]", 12f, Color.BLACK, WatermarkPosition.TOP_RIGHT, 0f, 0f, WatermarkRotation.DEG_270, 1f, font),
        TextWatermark("[3]", 12f, Color.BLACK, WatermarkPosition.MIDDLE_LEFT, 0f, 0f, WatermarkRotation.DEG_270, 1f, font),
        TextWatermark("[4]", 12f, Color.BLACK, WatermarkPosition.MIDDLE_CENTER, 0f, 0f, WatermarkRotation.DEG_270, 1f, font),
        TextWatermark("[5]", 12f, Color.BLACK, WatermarkPosition.MIDDLE_RIGHT, 0f, 0f, WatermarkRotation.DEG_270, 1f, font),
        TextWatermark("[6]", 12f, Color.BLACK, WatermarkPosition.BOTTOM_LEFT, 0f, 0f, WatermarkRotation.DEG_270, 1f, font),
        TextWatermark("[7]", 12f, Color.BLACK, WatermarkPosition.BOTTOM_CENTER, 0f, 0f, WatermarkRotation.DEG_270, 1f, font),
        TextWatermark("[8]", 12f, Color.BLACK, WatermarkPosition.BOTTOM_RIGHT, 0f, 0f, WatermarkRotation.DEG_270, 1f, font)
    ))
    TempFiles.saveBitmapToFile(context, ViewToImage.convert(context, config4), "T4")

    ///////////////////////////////////////

    val config5 = ViewToImageConfig(view, Color.WHITE, padding, arrayListOf(
        TextWatermark("[0]", 12f, Color.BLACK, WatermarkPosition.TOP_LEFT, 10f, 0f, WatermarkRotation.DEG_0, 1f, font),
        TextWatermark("[1]", 12f, Color.BLACK, WatermarkPosition.TOP_CENTER, 10f, 0f, WatermarkRotation.DEG_0, 1f, font),
        TextWatermark("[2]", 12f, Color.BLACK, WatermarkPosition.TOP_RIGHT, 10f, 0f, WatermarkRotation.DEG_0, 1f, font),
        TextWatermark("[3]", 12f, Color.BLACK, WatermarkPosition.MIDDLE_LEFT, 10f, 0f, WatermarkRotation.DEG_0, 1f, font),
        TextWatermark("[4]", 12f, Color.BLACK, WatermarkPosition.MIDDLE_CENTER, 10f, 0f, WatermarkRotation.DEG_0, 1f, font),
        TextWatermark("[5]", 12f, Color.BLACK, WatermarkPosition.MIDDLE_RIGHT, 10f, 0f, WatermarkRotation.DEG_0, 1f, font),
        TextWatermark("[6]", 12f, Color.BLACK, WatermarkPosition.BOTTOM_LEFT, 10f, 0f, WatermarkRotation.DEG_0, 1f, font),
        TextWatermark("[7]", 12f, Color.BLACK, WatermarkPosition.BOTTOM_CENTER, 10f, 0f, WatermarkRotation.DEG_0, 1f, font),
        TextWatermark("[8]", 12f, Color.BLACK, WatermarkPosition.BOTTOM_RIGHT, 10f, 0f, WatermarkRotation.DEG_0, 1f, font)
    ))
    TempFiles.saveBitmapToFile(context, ViewToImage.convert(context, config5), "T5")

    val config6 = ViewToImageConfig(view, Color.WHITE, padding, arrayListOf(
        TextWatermark("[0]", 12f, Color.BLACK, WatermarkPosition.TOP_LEFT, 0f, 10f, WatermarkRotation.DEG_90, 1f, font),
        TextWatermark("[1]", 12f, Color.BLACK, WatermarkPosition.TOP_CENTER, 0f, 10f, WatermarkRotation.DEG_90, 1f, font),
        TextWatermark("[2]", 12f, Color.BLACK, WatermarkPosition.TOP_RIGHT, 0f, 10f, WatermarkRotation.DEG_90, 1f, font),
        TextWatermark("[3]", 12f, Color.BLACK, WatermarkPosition.MIDDLE_LEFT, 0f, 10f, WatermarkRotation.DEG_90, 1f, font),
        TextWatermark("[4]", 12f, Color.BLACK, WatermarkPosition.MIDDLE_CENTER, 0f, 10f, WatermarkRotation.DEG_90, 1f, font),
        TextWatermark("[5]", 12f, Color.BLACK, WatermarkPosition.MIDDLE_RIGHT, 0f, 10f, WatermarkRotation.DEG_90, 1f, font),
        TextWatermark("[6]", 12f, Color.BLACK, WatermarkPosition.BOTTOM_LEFT, 0f, 10f, WatermarkRotation.DEG_90, 1f, font),
        TextWatermark("[7]", 12f, Color.BLACK, WatermarkPosition.BOTTOM_CENTER, 0f, 10f, WatermarkRotation.DEG_90, 1f, font),
        TextWatermark("[8]", 12f, Color.BLACK, WatermarkPosition.BOTTOM_RIGHT, 0f, 10f, WatermarkRotation.DEG_90, 1f, font)
    ))
    TempFiles.saveBitmapToFile(context, ViewToImage.convert(context, config6), "T6")


    val config7 = ViewToImageConfig(view, Color.WHITE, padding, arrayListOf(
        TextWatermark("[0]", 12f, Color.BLACK, WatermarkPosition.TOP_LEFT, 11f, 0f, WatermarkRotation.DEG_180, 1f, font),
        TextWatermark("[1]", 12f, Color.BLACK, WatermarkPosition.TOP_CENTER, 11f, 0f, WatermarkRotation.DEG_180, 1f, font),
        TextWatermark("[2]", 12f, Color.BLACK, WatermarkPosition.TOP_RIGHT, 11f, 0f, WatermarkRotation.DEG_180, 1f, font),
        TextWatermark("[3]", 12f, Color.BLACK, WatermarkPosition.MIDDLE_LEFT, 11f, 0f, WatermarkRotation.DEG_180, 1f, font),
        TextWatermark("[4]", 12f, Color.BLACK, WatermarkPosition.MIDDLE_CENTER, 11f, 0f, WatermarkRotation.DEG_180, 1f, font),
        TextWatermark("[5]", 12f, Color.BLACK, WatermarkPosition.MIDDLE_RIGHT, 11f, 0f, WatermarkRotation.DEG_180, 1f, font),
        TextWatermark("[6]", 12f, Color.BLACK, WatermarkPosition.BOTTOM_LEFT, 11f, 0f, WatermarkRotation.DEG_180, 1f, font),
        TextWatermark("[7]", 12f, Color.BLACK, WatermarkPosition.BOTTOM_CENTER, 11f, 0f, WatermarkRotation.DEG_180, 1f, font),
        TextWatermark("[8]", 12f, Color.BLACK, WatermarkPosition.BOTTOM_RIGHT, 11f, 0f, WatermarkRotation.DEG_180, 1f, font)
    ))
    TempFiles.saveBitmapToFile(context, ViewToImage.convert(context, config7), "T7")

    val config8 = ViewToImageConfig(view, Color.WHITE, padding, arrayListOf(
        TextWatermark("[0]", 12f, Color.BLACK, WatermarkPosition.TOP_LEFT, 0f, 11f, WatermarkRotation.DEG_270, 1f, font),
        TextWatermark("[1]", 12f, Color.BLACK, WatermarkPosition.TOP_CENTER, 0f, 11f, WatermarkRotation.DEG_270, 1f, font),
        TextWatermark("[2]", 12f, Color.BLACK, WatermarkPosition.TOP_RIGHT, 0f, 11f, WatermarkRotation.DEG_270, 1f, font),
        TextWatermark("[3]", 12f, Color.BLACK, WatermarkPosition.MIDDLE_LEFT, 0f, 11f, WatermarkRotation.DEG_270, 1f, font),
        TextWatermark("[4]", 12f, Color.BLACK, WatermarkPosition.MIDDLE_CENTER, 0f, 11f, WatermarkRotation.DEG_270, 1f, font),
        TextWatermark("[5]", 12f, Color.BLACK, WatermarkPosition.MIDDLE_RIGHT, 0f, 11f, WatermarkRotation.DEG_270, 1f, font),
        TextWatermark("[6]", 12f, Color.BLACK, WatermarkPosition.BOTTOM_LEFT, 0f, 11f, WatermarkRotation.DEG_270, 1f, font),
        TextWatermark("[7]", 12f, Color.BLACK, WatermarkPosition.BOTTOM_CENTER, 0f, 11f, WatermarkRotation.DEG_270, 1f, font),
        TextWatermark("[8]", 12f, Color.BLACK, WatermarkPosition.BOTTOM_RIGHT, 0f, 11f, WatermarkRotation.DEG_270, 1f, font)
    ))
    TempFiles.saveBitmapToFile(context, ViewToImage.convert(context, config8), "T8")



    // OTRAS PRUEBAS

//    //aaa yy II LL Ü Ó
//    val watermarkBase = TextWatermark("aaa yy II LL Ü Ó_", 12f, Color.BLACK, WatermarkPosition.TOP_LEFT, 0f, 0f, WatermarkRotation.DEG_0, 1f, null)
//    doGenerate(context, view, watermarkBase, "C")
//
//    val watermarkBase2 = TextWatermark("Test", 8f, Color.BLACK, WatermarkPosition.TOP_LEFT, 20f, 0f, WatermarkRotation.DEG_0, 1f, null)
//    doGenerate(context, view, watermarkBase2, "V")
//
//    val watermarkBase3 = TextWatermark("[0]", 20f, Color.BLACK, WatermarkPosition.TOP_LEFT, 0f, 30f, WatermarkRotation.DEG_0, 1f, null)
//    doGenerate(context, view, watermarkBase3, "H")

}

fun doGenerate(context: Context, view: View, watermarkBase: TextWatermark, shape: String) {

    val padding = Padding(50f)

    when (shape) {
        "C" -> Padding(50f)
        "V" -> Padding(100f, 50f)
        "H" -> Padding(50f, 100f)
    }

    val generate = { watermark: TextWatermark, fileName: String ->
        val config = ViewToImageConfig(view, Color.WHITE, padding, arrayListOf(watermark))
        TempFiles.saveBitmapToFile(context, ViewToImage.convert(context, config), fileName)
    }

    generate(watermarkBase.copy(position = WatermarkPosition.TOP_LEFT, rotation = WatermarkRotation.DEG_0), "TOP_LEFT-DEG_0-OFX${watermarkBase.offsetX.toInt()}-OFY${watermarkBase.offsetY.toInt()}-$shape")
    generate(watermarkBase.copy(position = WatermarkPosition.TOP_LEFT, rotation = WatermarkRotation.DEG_90), "TOP_LEFT-DEG_90${watermarkBase.offsetX.toInt()}-OFY${watermarkBase.offsetY.toInt()}-$shape")
    generate(watermarkBase.copy(position = WatermarkPosition.TOP_LEFT, rotation = WatermarkRotation.DEG_180), "TOP_LEFT-DEG_180${watermarkBase.offsetX.toInt()}-OFY${watermarkBase.offsetY.toInt()}-$shape")
    generate(watermarkBase.copy(position = WatermarkPosition.TOP_LEFT, rotation = WatermarkRotation.DEG_270), "TOP_LEFT-DEG_270${watermarkBase.offsetX.toInt()}-OFY${watermarkBase.offsetY.toInt()}-$shape")

    generate(watermarkBase.copy(position = WatermarkPosition.MIDDLE_LEFT, rotation = WatermarkRotation.DEG_0), "MIDDLE_LEFT-DEG_0${watermarkBase.offsetX.toInt()}-OFY${watermarkBase.offsetY.toInt()}-$shape")
    generate(watermarkBase.copy(position = WatermarkPosition.MIDDLE_LEFT, rotation = WatermarkRotation.DEG_90), "MIDDLE_LEFT-DEG_90${watermarkBase.offsetX.toInt()}-OFY${watermarkBase.offsetY.toInt()}-$shape")
    generate(watermarkBase.copy(position = WatermarkPosition.MIDDLE_LEFT, rotation = WatermarkRotation.DEG_180), "MIDDLE_LEFT-DEG_180${watermarkBase.offsetX.toInt()}-OFY${watermarkBase.offsetY.toInt()}-$shape")
    generate(watermarkBase.copy(position = WatermarkPosition.MIDDLE_LEFT, rotation = WatermarkRotation.DEG_270), "MIDDLE_LEFT-DEG_270${watermarkBase.offsetX.toInt()}-OFY${watermarkBase.offsetY.toInt()}-$shape")

    generate(watermarkBase.copy(position = WatermarkPosition.BOTTOM_LEFT, rotation = WatermarkRotation.DEG_0), "BOTTOM_LEFT-DEG_0${watermarkBase.offsetX.toInt()}-OFY${watermarkBase.offsetY.toInt()}-$shape")
    generate(watermarkBase.copy(position = WatermarkPosition.BOTTOM_LEFT, rotation = WatermarkRotation.DEG_90), "BOTTOM_LEFT-DEG_90${watermarkBase.offsetX.toInt()}-OFY${watermarkBase.offsetY.toInt()}-$shape")
    generate(watermarkBase.copy(position = WatermarkPosition.BOTTOM_LEFT, rotation = WatermarkRotation.DEG_180), "BOTTOM_LEFT-DEG_180${watermarkBase.offsetX.toInt()}-OFY${watermarkBase.offsetY.toInt()}-$shape")
    generate(watermarkBase.copy(position = WatermarkPosition.BOTTOM_LEFT, rotation = WatermarkRotation.DEG_270), "BOTTOM_LEFT-DEG_270${watermarkBase.offsetX.toInt()}-OFY${watermarkBase.offsetY.toInt()}-$shape")

    generate(watermarkBase.copy(position = WatermarkPosition.TOP_CENTER, rotation = WatermarkRotation.DEG_0), "TOP_CENTER-DEG_0${watermarkBase.offsetX.toInt()}-OFY${watermarkBase.offsetY.toInt()}-$shape")
    generate(watermarkBase.copy(position = WatermarkPosition.TOP_CENTER, rotation = WatermarkRotation.DEG_90), "TOP_CENTER-DEG_90${watermarkBase.offsetX.toInt()}-OFY${watermarkBase.offsetY.toInt()}-$shape")
    generate(watermarkBase.copy(position = WatermarkPosition.TOP_CENTER, rotation = WatermarkRotation.DEG_180), "TOP_CENTER-DEG_180${watermarkBase.offsetX.toInt()}-OFY${watermarkBase.offsetY.toInt()}-$shape")
    generate(watermarkBase.copy(position = WatermarkPosition.TOP_CENTER, rotation = WatermarkRotation.DEG_270), "TOP_CENTER-DEG_270${watermarkBase.offsetX.toInt()}-OFY${watermarkBase.offsetY.toInt()}-$shape")

    generate(watermarkBase.copy(position = WatermarkPosition.MIDDLE_CENTER, rotation = WatermarkRotation.DEG_0), "MIDDLE_CENTER-DEG_0${watermarkBase.offsetX.toInt()}-OFY${watermarkBase.offsetY.toInt()}-$shape")
    generate(watermarkBase.copy(position = WatermarkPosition.MIDDLE_CENTER, rotation = WatermarkRotation.DEG_90), "MIDDLE_CENTER-DEG_90${watermarkBase.offsetX.toInt()}-OFY${watermarkBase.offsetY.toInt()}-$shape")
    generate(watermarkBase.copy(position = WatermarkPosition.MIDDLE_CENTER, rotation = WatermarkRotation.DEG_180), "MIDDLE_CENTER-DEG_180${watermarkBase.offsetX.toInt()}-OFY${watermarkBase.offsetY.toInt()}-$shape")
    generate(watermarkBase.copy(position = WatermarkPosition.MIDDLE_CENTER, rotation = WatermarkRotation.DEG_270), "MIDDLE_CENTER-DEG_270${watermarkBase.offsetX.toInt()}-OFY${watermarkBase.offsetY.toInt()}-$shape")

    generate(watermarkBase.copy(position = WatermarkPosition.BOTTOM_CENTER, rotation = WatermarkRotation.DEG_0), "BOTTOM_CENTER-DEG_0${watermarkBase.offsetX.toInt()}-OFY${watermarkBase.offsetY.toInt()}-$shape")
    generate(watermarkBase.copy(position = WatermarkPosition.BOTTOM_CENTER, rotation = WatermarkRotation.DEG_90), "BOTTOM_CENTER-DEG_90${watermarkBase.offsetX.toInt()}-OFY${watermarkBase.offsetY.toInt()}-$shape")
    generate(watermarkBase.copy(position = WatermarkPosition.BOTTOM_CENTER, rotation = WatermarkRotation.DEG_180), "BOTTOM_CENTER-DEG_180${watermarkBase.offsetX.toInt()}-OFY${watermarkBase.offsetY.toInt()}-$shape")
    generate(watermarkBase.copy(position = WatermarkPosition.BOTTOM_CENTER, rotation = WatermarkRotation.DEG_270), "BOTTOM_CENTER-DEG_270${watermarkBase.offsetX.toInt()}-OFY${watermarkBase.offsetY.toInt()}-$shape")

    generate(watermarkBase.copy(position = WatermarkPosition.TOP_RIGHT, rotation = WatermarkRotation.DEG_0), "TOP_RIGHT-DEG_0${watermarkBase.offsetX.toInt()}-OFY${watermarkBase.offsetY.toInt()}-$shape")
    generate(watermarkBase.copy(position = WatermarkPosition.TOP_RIGHT, rotation = WatermarkRotation.DEG_90), "TOP_RIGHT-DEG_90${watermarkBase.offsetX.toInt()}-OFY${watermarkBase.offsetY.toInt()}-$shape")
    generate(watermarkBase.copy(position = WatermarkPosition.TOP_RIGHT, rotation = WatermarkRotation.DEG_180), "TOP_RIGHT-DEG_180${watermarkBase.offsetX.toInt()}-OFY${watermarkBase.offsetY.toInt()}-$shape")
    generate(watermarkBase.copy(position = WatermarkPosition.TOP_RIGHT, rotation = WatermarkRotation.DEG_270), "TOP_RIGHT-DEG_270${watermarkBase.offsetX.toInt()}-OFY${watermarkBase.offsetY.toInt()}-$shape")

    generate(watermarkBase.copy(position = WatermarkPosition.MIDDLE_RIGHT, rotation = WatermarkRotation.DEG_0), "MIDDLE_RIGHT-DEG_0${watermarkBase.offsetX.toInt()}-OFY${watermarkBase.offsetY.toInt()}-$shape")
    generate(watermarkBase.copy(position = WatermarkPosition.MIDDLE_RIGHT, rotation = WatermarkRotation.DEG_90), "MIDDLE_RIGHT-DEG_90${watermarkBase.offsetX.toInt()}-OFY${watermarkBase.offsetY.toInt()}-$shape")
    generate(watermarkBase.copy(position = WatermarkPosition.MIDDLE_RIGHT, rotation = WatermarkRotation.DEG_180), "MIDDLE_RIGHT-DEG_180${watermarkBase.offsetX.toInt()}-OFY${watermarkBase.offsetY.toInt()}-$shape")
    generate(watermarkBase.copy(position = WatermarkPosition.MIDDLE_RIGHT, rotation = WatermarkRotation.DEG_270), "MIDDLE_RIGHT-DEG_270${watermarkBase.offsetX.toInt()}-OFY${watermarkBase.offsetY.toInt()}-$shape")

    generate(watermarkBase.copy(position = WatermarkPosition.BOTTOM_RIGHT, rotation = WatermarkRotation.DEG_0), "BOTTOM_RIGHT-DEG_0${watermarkBase.offsetX.toInt()}-OFY${watermarkBase.offsetY.toInt()}-$shape")
    generate(watermarkBase.copy(position = WatermarkPosition.BOTTOM_RIGHT, rotation = WatermarkRotation.DEG_90), "BOTTOM_RIGHT-DEG_90${watermarkBase.offsetX.toInt()}-OFY${watermarkBase.offsetY.toInt()}-$shape")
    generate(watermarkBase.copy(position = WatermarkPosition.BOTTOM_RIGHT, rotation = WatermarkRotation.DEG_180), "BOTTOM_RIGHT-DEG_180${watermarkBase.offsetX.toInt()}-OFY${watermarkBase.offsetY.toInt()}-$shape")
    generate(watermarkBase.copy(position = WatermarkPosition.BOTTOM_RIGHT, rotation = WatermarkRotation.DEG_270), "BOTTOM_RIGHT-DEG_270${watermarkBase.offsetX.toInt()}-OFY${watermarkBase.offsetY.toInt()}-$shape")

}
