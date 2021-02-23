@file:Suppress("unused")

package com.jeovanimartinez.androidutils.graphics.utils

/**
 * Configuración de márgenes.
 * @param top Margen superior.
 * @param right Margen derecho.
 * @param bottom Margen inferior.
 * @param left Margen izquierdo.
 * */
data class Margin(val top: Float, val right: Float, val bottom: Float, val left: Float) {

    /**
     * Configuración de márgenes.
     * @param all Valor para aplicar a todos los márgenes (superior, derecho, inferior e izquierdo).
     * */
    constructor(all: Float) : this(all, all, all, all)

    /**
     * Configuración de márgenes.
     * @param vertical Valor para el margen superior e inferior.
     * @param horizontal Valor para el margen derecho e izquierdo.
     * */
    constructor(vertical: Float, horizontal: Float) : this(vertical, horizontal, vertical, horizontal)

}
