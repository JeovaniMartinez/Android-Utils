@file:Suppress("unused")

package com.jeovanimartinez.androidutils.extensions.basictypes

/**
 * Extensions for Float data type.
 * */

// Reference: https://www.arduino.cc/reference/en/language/functions/math/map/
/**
 * Re-maps a number from one range to another. That is, a value of [inMin] would get mapped to [outMin],
 * a value of [inMax] to [outMax], values in-between to values in-between, etc.
 *
 * > For example, inMin = 0, inMax = 1, outMin = 0, outMin = 255 - In this case, the input value
 * must be between 0 and 1, and it will be mapped to an output between 0 and 255. If the value
 * input is 0.5, output is 127.5. In this same example, a value of 2 would result in 510, since
 * it is double according to the established values.
 *
 * @param inMin The lower bound of the value’s current range.
 * @param inMax The upper bound of the value’s current range.
 * @param outMin The lower bound of the value’s target range.
 * @param outMax The upper bound of the value’s target range.
 *
 * */
fun Float.mapValue(inMin: Float, inMax: Float, outMin: Float, outMax: Float): Float {
    return (this - inMin) * (outMax - outMin) / (inMax - inMin) + outMin
}
