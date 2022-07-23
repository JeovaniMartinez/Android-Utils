@file:Suppress("UNUSED_PARAMETER")

package com.jeovanimartinez.androidutils.app.annotations

/**
 * File for testing the code inspection annotations and the functioning of the Lint Check module.
 * ** Keep all code commented, uncomment only when required. **
 * */

//import android.app.Activity
//import android.graphics.Color
//import android.graphics.drawable.ShapeDrawable
//import android.graphics.drawable.shapes.RoundRectShape
//import com.jeovanimartinez.androidutils.annotations.DrawableOrDrawableRes
//import com.jeovanimartinez.androidutils.annotations.StringOrStringRes
//import com.jeovanimartinez.androidutils.app.R
//
//private fun showImage(@DrawableOrDrawableRes image: Any) {
//    // ...
//}
//
//private fun showMessage(@StringOrStringRes message: Any) {
//    // ...
//}
//
//private fun demo() {
//
//    val activity = Activity()
//
//    val shape = ShapeDrawable(
//        RoundRectShape(
//            floatArrayOf(8f, 8f, 8f, 8f, 8f, 8f, 8f, 8f), null, null
//        )
//    )
//    shape.paint.color = Color.RED
//
//    /** Ok, using a drawable resource. **/
//    showImage(R.drawable.library_logo)
//
//    /** Ok, shape is an instance of a drawable class. **/
//    showImage(shape)
//
//    /** ERROR: Invalid resource. Expected drawable resource or drawable object. **/
//    showImage(R.color.colorAccent)
//
//    /**
//     * In some cases, the data type cannot be detected at compile time, so an
//     * exception will be thrown at runtime to indicate the error if the data
//     * type is wrong.
//     **/
//    showImage(activity)
//
//    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    /** Ok, using a String. **/
//    showMessage("Hello")
//
//    /** Ok, the char literal can be interpreted as a String. **/
//    showMessage('h')
//
//    /** Ok, using a string resource. **/
//    showMessage(R.string.app_name)
//
//    /** ERROR: Invalid resource. Expected string resource or string object. **/
//    showMessage(R.color.colorAccent)
//
//    /** ERROR: Invalid type. Expected string object or string resource. **/
//    showMessage(55)
//
//    /**
//     * In some cases, the data type cannot be detected at compile time, so an
//     * exception will be thrown at runtime to indicate the error if the data
//     * type is wrong.
//     **/
//    showMessage(activity)
//
//}
