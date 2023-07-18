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
import com.jeovanimartinez.androidutils.graphics.utils.Dimension
import com.jeovanimartinez.androidutils.graphics.utils.Margin
import com.jeovanimartinez.androidutils.graphics.utils.Padding
import com.jeovanimartinez.androidutils.views.viewtoimage.ViewToImage
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

    private var trimBorders = true
    private var interpretValuesAs = Dimension.PX
    private var backgroundColor = Color.TRANSPARENT
    private var backgroundCornerAllEqual = true
    private var backgroundCornerRadius = "10"
    private var paddingTop = "50"
    private var paddingRight = "50"
    private var paddingBottom = "50"
    private var paddingLeft = "50"
    private var marginTop = "0"
    private var marginRight = "0"
    private var marginBottom = "0"
    private var marginLeft = "0"
    private var excludeViews = false
    private var excludeViewTextExcludeMode: ExcludeMode? = null // null to no exclude, or use value from ExcludeMode enum to exclude
    private var excludeViewTextIncludeMargin = false
    private var excludeViewImageExcludeMode: ExcludeMode? = null
    private var excludeViewImageIncludeMargin = false
    private var excludeViewInputExcludeMode: ExcludeMode? = null
    private var excludeViewInputIncludeMargin = false
    private var excludeViewButtonExcludeMode: ExcludeMode? = null
    private var excludeViewButtonIncludeMargin = false


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
            trimBorders = isChecked
        }

        val interpretValuesAsMenu = binding.menuInterpretValuesAs.editText as MaterialAutoCompleteTextView
        val interpretValuesAsAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, resources.getStringArray(R.array.view_to_image_dimensions_array))
        interpretValuesAsMenu.setAdapter(interpretValuesAsAdapter)
        interpretValuesAsMenu.setText(interpretValuesAsAdapter.getItem(0).toString(), false)
        interpretValuesAsMenu.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            interpretValuesAs = Dimension.values()[position]
        }

        backgroundColor = getColorCompat(R.color.view_to_image_default_background_color)

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
            backgroundCornerAllEqual = isChecked

            // Set the default value, input type, and show a help message
            val message: Int
            if (isChecked) {
                backgroundCornerRadius = "10"
                binding.etBackgroundCornerRadius.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                binding.etBackgroundCornerRadius.setText("10")
                message = R.string.view_to_image_background_corner_all_equal_msg
            } else {
                backgroundCornerRadius = "10,10,10,10,10,10,10,10"
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
            backgroundCornerRadius = it.toString()
        }

        binding.etPaddingTop.doAfterTextChanged {
            paddingTop = it.toString()
        }

        binding.etPaddingRight.doAfterTextChanged {
            paddingRight = it.toString()
        }

        binding.etPaddingBottom.doAfterTextChanged {
            paddingBottom = it.toString()
        }

        binding.etPaddingLeft.doAfterTextChanged {
            paddingLeft = it.toString()
        }

        binding.etMarginTop.doAfterTextChanged {
            marginTop = it.toString()
        }

        binding.etMarginRight.doAfterTextChanged {
            marginRight = it.toString()
        }

        binding.etMarginBottom.doAfterTextChanged {
            marginBottom = it.toString()
        }

        binding.etMarginLeft.doAfterTextChanged {
            marginLeft = it.toString()
        }

        binding.swExcludeViews.isChecked = excludeViews
        binding.layoutExcludeViews.visibility = View.GONE

        binding.swExcludeViews.setOnCheckedChangeListener { _, isChecked ->
            excludeViews = isChecked
            binding.layoutExcludeViews.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        val excludeModeAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, resources.getStringArray(R.array.view_to_image_exclude_mode_array))

        val textExcludeModeMenu = binding.menuTextExcludeMode.editText as MaterialAutoCompleteTextView
        textExcludeModeMenu.setAdapter(excludeModeAdapter)
        textExcludeModeMenu.setText(excludeModeAdapter.getItem(0).toString(), false)
        textExcludeModeMenu.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            excludeViewTextExcludeMode = if (position == 0) null else ExcludeMode.values()[position - 1]
        }

        binding.cbTextIncludeMargin.setOnCheckedChangeListener { _, isChecked ->
            excludeViewTextIncludeMargin = isChecked
        }

        val imageExcludeModeMenu = binding.menuImageExcludeMode.editText as MaterialAutoCompleteTextView
        imageExcludeModeMenu.setAdapter(excludeModeAdapter)
        imageExcludeModeMenu.setText(excludeModeAdapter.getItem(0).toString(), false)
        imageExcludeModeMenu.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            excludeViewImageExcludeMode = if (position == 0) null else ExcludeMode.values()[position - 1]
        }

        binding.cbImageIncludeMargin.setOnCheckedChangeListener { _, isChecked ->
            excludeViewImageIncludeMargin = isChecked
        }

        val inputExcludeModeMenu = binding.menuInputExcludeMode.editText as MaterialAutoCompleteTextView
        inputExcludeModeMenu.setAdapter(excludeModeAdapter)
        inputExcludeModeMenu.setText(excludeModeAdapter.getItem(0).toString(), false)
        inputExcludeModeMenu.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            excludeViewInputExcludeMode = if (position == 0) null else ExcludeMode.values()[position - 1]
        }

        binding.cbInputIncludeMargin.setOnCheckedChangeListener { _, isChecked ->
            excludeViewInputIncludeMargin = isChecked
        }

        val buttonExcludeModeMenu = binding.menuButtonExcludeMode.editText as MaterialAutoCompleteTextView
        buttonExcludeModeMenu.setAdapter(excludeModeAdapter)
        buttonExcludeModeMenu.setText(excludeModeAdapter.getItem(0).toString(), false)
        buttonExcludeModeMenu.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            excludeViewButtonExcludeMode = if (position == 0) null else ExcludeMode.values()[position - 1]
        }

        binding.cbButtonIncludeMargin.setOnCheckedChangeListener { _, isChecked ->
            excludeViewButtonIncludeMargin = isChecked
        }

    }

    /** ColorPickerDialog on color selected */
    override fun onColorSelected(dialogId: Int, color: Int) {
        backgroundColor = color
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
        binding.separatorResult.visibility = visibility
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
            cornerRadius = if (backgroundCornerAllEqual) {
                CornerRadius(backgroundCornerRadius.toFloat())
            } else {
                val v = backgroundCornerRadius.split(",")
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
            padding = Padding(paddingTop.toFloat(), paddingRight.toFloat(), paddingBottom.toFloat(), paddingLeft.toFloat())
            margin = Margin(marginTop.toFloat(), marginRight.toFloat(), marginBottom.toFloat(), marginLeft.toFloat())
        } catch (err: Exception) {
            MaterialAlertDialogBuilder(this)
                .setTitle(R.string.view_to_image_incorrect_values_title)
                .setMessage(R.string.view_to_image_incorrect_values_msg)
                .setPositiveButton(R.string.ok, null)
                .show()
            return
        }

        val viewsToExclude = arrayListOf<ExcludeView>()

        if (excludeViews) {
            excludeViewTextExcludeMode.whenNotNull { viewsToExclude.add(ExcludeView(binding.tvLayoutBaseText1, it, excludeViewTextIncludeMargin)) }
            excludeViewImageExcludeMode.whenNotNull { viewsToExclude.add(ExcludeView(binding.ivLayoutBaseImage1, it, excludeViewImageIncludeMargin)) }
            excludeViewInputExcludeMode.whenNotNull { viewsToExclude.add(ExcludeView(binding.layoutBaseInput1, it, excludeViewInputIncludeMargin)) }
            excludeViewButtonExcludeMode.whenNotNull { viewsToExclude.add(ExcludeView(binding.btnLayoutBaseButton1, it, excludeViewButtonIncludeMargin)) }
        }

        android.util.Log.d(
            "ViewToImageActivityTest", """
                trimBorders $trimBorders
                interpretValuesAs $interpretValuesAs
                backgroundColor $backgroundColor
                backgroundCornerAllEqual $backgroundCornerAllEqual
                backgroundCornerRadius $backgroundCornerRadius
                cornerRadius $cornerRadius
                paddingTop $paddingTop
                paddingRight $paddingRight
                paddingBottom $paddingBottom
                paddingLeft $paddingLeft
                padding $padding
                marginTop $marginTop
                marginRight $marginRight
                marginBottom $marginBottom
                marginLeft $marginLeft
                margin $margin
                excludeViews $excludeViews
                excludeViewText $excludeViewTextExcludeMode $excludeViewTextIncludeMargin
                excludeViewImage $excludeViewImageExcludeMode $excludeViewImageIncludeMargin
                excludeViewInput $excludeViewInputExcludeMode $excludeViewInputIncludeMargin
                excludeViewButton $excludeViewButtonExcludeMode $excludeViewButtonIncludeMargin
            """.trimIndent()
        )

        // The view is converted to an image
        val convertViewToImageResult = ViewToImage.convert(
            view = binding.layoutBase,
            backgroundColor = backgroundColor,
            backgroundCornerRadius = if (interpretValuesAs == Dimension.PX) cornerRadius else cornerRadius.asDpToPx(this@ViewToImageActivity),
            trimBorders = trimBorders,
            padding = if (interpretValuesAs == Dimension.PX) padding else padding.asDpToPx(this@ViewToImageActivity),
            margin = if (interpretValuesAs == Dimension.PX) margin else margin.asDpToPx(this@ViewToImageActivity),
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
