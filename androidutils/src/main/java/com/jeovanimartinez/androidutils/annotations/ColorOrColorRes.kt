package com.jeovanimartinez.androidutils.annotations

/**
 * Indica que el valor esperado debe ser un color-int o el ID de un recuso de color.
 * Por ejemplo: para el color negro, se puede usar Color.parseColor("#000000") que da como resultado -16777216 que es su representación
 * en color-int, o bien R.color.demo para usar un color de color res.
 * */
@MustBeDocumented
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.LOCAL_VARIABLE)
@Deprecated("Tanto color-int como color res son valores tipo int, por lo cual el uso de esta anotación sería ambiguo, ya que, en el valor asignado, no se podría saber si se trata de un color-int o un resource.")
annotation class ColorOrColorRes
