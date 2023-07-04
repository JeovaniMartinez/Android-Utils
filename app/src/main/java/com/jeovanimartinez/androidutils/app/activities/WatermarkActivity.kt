package com.jeovanimartinez.androidutils.app.activities

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.drawToBitmap
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialog.TYPE_CUSTOM
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import com.jeovanimartinez.androidutils.app.R
import com.jeovanimartinez.androidutils.app.databinding.ActivityWatermarkBinding
import com.jeovanimartinez.androidutils.app.enums.TextWatermarkTypeface
import com.jeovanimartinez.androidutils.extensions.activity.configureTaskDescription
import com.jeovanimartinez.androidutils.extensions.context.getColorCompat
import com.jeovanimartinez.androidutils.extensions.context.getFontCompat
import com.jeovanimartinez.androidutils.graphics.utils.Dimension
import com.jeovanimartinez.androidutils.watermark.Watermark
import com.jeovanimartinez.androidutils.watermark.WatermarkUtils
import com.jeovanimartinez.androidutils.watermark.config.WatermarkPosition
import com.jeovanimartinez.androidutils.watermark.config.WatermarkShadow

/** WatermarkActivity */
class WatermarkActivity : AppCompatActivity(), ColorPickerDialogListener {

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
    private var watermarkPosition = WatermarkPosition.MIDDLE_CENTER
    private var watermarkMeasurementDimension = Dimension.PX
    private var watermarkDx = "0"
    private var watermarkDy = "0"
    private var watermarkRotation = "0"
    private var watermarkOpacity = "1"

    // Drawable watermark properties
    private lateinit var watermarkDrawable: Drawable
    private var watermarkWidth = "100"
    private var watermarkHeight = "100"

    // Text watermark properties
    private lateinit var watermarkText: String
    private var watermarkTextSize = "50"
    private lateinit var watermarkTextColor: String
    private var watermarkTypeface = TextWatermarkTypeface.DEFAULT
    private var watermarkShadow = false
    private var watermarkShadowRadius = "2"
    private lateinit var watermarkShadowColor: String
    private var watermarkShadowDx = "0"
    private var watermarkShadowDy = "0"

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
        drawableWatermarkSetup()
        textWatermarkSetup()
    }

    /** User interface setup */
    private fun uiSetup() {

        // Default
        binding.layoutDrawableWatermark.visibility = View.VISIBLE
        binding.layoutTextWatermark.visibility = View.GONE
        binding.btnDrawDrawableWatermark.visibility = View.VISIBLE
        binding.btnDrawTextWatermark.visibility = View.GONE

        binding.tabLayoutWatermarkType.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        binding.layoutDrawableWatermark.visibility = View.VISIBLE
                        binding.layoutTextWatermark.visibility = View.GONE
                        binding.btnDrawDrawableWatermark.visibility = View.VISIBLE
                        binding.btnDrawTextWatermark.visibility = View.GONE
                    }

                    1 -> {
                        binding.layoutDrawableWatermark.visibility = View.GONE
                        binding.layoutTextWatermark.visibility = View.VISIBLE
                        binding.btnDrawDrawableWatermark.visibility = View.GONE
                        binding.btnDrawTextWatermark.visibility = View.VISIBLE
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
            binding.svMain.smoothScrollTo(0, 0)
        }

        binding.btnNextImage.setOnClickListener {
            if (currentBaseImage >= baseImageList.size - 1) currentBaseImage = 0
            else currentBaseImage++
            binding.ivBaseImage.setImageResource(baseImageList[currentBaseImage])
            binding.svMain.smoothScrollTo(0, 0)
        }

        binding.btnRestoreImage.setOnClickListener {
            binding.ivBaseImage.setImageResource(baseImageList[currentBaseImage])
            binding.svMain.smoothScrollTo(0, 0)
        }

    }

    /** Common watermark properties setup */
    private fun commonWatermarkPropertiesSetup() {

        val positionsMenu = binding.menuPosition.editText as MaterialAutoCompleteTextView
        val positionsAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, resources.getStringArray(R.array.watermark_positions_array))
        positionsMenu.setAdapter(positionsAdapter)
        positionsMenu.setText(positionsAdapter.getItem(5).toString(), false)
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

    /** Drawable watermark setup */
    private fun drawableWatermarkSetup() {

        watermarkDrawable = binding.ivWatermark1.drawable
        binding.rbWatermark1.isChecked = true
        binding.rbWatermark2.isChecked = false
        binding.rbWatermark3.isChecked = false

        binding.rbWatermark1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                watermarkDrawable = binding.ivWatermark1.drawable
                binding.rbWatermark1.isChecked = true
                binding.rbWatermark2.isChecked = false
                binding.rbWatermark3.isChecked = false
            }
        }

        binding.rbWatermark2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                watermarkDrawable = binding.ivWatermark2.drawable
                binding.rbWatermark1.isChecked = false
                binding.rbWatermark2.isChecked = true
                binding.rbWatermark3.isChecked = false
            }
        }

        binding.rbWatermark3.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                watermarkDrawable = binding.ivWatermark3.drawable
                binding.rbWatermark1.isChecked = false
                binding.rbWatermark2.isChecked = false
                binding.rbWatermark3.isChecked = true
            }
        }

        binding.etWidth.doAfterTextChanged {
            watermarkWidth = it.toString()
        }

        binding.etHeight.doAfterTextChanged {
            watermarkHeight = it.toString()
        }

        binding.btnDrawDrawableWatermark.setOnClickListener {

            val drawableWatermark: Watermark.Drawable

            /*
            * The watermark object is generated by converting the input values to the appropriate data type.
            * If it's not possible, an error message is showed, and the operation is aborted.
            * */
            try {
                drawableWatermark = Watermark.Drawable(
                    drawable = watermarkDrawable,
                    position = watermarkPosition,
                    width = watermarkWidth.toFloat(),
                    height = watermarkHeight.toFloat(),
                    dx = watermarkDx.toFloat(),
                    dy = watermarkDy.toFloat(),
                    rotation = watermarkRotation.toFloat(),
                    opacity = watermarkOpacity.toFloat(),
                    measurementDimension = watermarkMeasurementDimension
                )
            } catch (err: Exception) {
                MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.watermark_convert_values_error_title)
                    .setMessage(R.string.watermark_convert_values_error_msg)
                    .setPositiveButton(R.string.ok, null)
                    .show()
                return@setOnClickListener
            }

            // A bitmap of the current base image is generated to process it
            val baseImageBitmap = binding.ivBaseImage.drawToBitmap()

            // The watermark is drawn using the library's utility
            WatermarkUtils.drawWatermark(this@WatermarkActivity, baseImageBitmap, drawableWatermark)

            // The image is assigned to the image view
            binding.ivBaseImage.setImageBitmap(baseImageBitmap)

            // Scrolling up is done to see the result
            binding.svMain.smoothScrollTo(0, 0)

            /*
            android.util.Log.d(
                "WatermarkActivityTest", """
                watermarkDrawable $watermarkDrawable
                watermarkWidth $watermarkWidth
                watermarkHeight $watermarkHeight
                watermarkPosition $watermarkPosition
                watermarkMeasurementDimension $watermarkMeasurementDimension
                watermarkDx $watermarkDx
                watermarkDy $watermarkDy
                watermarkRotation $watermarkRotation
                watermarkOpacity $watermarkOpacity
            """.trimIndent()
            )
            */

        }

    }

    /** Text watermark setup */
    private fun textWatermarkSetup() {

        binding.etText.requestFocus()
        watermarkText = getString(R.string.app_name)
        watermarkTextColor = getColorCompat(R.color.watermark_default_text_color).toString()
        watermarkShadowColor = getColorCompat(R.color.watermark_default_text_shadow_color).toString()
        binding.layoutShadow.visibility = View.GONE

        binding.etText.doAfterTextChanged {
            watermarkText = it.toString()
        }

        binding.etTextSize.doAfterTextChanged {
            watermarkTextSize = it.toString()
        }

        binding.btnTextColor.setOnClickListener {
            showColorPickerDialog(0)
        }

        val typefacesMenu = binding.menuTypeface.editText as MaterialAutoCompleteTextView
        val typefacesAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, resources.getStringArray(R.array.watermark_typefaces_array))
        typefacesMenu.setAdapter(typefacesAdapter)
        typefacesMenu.setText(typefacesAdapter.getItem(0).toString(), false)
        typefacesMenu.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            watermarkTypeface = TextWatermarkTypeface.values()[position]
        }

        binding.swShadow.setOnCheckedChangeListener { _, isChecked ->
            binding.layoutShadow.visibility = if (isChecked) View.VISIBLE else View.GONE
            watermarkShadow = isChecked
        }

        binding.etShadowRadius.doAfterTextChanged {
            watermarkShadowRadius = it.toString()
        }

        binding.etShadowDx.doAfterTextChanged {
            watermarkShadowDx = it.toString()
        }

        binding.etShadowDy.doAfterTextChanged {
            watermarkShadowDy = it.toString()
        }

        binding.btnShadowColor.setOnClickListener {
            showColorPickerDialog(1)
        }

        binding.btnDrawTextWatermark.setOnClickListener {

            val textWatermark: Watermark.Text

            /*
            * The watermark object is generated by converting the input values to the appropriate data type.
            * If it's not possible, an error message is showed, and the operation is aborted.
            * */
            try {

                // The selected font is assigned
                val typeface = when (watermarkTypeface) {
                    TextWatermarkTypeface.DEFAULT -> null
                    TextWatermarkTypeface.BALLET -> getFontCompat(R.font.ballet_regular)
                    TextWatermarkTypeface.FUGAZ_ONE -> getFontCompat(R.font.fugaz_one_regular)
                    TextWatermarkTypeface.KARLA -> getFontCompat(R.font.karla_regular)
                    TextWatermarkTypeface.OI -> getFontCompat(R.font.oi_regular)
                }

                // The shadow is configured (if applicable)
                val shadow = if (watermarkShadow) {
                    WatermarkShadow(
                        watermarkShadowRadius.toFloat(), watermarkShadowDx.toFloat(), watermarkShadowDy.toFloat(), watermarkShadowColor.toInt()
                    )
                } else {
                    null
                }

                textWatermark = Watermark.Text(
                    text = watermarkText,
                    textSize = watermarkTextSize.toFloat(),
                    textColor = watermarkTextColor.toInt(),
                    position = watermarkPosition,
                    dx = watermarkDx.toFloat(),
                    dy = watermarkDy.toFloat(),
                    rotation = watermarkRotation.toFloat(),
                    opacity = watermarkOpacity.toFloat(),
                    typeface = typeface,
                    shadow = shadow,
                    measurementDimension = watermarkMeasurementDimension
                )
            } catch (err: Exception) {
                MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.watermark_convert_values_error_title)
                    .setMessage(R.string.watermark_convert_values_error_msg)
                    .setPositiveButton(R.string.ok, null)
                    .show()
                return@setOnClickListener
            }

            // A bitmap of the current base image is generated to process it
            val baseImageBitmap = binding.ivBaseImage.drawToBitmap()

            // The watermark is drawn using the library's utility
            WatermarkUtils.drawWatermark(this@WatermarkActivity, baseImageBitmap, textWatermark)

            // The image is assigned to the image view
            binding.ivBaseImage.setImageBitmap(baseImageBitmap)

            // Scrolling up is done to see the result
            binding.svMain.smoothScrollTo(0, 0)

            /*
            android.util.Log.d(
                "WatermarkActivityTest", """
                watermarkText $watermarkText
                watermarkTextSize $watermarkTextSize
                watermarkTextColor $watermarkTextColor
                watermarkTypeface $watermarkTypeface
                watermarkShadow $watermarkShadow
                watermarkShadowRadius $watermarkShadowRadius
                watermarkShadowColor $watermarkShadowColor
                watermarkShadowDx $watermarkShadowDx
                watermarkShadowDy $watermarkShadowDy
                watermarkPosition $watermarkPosition
                watermarkMeasurementDimension $watermarkMeasurementDimension
                watermarkDx $watermarkDx
                watermarkDy $watermarkDy
                watermarkRotation $watermarkRotation
                watermarkOpacity $watermarkOpacity
            """.trimIndent()
            )
            */

        }

    }

    /**
     * Show a dialog to select a color.
     * @param id Identifier to determine what the dialog is displayed for. Use 0 for select the watermark text color or 1
     *        for select the text shadow color.
     * */
    private fun showColorPickerDialog(id: Int) {

        var selectedColor = -1
        var titleResourceId = -1

        if (id == 0) {
            selectedColor = watermarkTextColor.toInt()
            titleResourceId = R.string.watermark_text_color
        }
        if (id == 1) {
            selectedColor = watermarkShadowColor.toInt()
            titleResourceId = R.string.watermark_shadow_color
        }

        ColorPickerDialog.newBuilder().apply {
            setDialogType(TYPE_CUSTOM)
            setDialogTitle(titleResourceId)
            setColor(selectedColor)
            setShowAlphaSlider(true)
            setAllowPresets(false)
            setAllowCustom(true)
            setSelectedButtonText(R.string.select)
            setDialogId(id)
        }.show(this@WatermarkActivity)
    }

    /** ColorPickerDialog on color selected */
    override fun onColorSelected(dialogId: Int, color: Int) {

        if (dialogId == 0) {
            watermarkTextColor = color.toString()
            binding.btnTextColor.setCardBackgroundColor(color)
        } else if (dialogId == 1) {
            watermarkShadowColor = color.toString()
            binding.btnShadowColor.setCardBackgroundColor(color)
        }

    }

    /** ColorPickerDialog on dialog dismissed */
    override fun onDialogDismissed(dialogId: Int) {}

}
