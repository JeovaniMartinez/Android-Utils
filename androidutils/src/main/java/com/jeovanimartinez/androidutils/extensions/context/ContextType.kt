package com.jeovanimartinez.androidutils.extensions.context

import android.app.Application
import android.content.Context

/**
 * Extensions to determine the type of context.
 * */

/**
 * Determine if the current context is the application context and not that of an activity or any other context type.
 * @return true if the context is the application context, false otherwise.
 * */
fun Context.isApplicationContext(): Boolean {
    return this is Application
}
