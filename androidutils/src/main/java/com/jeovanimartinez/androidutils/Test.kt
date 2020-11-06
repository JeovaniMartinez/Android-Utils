package com.jeovanimartinez.androidutils

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.analytics.FirebaseAnalytics

class Test {

    companion object {
        fun test(instance: FirebaseAnalytics?): String {

            instance?.logEvent("Test", null)

            return "Hola mundo"
        }

        fun mensaje(context: Context) {
            MaterialAlertDialogBuilder(context)
                .setTitle("libreria")
                .setMessage(context.packageName)
                .setPositiveButton("aceptar") { dialog, which ->
                    // Respond to positive button press
                }
                .show()
        }
    }

}