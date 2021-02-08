package com.jeovanimartinez.androidutils

import android.content.Context
import java.lang.Exception

class ParseValue(private val context: Context) : Base<ParseValue>() {

    override val LOG_TAG = "ParseValue"

    fun toString(value: Any): String {
        return when (value) {
            is String -> {
                log("Value is String")
                return value
            }
            is Int -> {
                try {
                    context.getString(value)
                } catch (e: Exception) {
                    return value.toString()
                }
            }
            else -> {
                return value.toString()
            }
        }
    }

}