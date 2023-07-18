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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.jaredrummler.android.colorpicker.ColorPickerDialog
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener
import com.jeovanimartinez.androidutils.app.R
import com.jeovanimartinez.androidutils.app.databinding.ActivityViewToImageBinding
import com.jeovanimartinez.androidutils.extensions.activity.configureTaskDescription
import com.jeovanimartinez.androidutils.extensions.context.getColorCompat
import com.jeovanimartinez.androidutils.extensions.dimension.dp2px
import com.jeovanimartinez.androidutils.extensions.nullability.whenNotNull
import com.jeovanimartinez.androidutils.graphics.utils.CornerRadius
import com.jeovanimartinez.androidutils.graphics.utils.Margin
import com.jeovanimartinez.androidutils.graphics.utils.Padding
import com.jeovanimartinez.androidutils.views.viewtoimage.config.ExcludeMode
import com.jeovanimartinez.androidutils.views.viewtoimage.config.ExcludeView

/** ViewToImageActivity */
class ViewToImageActivity : AppCompatActivity(), ColorPickerDialogListener {

    private lateinit var binding: ActivityViewToImageBinding

    /*
    * ** IMPORTANT **
    * Some numeric properties are stored as String for easier assignment. During the
    * view to image conversion process, they are converted to the appropriate data type.
    * */

    // Auxiliary object to store the properties that will be processed later
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
        var excludeViewTextExcludeMode: ExcludeMode? = null // null to no exclude, or use value from ExcludeMode enum to exclude
        var excludeViewTextIncludeMargin = false
        var excludeViewImageExcludeMode: ExcludeMode? = null
        var excludeViewImageIncludeMargin = false
        var excludeViewInputExcludeMode: ExcludeMode? = null
        var excludeViewInputIncludeMargin = false
        var excludeViewButtonExcludeMode: ExcludeMode? = null
        var excludeViewButtonIncludeMargin = false
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

        val excludeModeAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, resources.getStringArray(R.array.view_to_image_exclude_mode_array))

        val textExcludeModeMenu = binding.menuTextExcludeMode.editText as MaterialAutoCompleteTextView
        textExcludeModeMenu.setAdapter(excludeModeAdapter)
        textExcludeModeMenu.setText(excludeModeAdapter.getItem(0).toString(), false)
        textExcludeModeMenu.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            if (position == 0) ViewToImage.excludeViewTextExcludeMode = null
            else ViewToImage.excludeViewTextExcludeMode = ExcludeMode.values()[position - 1]
        }

        binding.cbTextIncludeMargin.setOnCheckedChangeListener { _, isChecked ->
            ViewToImage.excludeViewTextIncludeMargin = isChecked
        }

        val imageExcludeModeMenu = binding.menuImageExcludeMode.editText as MaterialAutoCompleteTextView
        imageExcludeModeMenu.setAdapter(excludeModeAdapter)
        imageExcludeModeMenu.setText(excludeModeAdapter.getItem(0).toString(), false)
        imageExcludeModeMenu.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            if (position == 0) ViewToImage.excludeViewImageExcludeMode = null
            else ViewToImage.excludeViewImageExcludeMode = ExcludeMode.values()[position - 1]
        }

        binding.cbImageIncludeMargin.setOnCheckedChangeListener { _, isChecked ->
            ViewToImage.excludeViewImageIncludeMargin = isChecked
        }

        val inputExcludeModeMenu = binding.menuInputExcludeMode.editText as MaterialAutoCompleteTextView
        inputExcludeModeMenu.setAdapter(excludeModeAdapter)
        inputExcludeModeMenu.setText(excludeModeAdapter.getItem(0).toString(), false)
        inputExcludeModeMenu.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            if (position == 0) ViewToImage.excludeViewInputExcludeMode = null
            else ViewToImage.excludeViewInputExcludeMode = ExcludeMode.values()[position - 1]
        }

        binding.cbInputIncludeMargin.setOnCheckedChangeListener { _, isChecked ->
            ViewToImage.excludeViewInputIncludeMargin = isChecked
        }

        val buttonExcludeModeMenu = binding.menuButtonExcludeMode.editText as MaterialAutoCompleteTextView
        buttonExcludeModeMenu.setAdapter(excludeModeAdapter)
        buttonExcludeModeMenu.setText(excludeModeAdapter.getItem(0).toString(), false)
        buttonExcludeModeMenu.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            if (position == 0) ViewToImage.excludeViewButtonExcludeMode = null
            else ViewToImage.excludeViewButtonExcludeMode = ExcludeMode.values()[position - 1]
        }

        binding.cbButtonIncludeMargin.setOnCheckedChangeListener { _, isChecked ->
            ViewToImage.excludeViewButtonIncludeMargin = isChecked
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

        // Validations are performed and the appropriate objects are generated to convert the view into an image

        val cornerRadius: CornerRadius

        try {
            cornerRadius = if (ViewToImage.backgroundCornerAllEqual) {
                CornerRadius(ViewToImage.backgroundCornerRadius.toFloat())
            } else {
                val v = ViewToImage.backgroundCornerRadius.split(",")
                if (v.size != 8) throw Exception("8 values were expected")
                CornerRadius(v[0].toFloat(), v[1].toFloat(), v[2].toFloat(), v[3].toFloat(), v[4].toFloat(), v[5].toFloat(), v[6].toFloat(), v[7].toFloat())
            }
        } catch (err: Exception) {
            MaterialAlertDialogBuilder(this)
                .setTitle(R.string.view_to_image_incorrect_values_title)
                .setMessage(R.string.view_to_image_background_corner_not_all_equal_err_msg)
                .setPositiveButton(R.string.ok, null)
                .show()
            return
        }

        val padding: Padding
        val margin: Margin

        try {
            padding = Padding(ViewToImage.paddingTop.toFloat(), ViewToImage.paddingRight.toFloat(), ViewToImage.paddingBottom.toFloat(), ViewToImage.paddingLeft.toFloat())
            margin = Margin(ViewToImage.marginTop.toFloat(), ViewToImage.marginRight.toFloat(), ViewToImage.marginBottom.toFloat(), ViewToImage.marginLeft.toFloat())
        } catch (err: Exception) {
            MaterialAlertDialogBuilder(this)
                .setTitle(R.string.view_to_image_incorrect_values_title)
                .setMessage(R.string.view_to_image_incorrect_values_msg)
                .setPositiveButton(R.string.ok, null)
                .show()
            return
        }

        val viewsToExclude = arrayListOf<ExcludeView>()

        if (ViewToImage.excludeViews) {
            ViewToImage.excludeViewTextExcludeMode.whenNotNull { viewsToExclude.add(ExcludeView(binding.tvLayoutBaseText1, it, ViewToImage.excludeViewTextIncludeMargin)) }
            ViewToImage.excludeViewImageExcludeMode.whenNotNull { viewsToExclude.add(ExcludeView(binding.ivLayoutBaseImage1, it, ViewToImage.excludeViewImageIncludeMargin)) }
            ViewToImage.excludeViewInputExcludeMode.whenNotNull { viewsToExclude.add(ExcludeView(binding.layoutBaseInput1, it, ViewToImage.excludeViewInputIncludeMargin)) }
            ViewToImage.excludeViewButtonExcludeMode.whenNotNull { viewsToExclude.add(ExcludeView(binding.btnLayoutBaseButton1, it, ViewToImage.excludeViewButtonIncludeMargin)) }
        }

        /*
        android.util.Log.d(
            "ViewToImageActivityTest", """
                trimBorders ${ViewToImage.trimBorders}
                backgroundColor ${ViewToImage.backgroundColor}
                backgroundCornerAllEqual ${ViewToImage.backgroundCornerAllEqual}
                backgroundCornerRadius ${ViewToImage.backgroundCornerRadius}
                cornerRadius $cornerRadius
                paddingTop ${ViewToImage.paddingTop}
                paddingRight ${ViewToImage.paddingRight}
                paddingBottom ${ViewToImage.paddingBottom}
                paddingLeft ${ViewToImage.paddingLeft}
                padding $padding
                marginTop ${ViewToImage.marginTop}
                marginRight ${ViewToImage.marginRight}
                marginBottom ${ViewToImage.marginBottom}
                marginLeft ${ViewToImage.marginLeft}
                margin $margin
                excludeViews ${ViewToImage.excludeViews}
                excludeViewText ${ViewToImage.excludeViewTextExcludeMode} ${ViewToImage.excludeViewTextIncludeMargin}
                excludeViewImage ${ViewToImage.excludeViewImageExcludeMode} ${ViewToImage.excludeViewImageIncludeMargin}
                excludeViewInput ${ViewToImage.excludeViewInputExcludeMode} ${ViewToImage.excludeViewInputIncludeMargin}
                excludeViewButton ${ViewToImage.excludeViewButtonExcludeMode} ${ViewToImage.excludeViewButtonIncludeMargin}
            """.trimIndent()
        )
        */

        // The view is converted to an image
        val convertViewToImageResult = com.jeovanimartinez.androidutils.views.viewtoimage.ViewToImage.convert(
            view = binding.layoutBase,
            backgroundColor = ViewToImage.backgroundColor,
            backgroundCornerRadius = cornerRadius,
            trimBorders = ViewToImage.trimBorders,
            padding = padding,
            margin = margin,
            viewsToExclude = viewsToExclude
        )

        binding.ivResult.setImageBitmap(convertViewToImageResult)

        // Show the result and go to top
        changeResultVisibility(true)
        Handler(Looper.getMainLooper()).postDelayed({
            binding.svMain.smoothScrollTo(0, 0)
        }, 100)

    }

}
