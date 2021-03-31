---
id: view-to-image
title: View To Image
---

#### <a href="../reference/androidutils/com.jeovanimartinez.androidutils.views.viewtoimage/-view-to-image/index.html" target="_blank"><b>[ Reference ]</b></a>

## Description

Utility for convert views to images. Works for any view and view groups, including layouts with all their children views.

![img](../img/view-to-image/view-to-image-img1.png)

---

## Usage

Call the following function of the utility passing the configuration to generate the image of the view.

> #### <a href="../reference/androidutils/com.jeovanimartinez.androidutils.views.viewtoimage/-view-to-image/convert.html" target="_blank"><b>[ Configuration Parameters  ]</b></a>

```kotlin
val bitmap = ViewToImage.convert(
    view = about_contentCardLayout,
    backgroundColor = getColorCompat(R.color.colorBackground),
    backgroundCornerRadius = CornerRadius(10f).asDpToPx(context),
    trimBorders = false,
    padding = Padding(0f, 0f, 18f, 0f).asDpToPx(context),
    margin = Margin(0f),
    viewsToExclude = arrayListOf(
        ExcludeView(about_BottomSeparator, ExcludeMode.CROP_VERTICALLY),
        ExcludeView(about_termsAndPolicy, ExcludeMode.CROP_VERTICALLY),
        ExcludeView(about_openSourceLicenses, ExcludeMode.CROP_VERTICALLY),
        ExcludeView(about_closeBtn, ExcludeMode.CROP_VERTICALLY),
    )
)
```

---

## Considerations

## > Padding and Margin

When converting the view to an image, a margin and padding can be specified, the margin is completely independent (and it will always be transparent), 
and the padding is applied within the specified background, for example:

![img](../img/view-to-image/view-to-image-img2.png)

---

## > Exclude Children Views

If the view to be converted to an image is a view group as a layout, by default the generated image includes all children views, however, it is 
possible to pass a configuration to exclude certain children views in different ways.

> ExcludeMode enum defines the ways in which the child view can be excluded from the image.
> #### <a href="../reference/androidutils/com.jeovanimartinez.androidutils.views.viewtoimage.config/-exclude-mode/index.html" target="_blank"><b>[ Reference ]</b></a>
> ---
> ExcludeView define the configuration to exclude a child view from the image.
> #### <a href="../reference/androidutils/com.jeovanimartinez.androidutils.views.viewtoimage.config/-exclude-view/index.html" target="_blank"><b>[ Reference ]</b></a>

:::note
The children views to exclude must belong directly to the view. For example, if the view is a Linear Layout, which has a Constraint Layout as child view, 
the Constraint Layout can be excluded, but not its children views.
:::

To exemplify the ways in which a child view can be excluded, we will consider the following layout:

![img](../img/view-to-image/view-to-image-img3.png)

### - Hide

> ExcludeMode.HIDE

In this mode, the space occupied by the child view is replaced by the background color.

```kotlin
viewsToExclude = arrayListOf(
    ExcludeView(demoButton, ExcludeMode.HIDE)
)
```

![img](../img/view-to-image/view-to-image-img4.png)

### - Crop Vertically

> ExcludeMode.CROP_VERTICALLY

In this mode, the image of the view is cropped vertically, deleting all the space occupied by the child view.

![img](../img/view-to-image/view-to-image-img5.png)

### - Crop Horizontally

> ExcludeMode.CROP_HORIZONTALLY

In this mode, the image of the view is cropped horizontally, deleting all the space occupied by the child view.

![img](../img/view-to-image/view-to-image-img6.png)

### - Crop All

> ExcludeMode.CROP_ALL

In this mode, the image of the view is cropped vertically and horizontally, deleting all the space occupied by the child view.

![img](../img/view-to-image/view-to-image-img7.png)