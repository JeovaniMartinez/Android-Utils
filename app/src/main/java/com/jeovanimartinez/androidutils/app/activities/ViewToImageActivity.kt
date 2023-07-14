package com.jeovanimartinez.androidutils.app.activities

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import com.jeovanimartinez.androidutils.app.R
import com.jeovanimartinez.androidutils.app.databinding.ActivityViewToImageBinding
import com.jeovanimartinez.androidutils.extensions.activity.configureTaskDescription
import com.jeovanimartinez.androidutils.extensions.context.getColorCompat
import com.jeovanimartinez.androidutils.extensions.dimension.dp2px

/** ViewToImageActivity */
class ViewToImageActivity : AppCompatActivity(), ColorPickerDialogListener {

    private lateinit var binding: ActivityViewToImageBinding

    /*
    * ** IMPORTANT **
    * Some numeric properties are stored as String for easier assignment. During the
    * view to image conversion process, they are converted to the appropriate data type.
    * */

    object ViewToImage {
        var trimBorders = true
        var backgroundColor = Color.TRANSPARENT
        var backgroundCornerAllEqual = true
        var backgroundCornerRadius = "10"
        var paddingTop = "10"
        var paddingRight = "10"
        var paddingBottom = "10"
        var paddingLeft = "10"
        var marginTop = "0"
        var marginRight = "0"
        var marginBottom = "0"
        var marginLeft = "0"
        var excludeViews = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewToImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configureTaskDescription(R.string.app_name, R.mipmap.ic_launcher, getColorCompat(R.color.md_theme_background))

        binding.appBar.btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.appBar.tvTitle.text = getString(R.string.view_to_image_title)

        generalSetup()
        baseLayoutSetup()
        configurationSetup()
    }

    /** General setup */
    private fun generalSetup() {

        // Hide result
        changeResultVisibility(false)

        binding.btnConvert.setOnClickListener { convertViewToImage() }

        binding.btnDeleteResultImage.setOnClickListener { changeResultVisibility(false) }

    }

    /** Base layout setup */
    private fun baseLayoutSetup() {

        binding.btnLayoutBaseButton1.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(R.string.app_name)
                .setMessage(binding.etLayoutBaseInput1.text)
                .setPositiveButton(R.string.ok, null)
                .show()
        }

    }

    /** Configuration setup */
    @SuppressLint("SetTextI18n")
    private fun configurationSetup() {

        binding.swTrimBorders.setOnCheckedChangeListener { _, isChecked ->
            ViewToImage.trimBorders = isChecked
        }

        ViewToImage.backgroundColor = getColorCompat(R.color.view_to_image_default_background_color)

        binding.btnBackgroundColor.setOnClickListener {
            ColorPickerDialog.newBuilder().apply {
                setDialogType(ColorPickerDialog.TYPE_CUSTOM)
                setDialogTitle(R.string.view_to_image_select_background_color)
                setColor(getColorCompat(R.color.view_to_image_default_background_color))
                setShowAlphaSlider(true)
                setAllowPresets(false)
                setAllowCustom(true)
                setSelectedButtonText(R.string.select)
                setDialogId(0)
            }.show(this@ViewToImageActivity)
        }

        binding.swBackgroundCornerAllEqual.setOnCheckedChangeListener { _, isChecked ->
            ViewToImage.backgroundCornerAllEqual = isChecked

            // Set the default value, input type, and show a help message
            val message: Int
            if (isChecked) {
                ViewToImage.backgroundCornerRadius = "10"
                binding.etBackgroundCornerRadius.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                binding.etBackgroundCornerRadius.setText("10")
                message = R.string.view_to_image_background_corner_all_equal_msg
            } else {
                ViewToImage.backgroundCornerRadius = "10,10,10,10,10,10,10,10"
                binding.etBackgroundCornerRadius.inputType = InputType.TYPE_CLASS_TEXT
                binding.etBackgroundCornerRadius.setText("10,10,10,10,10,10,10,10")
                message = R.string.view_to_image_background_corner_not_all_equal_msg
            }
            MaterialAlertDialogBuilder(this)
                .setTitle(R.string.view_to_image_background_corner_radius)
                .setMessage(message)
                .setPositiveButton(R.string.ok, null)
                .show()

        }

        binding.etBackgroundCornerRadius.doAfterTextChanged {
            ViewToImage.backgroundCornerRadius = it.toString()
        }

        binding.etPaddingTop.doAfterTextChanged {
            ViewToImage.paddingTop = it.toString()
        }

        binding.etPaddingRight.doAfterTextChanged {
            ViewToImage.paddingRight = it.toString()
        }

        binding.etPaddingBottom.doAfterTextChanged {
            ViewToImage.paddingBottom = it.toString()
        }

        binding.etPaddingLeft.doAfterTextChanged {
            ViewToImage.paddingLeft = it.toString()
        }

        binding.etMarginTop.doAfterTextChanged {
            ViewToImage.marginTop = it.toString()
        }

        binding.etMarginRight.doAfterTextChanged {
            ViewToImage.marginRight = it.toString()
        }

        binding.etMarginBottom.doAfterTextChanged {
            ViewToImage.marginBottom = it.toString()
        }

        binding.etMarginLeft.doAfterTextChanged {
            ViewToImage.marginLeft = it.toString()
        }

        binding.swExcludeViews.isChecked = ViewToImage.excludeViews
        binding.layoutExcludeViews.visibility = View.GONE

        binding.swExcludeViews.setOnCheckedChangeListener { _, isChecked ->
            ViewToImage.excludeViews = isChecked
            binding.layoutExcludeViews.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

    }

    /** ColorPickerDialog on color selected */
    override fun onColorSelected(dialogId: Int, color: Int) {
        ViewToImage.backgroundColor = color
        binding.btnBackgroundColor.setCardBackgroundColor(color)
    }

    /** ColorPickerDialog on dialog dismissed */
    override fun onDialogDismissed(dialogId: Int) {}

    /**
     * Change the visibility of the views that show the result.
     * @param visible Whether views are shown or hidden.
     * */
    private fun changeResultVisibility(visible: Boolean) {

        val visibility = if (visible) View.VISIBLE else View.GONE
        binding.tvResult.visibility = visibility
        binding.ivResult.visibility = visibility
        binding.layoutResultActions.visibility = visibility

        // Adjust separator margin
        val separatorLayoutParams = binding.separator1.layoutParams as ViewGroup.MarginLayoutParams
        separatorLayoutParams.topMargin = dp2px(if (visible) 0 else 20)
        binding.separator1.layoutParams = separatorLayoutParams
        binding.separator1.requestLayout()

    }

    /** Execute the process to convert the view to an image. */
    private fun convertViewToImage() {

        android.util.Log.d(
            "ViewToImageActivityTest", """
                trimBorders ${ViewToImage.trimBorders}
                backgroundColor ${ViewToImage.backgroundColor}
                backgroundCornerAllEqual ${ViewToImage.backgroundCornerAllEqual}
                backgroundCornerRadius ${ViewToImage.backgroundCornerRadius}
                paddingTop ${ViewToImage.paddingTop}
                paddingRight ${ViewToImage.paddingRight}
                paddingBottom ${ViewToImage.paddingBottom}
                paddingLeft ${ViewToImage.paddingLeft}
                marginTop ${ViewToImage.marginTop}
                marginRight ${ViewToImage.marginRight}
                marginBottom ${ViewToImage.marginBottom}
                marginLeft ${ViewToImage.marginLeft}
                excludeViews ${ViewToImage.excludeViews}
            """.trimIndent()
        )

        // Show the result and to to top
        changeResultVisibility(true)
        Handler(Looper.getMainLooper()).postDelayed({
            binding.svMain.smoothScrollTo(0, 0)
        }, 100)

    }

}
