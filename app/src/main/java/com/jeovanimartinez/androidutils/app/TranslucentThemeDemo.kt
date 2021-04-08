package com.jeovanimartinez.androidutils.app

import android.os.Bundle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jeovanimartinez.androidutils.app.databinding.ActivityTranslucentDemoBinding
import com.jeovanimartinez.androidutils.themes.translucent.TranslucentActivity

class TranslucentThemeDemo : TranslucentActivity() {

    private lateinit var binding: ActivityTranslucentDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.activityOpacity = intent.extras!!.getFloat("opacity")
        super.onCreate(savedInstanceState)
        binding = ActivityTranslucentDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Example calling configureWindowDim()
        binding.showDialogBtn1.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(this@TranslucentThemeDemo)
                .setTitle("Android Utils")
                .setPositiveButton("Ok") { _, _ -> }
                .show()
            configureWindowDim(dialog.window)
        }

        // Example without calling configureWindowDim()
        binding.showDialogBtn2.setOnClickListener {
            MaterialAlertDialogBuilder(this@TranslucentThemeDemo)
                .setTitle("Android Utils")
                .setPositiveButton("Ok") { _, _ -> }
                .show()
        }

    }

}
