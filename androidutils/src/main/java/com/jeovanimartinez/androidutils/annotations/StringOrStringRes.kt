package com.jeovanimartinez.androidutils.annotations

/**
 * Indica que el valor esperado debe ser un dato tipo String, Char (ya que se puede representar como String) o el ID de un recuso de string.
 * Por ejemplo: 'a', "Hola", R.string.demo.
 * */
@MustBeDocumented
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.LOCAL_VARIABLE)
annotation class StringOrStringRes
