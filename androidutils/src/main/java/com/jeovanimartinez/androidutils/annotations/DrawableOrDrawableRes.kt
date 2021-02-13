package com.jeovanimartinez.androidutils.annotations

/**
 * Indica que el valor esperado debe ser un Drawable o el ID de un recuso de drawable.
 * Por ejemplo: drawableObject, R.drawable.demo.
 * */
@MustBeDocumented
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.LOCAL_VARIABLE)
annotation class DrawableOrDrawableRes
