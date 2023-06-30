package com.jeovanimartinez.androidutils.app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.tabs.TabLayout
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

    private val baseImageList = arrayListOf(
        R.drawable.watermark_base_image_1, R.drawable.watermark_base_image_2, R.drawable.watermark_base_image_3,
        R.drawable.watermark_base_image_4, R.drawable.watermark_base_image_5
    )
    private var currentBaseImage = 0

    /*
    * ** IMPORTANT **
    * Numeric properties for the watermark are stored as String for easier assignment. During the
    * watermark drawing process, they are converted to the appropriate data type.
    * */

    // Common watermark properties
    private var watermarkPosition = WatermarkPosition.ABSOLUTE
    private var watermarkMeasurementDimension = Dimension.PX
    private var watermarkDx = "0"
    private var watermarkDy = "0"
    private var watermarkRotation = "0"
    private var watermarkOpacity = "1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWatermarkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configureTaskDescription(R.string.app_name, R.mipmap.ic_launcher, getColorCompat(R.color.md_theme_background))

        binding.appBar.btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.appBar.tvTitle.text = getString(R.string.watermark_title)

        uiSetup()
        imageActionsSetup()
        commonWatermarkPropertiesSetup()
    }

    /** User interface setup */
    private fun uiSetup() {

        // Default
        binding.layoutDrawableWatermark.visibility = View.VISIBLE
        binding.layoutTextWatermark.visibility = View.GONE

        binding.tabLayoutWatermarkType.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        binding.layoutDrawableWatermark.visibility = View.VISIBLE
                        binding.layoutTextWatermark.visibility = View.GONE
                    }

                    1 -> {
                        binding.layoutDrawableWatermark.visibility = View.GONE
                        binding.layoutTextWatermark.visibility = View.VISIBLE
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

    }

    /** Image actions setup */
    private fun imageActionsSetup() {

        binding.btnPreviousImage.setOnClickListener {
            if (currentBaseImage <= 0) currentBaseImage = baseImageList.size - 1
            else currentBaseImage--
            binding.ivBaseImage.setImageResource(baseImageList[currentBaseImage])
        }

        binding.btnNextImage.setOnClickListener {
            if (currentBaseImage >= baseImageList.size - 1) currentBaseImage = 0
            else currentBaseImage++
            binding.ivBaseImage.setImageResource(baseImageList[currentBaseImage])
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

        binding.etDx.doAfterTextChanged {
            watermarkDx = it.toString()
        }

        binding.etDy.doAfterTextChanged {
            watermarkDy = it.toString()
        }

        binding.etRotation.doAfterTextChanged {
            watermarkRotation = it.toString()
        }

        binding.etOpacity.doAfterTextChanged {
            watermarkOpacity = it.toString()
        }

    }

}
