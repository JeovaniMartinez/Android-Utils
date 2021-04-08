binding.demoButton!!.setOnClickListener {

    val context = this@AboutActivity
    val backgroundColor = Color.parseColor("#353535")

    val bitmap = ViewToImage.convert(
            view = binding.mainLayout as View,
            backgroundColor = backgroundColor,
            backgroundCornerRadius = CornerRadius(0f),
            trimBorders = false,
            padding = Padding(10f).asDpToPx(context),
            margin = Margin(0f),
            viewsToExclude = arrayListOf(
                    //ExcludeView(demoButton, ExcludeMode.CROP_VERTICALLY),
            )
    )

    FileUtils.saveBitmapToFile(context, bitmap, "3", getExternalFilesDir(null)!!.absolutePath)

    val bitmap2 = ViewToImage.convert(
            view = binding.mainLayout as View,
            backgroundColor = backgroundColor,
            backgroundCornerRadius = CornerRadius(0f),
            trimBorders = false,
            padding = Padding(10f).asDpToPx(context),
            margin = Margin(0f),
            viewsToExclude = arrayListOf(
                    ExcludeView(binding.demoButton as View, ExcludeMode.HIDE),
                    )
    )

    FileUtils.saveBitmapToFile(context, bitmap2, "4", getExternalFilesDir(null)!!.absolutePath)

    val bitmap3 = ViewToImage.convert(
            view = binding.mainLayout as View,
            backgroundColor = backgroundColor,
            backgroundCornerRadius = CornerRadius(0f),
            trimBorders = false,
            padding = Padding(10f).asDpToPx(context),
            margin = Margin(0f),
            viewsToExclude = arrayListOf(
                    ExcludeView(binding.demoButton as View, ExcludeMode.CROP_VERTICALLY),
                    )
    )

    FileUtils.saveBitmapToFile(context, bitmap3, "5", getExternalFilesDir(null)!!.absolutePath)

    val bitmap4 = ViewToImage.convert(
            view = binding.mainLayout as View,
            backgroundColor = backgroundColor,
            backgroundCornerRadius = CornerRadius(0f),
            trimBorders = false,
            padding = Padding(10f).asDpToPx(context),
            margin = Margin(0f),
            viewsToExclude = arrayListOf(
                    ExcludeView(binding.demoButton as View, ExcludeMode.CROP_HORIZONTALLY),
                    )
    )

    FileUtils.saveBitmapToFile(context, bitmap4, "6", getExternalFilesDir(null)!!.absolutePath)

    val bitmap5 = ViewToImage.convert(
            view = binding.mainLayout as View,
            backgroundColor = backgroundColor,
            backgroundCornerRadius = CornerRadius(0f),
            trimBorders = false,
            padding = Padding(10f).asDpToPx(context),
            margin = Margin(0f),
            viewsToExclude = arrayListOf(
                    ExcludeView(binding.demoButton as View, ExcludeMode.CROP_ALL),
                    )
    )

    FileUtils.saveBitmapToFile(context, bitmap5, "7", getExternalFilesDir(null)!!.absolutePath)

}
