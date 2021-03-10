package com.jeovanimartinez.androidutils.app

import android.os.Bundle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jeovanimartinez.androidutils.themes.translucent.TranslucentActivity
import kotlinx.android.synthetic.main.activity_translucent_demo.*

class TranslucentThemeDemo : TranslucentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.activityOpacity = intent.extras!!.getFloat("opacity")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translucent_demo)

        showDialogBtn1.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(this@TranslucentThemeDemo)
                .setTitle("Android Utils")
                .setPositiveButton("Ok") { _, _ -> }
                .show()
            configureWindowDim(dialog.window)
        }

        showDialogBtn2.setOnClickListener {
            MaterialAlertDialogBuilder(this@TranslucentThemeDemo)
                .setTitle("Android Utils")
                .setPositiveButton("Ok") { _, _ -> }
                .show()
        }

    }

}
