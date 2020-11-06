package com.jeovanimartinez.androidutils

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class Test {

    companion object {
        fun test(): String {


            return "Hola mundo"
        }

        fun mensaje(context: Context) {
            MaterialAlertDialogBuilder(context)
                .setTitle("libreria")
                .setMessage("hola")
                .setPositiveButton("aceptar") { dialog, which ->
                    // Respond to positive button press
                }
                .show()
        }
    }

}