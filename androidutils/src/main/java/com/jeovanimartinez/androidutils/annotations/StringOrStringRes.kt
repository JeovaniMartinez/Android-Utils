package com.jeovanimartinez.androidutils.annotations

/**
 * Indica que el valor esperado debe ser un dato tipo String o el ID de un recuso de string.
 * Por ejemplo: “Hola”, R.string.ok.
 * */
@MustBeDocumented
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY_SETTER)
annotation class StringOrStringRes
