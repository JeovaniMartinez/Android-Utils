package com.jeovanimartinez.androidutils.about

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jeovanimartinez.androidutils.R
import kotlinx.android.synthetic.main.activity_about.*

/** Actividad de acerca de */
class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        AboutApp.appName?.let { aboutTMP.text = it }

    }
}
