package com.jeovanimartinez.androidutils.app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jeovanimartinez.androidutils.app.R
import com.jeovanimartinez.androidutils.app.databinding.ActivityWatermarkBinding

/** WatermarkActivity */
class WatermarkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWatermarkBinding

    private val imageList = arrayListOf(
        R.drawable.watermark_img1, R.drawable.watermark_img2, R.drawable.watermark_img3,
        R.drawable.watermark_img4, R.drawable.watermark_img5
    )
    private var currentImage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWatermarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appBar.btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.appBar.tvTitle.text = getString(R.string.watermark_title)

        imageActionsSetup()

    }

    /** Image actions setup */
    private fun imageActionsSetup() {

        binding.btnPreviousImage.setOnClickListener {
            if (currentImage <= 0) currentImage = imageList.size - 1
            else currentImage--
            binding.ivWatermark.setImageResource(imageList[currentImage])
        }

        binding.btnNextImage.setOnClickListener {
            if (currentImage >= imageList.size - 1) currentImage = 0
            else currentImage++
            binding.ivWatermark.setImageResource(imageList[currentImage])
        }

    }

}
