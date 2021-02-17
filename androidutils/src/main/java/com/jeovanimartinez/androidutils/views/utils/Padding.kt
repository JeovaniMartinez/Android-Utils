@file:Suppress("unused")

package com.jeovanimartinez.androidutils.views.utils

/**
 * Configuración de rellenos (padding).
 * @param top Padding superior.
 * @param right Padding derecho.
 * @param bottom Padding inferior.
 * @param left Padding izquierdo.
 * */
data class Padding(val top: Float, val right: Float, val bottom: Float, val left: Float) {

    /**
     * Configuración de rellenos (padding).
     * @param all Valor para aplicar a todos los padding (superior, derecho, inferior e izquierdo).
     * */
    constructor(all: Float) : this(all, all, all, all)

    /**
     * Configuración de rellenos (padding).
     * @param vertical Valor para el padding superior e inferior.
     * @param horizontal Valor para el padding derecho e izquierdo.
     * */
    constructor(vertical: Float, horizontal: Float) : this(vertical, horizontal, vertical, horizontal)

}
