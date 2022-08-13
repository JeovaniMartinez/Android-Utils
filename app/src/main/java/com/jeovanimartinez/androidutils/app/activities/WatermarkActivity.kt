package com.jeovanimartinez.androidutils.app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jeovanimartinez.androidutils.app.R
import com.jeovanimartinez.androidutils.app.databinding.ActivityWatermarkBinding

/** Base activity for a new utility */
class WatermarkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWatermarkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWatermarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appBar.btnBack.setOnClickListener { super.onBackPressed() }
        binding.appBar.tvTitle.text = getString(R.string.watermark_title)

    }

}
