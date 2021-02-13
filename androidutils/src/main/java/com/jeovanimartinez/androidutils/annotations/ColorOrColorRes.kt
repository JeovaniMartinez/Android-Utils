package com.jeovanimartinez.androidutils.annotations

/**
 * Indica que el valor esperado debe ser un color Int o el ID de un recuso de color.
 * Por ejemplo: PENDIENTE
 * */
@MustBeDocumented
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.LOCAL_VARIABLE)
annotation class ColorOrColorRes
