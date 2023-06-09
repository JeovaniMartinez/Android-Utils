package com.jeovanimartinez.androidutils.app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.jeovanimartinez.androidutils.app.R
import com.jeovanimartinez.androidutils.app.databinding.ActivityWatermarkBinding
import com.jeovanimartinez.androidutils.extensions.activity.configureTaskDescription
import com.jeovanimartinez.androidutils.extensions.context.getColorCompat
import com.jeovanimartinez.androidutils.graphics.utils.Dimension
import com.jeovanimartinez.androidutils.watermark.config.WatermarkPosition

/** WatermarkActivity */
class WatermarkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWatermarkBinding

    private val imageList = arrayListOf(
        R.drawable.watermark_img1, R.drawable.watermark_img2, R.drawable.watermark_img3,
        R.drawable.watermark_img4, R.drawable.watermark_img5
    )
    private var currentImage = 0

    // Common watermark properties
    private var watermarkPosition = WatermarkPosition.ABSOLUTE
    private var watermarkMeasurementDimension = Dimension.PX

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWatermarkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configureTaskDescription(R.string.app_name, R.mipmap.ic_launcher, getColorCompat(R.color.md_theme_background))

        binding.appBar.btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.appBar.tvTitle.text = getString(R.string.watermark_title)

        imageActionsSetup()
        commonWatermarkPropertiesSetup()
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

    /** Common watermark properties setup */
    private fun commonWatermarkPropertiesSetup() {

        val positionsMenu = binding.menuPosition.editText as MaterialAutoCompleteTextView
        val positionsAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, resources.getStringArray(R.array.watermark_positions_array))
        positionsMenu.setAdapter(positionsAdapter)
        positionsMenu.setText(positionsAdapter.getItem(0).toString(), false)
        positionsMenu.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            watermarkPosition = WatermarkPosition.values()[position]
        }

        val measurementDimensionMenu = binding.menuMeasurementDimension.editText as MaterialAutoCompleteTextView
        val measurementDimensionAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, resources.getStringArray(R.array.watermark_dimensions_array))
        measurementDimensionMenu.setAdapter(measurementDimensionAdapter)
        measurementDimensionMenu.setText(measurementDimensionAdapter.getItem(0).toString(), false)
        measurementDimensionMenu.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            watermarkMeasurementDimension = Dimension.values()[position]
        }

    }

}
