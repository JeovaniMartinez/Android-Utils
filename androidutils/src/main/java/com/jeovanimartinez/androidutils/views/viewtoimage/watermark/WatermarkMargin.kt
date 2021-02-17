package com.jeovanimartinez.androidutils.views.viewtoimage.watermark

/**
 * Márgenes para la marca de agua, todos los valores se manejan como dp.
 * @param top Margen superior.
 * @param right Margen derecho.
 * @param bottom Margen inferior.
 * @param left Margen izquierdo.
 * */
data class WatermarkMargin(val top: Float, val right: Float, val bottom: Float, val left: Float) {
    /**
     * Márgenes para la marca de agua, todos los valores se manejan como dp.
     * @param all Valor para aplicar a todos los márgenes (superior, derecho, inferior e izquierdo).
     * */
    constructor(all: Float) : this(all, all, all, all)
}
