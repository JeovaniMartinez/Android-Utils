demoButton.setOnClickListener {

    val context = this@MainActivity
    val backgroundColor = Color.parseColor("#353535")

    val bitmap = ViewToImage.convert(
            view = mainLayout,
            backgroundColor = backgroundColor,
            backgroundCornerRadius = CornerRadius(0f),
            trimBorders = false,
            padding = Padding(10f).asDpToPx(context),
            margin = Margin(0f),
            viewsToExclude = arrayListOf(
                    //ExcludeView(demoButton, ExcludeMode.CROP_VERTICALLY),
            )
    )

    FileUtils.saveBitmapToFile(this@MainActivity, bitmap, "3", getExternalFilesDir(null)!!.absolutePath)

    val bitmap2 = ViewToImage.convert(
            view = mainLayout,
            backgroundColor = backgroundColor,
            backgroundCornerRadius = CornerRadius(0f),
            trimBorders = false,
            padding = Padding(10f).asDpToPx(context),
            margin = Margin(0f),
            viewsToExclude = arrayListOf(
                    ExcludeView(demoButton, ExcludeMode.HIDE),
                    )
    )

    FileUtils.saveBitmapToFile(this@MainActivity, bitmap2, "4", getExternalFilesDir(null)!!.absolutePath)

    val bitmap3 = ViewToImage.convert(
            view = mainLayout,
            backgroundColor = backgroundColor,
            backgroundCornerRadius = CornerRadius(0f),
            trimBorders = false,
            padding = Padding(10f).asDpToPx(context),
            margin = Margin(0f),
            viewsToExclude = arrayListOf(
                    ExcludeView(demoButton, ExcludeMode.CROP_VERTICALLY),
                    )
    )

    FileUtils.saveBitmapToFile(this@MainActivity, bitmap3, "5", getExternalFilesDir(null)!!.absolutePath)

    val bitmap4 = ViewToImage.convert(
            view = mainLayout,
            backgroundColor = backgroundColor,
            backgroundCornerRadius = CornerRadius(0f),
            trimBorders = false,
            padding = Padding(10f).asDpToPx(context),
            margin = Margin(0f),
            viewsToExclude = arrayListOf(
                    ExcludeView(demoButton, ExcludeMode.CROP_HORIZONTALLY),
                    )
    )

    FileUtils.saveBitmapToFile(this@MainActivity, bitmap4, "6", getExternalFilesDir(null)!!.absolutePath)

    val bitmap5 = ViewToImage.convert(
            view = mainLayout,
            backgroundColor = backgroundColor,
            backgroundCornerRadius = CornerRadius(0f),
            trimBorders = false,
            padding = Padding(10f).asDpToPx(context),
            margin = Margin(0f),
            viewsToExclude = arrayListOf(
                    ExcludeView(demoButton, ExcludeMode.CROP_ALL),
                    )
    )

    FileUtils.saveBitmapToFile(this@MainActivity, bitmap5, "7", getExternalFilesDir(null)!!.absolutePath)

}
