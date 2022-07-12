package com.jeovanimartinez.androidutils.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jeovanimartinez.androidutils.app.databinding.ActivityMainBinding
import com.jeovanimartinez.androidutils.extensions.activity.configureTaskDescription
import com.jeovanimartinez.androidutils.extensions.context.getColorCompat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configureTaskDescription(R.string.app_name, R.mipmap.ic_launcher, getColorCompat(R.color.colorBackground))

        initialSetup()
    }

    /** Initial setup */
    private fun initialSetup() {

    }

}
