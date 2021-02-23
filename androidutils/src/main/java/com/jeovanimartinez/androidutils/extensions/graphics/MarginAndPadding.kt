@file:Suppress("unused")

package com.jeovanimartinez.androidutils.extensions.graphics

import android.content.Context
import com.jeovanimartinez.androidutils.extensions.dimension.dp2px
import com.jeovanimartinez.androidutils.graphics.utils.Margin
import com.jeovanimartinez.androidutils.graphics.utils.Padding

/**
 * Conjunto de extensiones para las clases Margin y Padding
 * */

/**
 * Convierte todos los valores del margen de píxeles independientes de la densidad (dp) a píxeles (px) y los devuelve en un nuevo objeto.
 * Por ejemplo, si el valor de margin top = 10, se va a asumir que este valor esta representado en dp, por lo que se realiza la conversión
 * a píxeles de acuerdo a la densidad de la pantalla, y el resultado de esa conversión será el margin top del objeto que se devuelve.
 * */
fun Margin.dp2px(context: Context): Margin {
    val top = context.dp2px(this.top)
    val right = context.dp2px(this.right)
    val bottom = context.dp2px(this.bottom)
    val left = context.dp2px(this.left)
    return Margin(top.toFloat(), right.toFloat(), bottom.toFloat(), left.toFloat())
}

/**
 * Convierte todos los valores del margen de píxeles independientes de la densidad (dp) a píxeles (px) y los devuelve en un nuevo objeto.
 * Por ejemplo, si el valor de margin top = 10, se va a asumir que este valor esta representado en dp, por lo que se realiza la conversión
 * a píxeles de acuerdo a la densidad de la pantalla, y el resultado de esa conversión será el margin top del objeto que se devuelve.
 * */
fun Padding.dp2px(context: Context): Padding {
    val top = context.dp2px(this.top)
    val right = context.dp2px(this.right)
    val bottom = context.dp2px(this.bottom)
    val left = context.dp2px(this.left)
    return Padding(top.toFloat(), right.toFloat(), bottom.toFloat(), left.toFloat())
}
