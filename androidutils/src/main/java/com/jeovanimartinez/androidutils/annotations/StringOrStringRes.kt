package com.jeovanimartinez.androidutils.annotations

/**
 * Indicates that the expected value or object must be a data type String, Char (since it can be represented as String)
 * or the ID of a string resource. For example: 'a', "Hello", R.string.demo
 * */
@MustBeDocumented
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.LOCAL_VARIABLE)
annotation class StringOrStringRes
