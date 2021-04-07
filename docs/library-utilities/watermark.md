---
id: watermark
title: Watermark
---

## Description

Set of utilities to draw a text watermarks or a drawable watermarks into images.

<p align="center"><img src={require('@site/docs/img/watermark/watermark-img1-a.jpeg').default} alt="" /></p>

<p align="center"><img src={require('@site/docs/img/watermark/watermark-img1-b.svg').default} alt="" /></p>

<p align="center"><img src={require('@site/docs/img/watermark/watermark-img1-c.jpg').default} alt="" /></p>

#### Code required to process the image as in the example:

```kotlin
// Get the bitmap from image resource
val bitmap = BitmapFactory.decodeResource(
    resources,
    R.drawable.watermark_base1,
    BitmapFactory.Options().apply { inMutable = true; inScaled = false }
)

// Create a shape to draw into image
val shape = GradientDrawable()
    .apply { shape = GradientDrawable.RECTANGLE; setColor(Color.BLACK) }

// Create and draw the watermarks on the image
WatermarkUtils.drawWatermark(
    context,
    bitmap,
    arrayListOf(
        Watermark.Drawable(
            drawable = R.drawable.library_logo,
            position = WatermarkPosition.MIDDLE_CENTER,
            width = 150f,
            height = 150f,
            dx = 0f,
            dy = -20f,
            rotation = 0f,
            opacity = 0.85f,
            measurementDimension = Dimension.PX
        ),
        Watermark.Drawable(
            drawable = shape,
            position = WatermarkPosition.MIDDLE_CENTER,
            width = 807f,
            height = 80f,
            dx = 0f,
            dy = 100f,
            rotation = 0f,
            opacity = 0.6f,
            measurementDimension = Dimension.PX
        ),
        Watermark.Text(
            text = "Android Utils",
            textSize = 40f,
            textColor = Color.WHITE,
            position = WatermarkPosition.MIDDLE_CENTER,
            dx = 0f,
            dy = 108f,
            rotation = 0f,
            opacity = 0.9f,
            typeface = getFontCompat(R.font.oi_regular),
            shadow = WatermarkShadow(2f, 3f, 3f, Color.BLACK),
            measurementDimension = Dimension.PX
        ),
        Watermark.Text(
            text = "Watermark Demo",
            textSize = 20f,
            textColor = Color.WHITE,
            position = WatermarkPosition.TOP_LEFT,
            dx = 10f,
            dy = 10f,
            rotation = 315f,
            opacity = 0.6f,
            typeface = null,
            shadow = WatermarkShadow(2f, 3f, 3f, Color.BLACK),
            measurementDimension = Dimension.PX
        ),
        Watermark.Text(
            text = "Watermark Demo",
            textSize = 20f,
            textColor = Color.WHITE,
            position = WatermarkPosition.TOP_RIGHT,
            dx = -10f,
            dy = 10f,
            rotation = 45f,
            opacity = 0.6f,
            typeface = null,
            shadow = WatermarkShadow(2f, 3f, 3f, Color.BLACK),
            measurementDimension = Dimension.PX
        )
    )
)

// Save image into file using FileUtils
FileUtils.saveBitmapToFile(
    context = context,
    bitmap = bitmap,
    fileName = "watermark-demo",
    format = Bitmap.CompressFormat.JPEG
)
```
 
---

## Basic Concepts

### Watermark Position

To create a watermark it is necessary to define the position that it will have within the image.

> WatermarkPosition enum defines the position where the watermark can be place within the image.
> #### <a href="../reference/androidutils/com.jeovanimartinez.androidutils.watermark.config/-watermark-position/index.html" target="_blank"><b>[ Reference ]</b></a>

#### Fixed Positions

The enum defines 9 fixed positions for the watermark, as shown in the following image:

<p align="center"><img src={require('@site/docs/img/watermark/watermark-img2.png').default} alt="" /></p>

#### Absolute Position

Additionally, the enum has an `ABSOLUTE` position, which allows the watermark to be placed freely within the image. The origin or pivot of the watermark 
is the center of it.

<p align="center"><img src={require('@site/docs/img/watermark/watermark-img3.png').default} alt="" /></p>

---

### Offset

When creating a watermark, it is also necessary to define its offset, both for the x and y axis. This offset can be negative, zero or positive, and 
its interpretation is according to the position of the watermark.

#### Offset For Fixed Positions

The offset in the fixed positions allows to set a margin for the watermark, and its value must be assigned according to the position, for example:

<p align="center"><img src={require('@site/docs/img/watermark/watermark-img4.png').default} alt="" /></p>

#### Offset For Absolute Position

The offset in the absolute position, sets the position of the watermark within the image, for example:

<p align="center"><img src={require('@site/docs/img/watermark/watermark-img5.png').default} alt="" /></p>

---

### Measurement Dimension

When set the properties for the watermark, you can specify the `measurementDimension`, the default is in pixels (PX), but sometimes it can be useful to 
use density-independent pixels (DP).

> Dimension enum defines the allowed dimensions for a watermark.
> #### <a href="../reference/androidutils/com.jeovanimartinez.androidutils.graphics.utils/-dimension/index.html" target="_blank"><b>[ Reference ]</b></a>

For example, in the following image we define a text watermark, in the `BOTTOM_CENTER` position, if we want a margin in y-axis of 50 px, for `Dimension.PX`
we must set dy to -50. But if we use `Dimension.DP`, we must set dy to -25, since the density of the screen is 2.0 and that -25 will be converted to pixels 
at the moment of drawing the watermark.

<p align="center"><img src={require('@site/docs/img/watermark/watermark-img6.png').default} alt="" /></p>

:::note
Measurement dimension applies only to the following watermark properties:
- dx
- dy
- width
- height
- textSize
- shadow
:::

:::caution
`Dimension.SP` is allowed for the `measurementDimension` property, however it is **not recommended** to use it, since if, for example, the device uses a font size larger 
or smaller than the default, this will cause an unwanted change in the dimensions of the watermark. Even for the textSize property of the text watermark, 
it is recommended to use Dimension.DP, since the size will depend on the density, but not on the scale of the text.
:::

---

## Watermark

#### <a href="../reference/androidutils/com.jeovanimartinez.androidutils.watermark/-watermark/index.html" target="_blank"><b>[ Reference ]</b></a>

Before to draw the watermark on the image, is needed create an object with the watermark configuration. The library defines two types of watermark, text 
and drawable (image, shape, etc.).

### Drawable

> This type of watermark allows to draw any drawable on the image.
> #### <a href="../reference/androidutils/com.jeovanimartinez.androidutils.watermark/-watermark/-drawable/index.html" target="_blank"><b>[ Reference ]</b></a>

#### Usage

```kotlin
val drawableWatermark = Watermark.Drawable(
    drawable = R.drawable.library_logo,
    position = WatermarkPosition.BOTTOM_LEFT,
    width = 80f,
    height = 80f,
    dx = 10f,
    dy = -5f,
    rotation = 0f,
    opacity = 0.8f,
    measurementDimension = Dimension.PX
)
```

---

### Text

> This type of watermark allows to draw any text on the image.
> #### <a href="../reference/androidutils/com.jeovanimartinez.androidutils.watermark/-watermark/-text/index.html" target="_blank"><b>[ Reference ]</b></a>

#### Usage

```kotlin
val textWatermark = Watermark.Text(
    text = "Sample Watermark By Android Utils",
    textSize = 30f,
    textColor = Color.WHITE,
    position = WatermarkPosition.TOP_RIGHT,
    dx = -10f,
    dy = 10f,
    rotation = 0f,
    opacity = 0.65f,
    typeface = getFontCompat(R.font.fugaz_one_regular),
    shadow = WatermarkShadow(2f, 10f, 20f, Color.parseColor("#1976D2")),
    measurementDimension = Dimension.PX
)
```

---

## Watermark Utils

#### <a href="../reference/androidutils/com.jeovanimartinez.androidutils.watermark/-watermark-utils/index.html" target="_blank"><b>[ Reference ]</b></a>

After defining the watermarks, this utility takes care of processing and drawing them on the images.

### Draw Watermark

Allows to draw watermarks on a bitmap image. The utility has functions to draw one or a list of watermarks.

#### Usage

```kotlin {1,5}
// To draw a watermark
WatermarkUtils.drawWatermark(context, bitmap, drawableWatermark)
WatermarkUtils.drawWatermark(context, bitmap, textWatermark)

// To draw a list of watermarks
WatermarkUtils.drawWatermark(
    context,
    bitmap,
    arrayListOf(
        drawableWatermark,
        drawableWatermark.copy(dx = 95f, opacity = 0.5f),
        textWatermark
    )
)
```

:::tip
You can use the copy function to copy a watermark object for changing only some of its properties, but keeping the rest unchanged.
```kotlin
WatermarkUtils.drawWatermark(
    context, bitmap,
    drawableWatermark.copy(dx = 95f, opacity = 0.5f)
)
```
:::

#### Result

<p align="center"><img src={require('@site/docs/img/watermark/watermark-img7.jpeg').default} alt="" /></p>
