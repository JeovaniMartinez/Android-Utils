package com.jeovanimartinez.androidutils.annotations

/**
 * Indica que el valor esperado debe ser un color-int o el ID de un recuso de color.
 * Por ejemplo: para el color negro, se puede usar Color.parseColor("#000000") que da como resultado -16777216 que es su representación
 * en color-int, o bien R.color.demo para usar un color de color res.
 * */
@MustBeDocumented
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.LOCAL_VARIABLE)
annotation class ColorOrColorRes
