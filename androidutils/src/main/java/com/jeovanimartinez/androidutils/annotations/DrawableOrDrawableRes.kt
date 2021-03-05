package com.jeovanimartinez.androidutils.annotations

/**
 * Indicates that the expected value or object should be a Drawable object or the ID of a drawable resource.
 * For example: drawableObject, R.drawable.demo.
 * */
@MustBeDocumented
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.LOCAL_VARIABLE)
annotation class DrawableOrDrawableRes
