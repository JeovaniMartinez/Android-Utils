package com.jeovanimartinez.androidutils.app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jeovanimartinez.androidutils.app.R
import com.jeovanimartinez.androidutils.app.databinding.ActivityRateAppBinding

/** RateAppActivity */
class RateAppActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRateAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRateAppBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_rate_app)
    }

}
