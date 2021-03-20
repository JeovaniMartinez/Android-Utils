---
id: view-to-image
title: View To Image
---

#### <a href="../reference/androidutils/com.jeovanimartinez.androidutils.web/-system-web-browser/index.html" target="_blank"><b>[ Reference ]</b></a>

## Description

Utility for convert views to images. Works for any view and view groups, including layouts with all their children views.

![img](../img/pending-image.png)

---

## Usage

Call the following function of the utility 1passing the configuration to generate the image of the view.

> #### <a href="../reference/androidutils/com.jeovanimartinez.androidutils.about/-about-app-config/index.html" target="_blank"><b>[ Configuration Parameters  ]</b></a>

```kotlin
    // CODE
```

---

## Considerations

## > Padding and Margin

When converting the view to an image, a margin and padding can be specified, the margin is completely independent and the padding is applied within 
the specified background, for example:

![img](../img/pending-image.png)

---

## > Exclude Children Views

If the view to be converted to an image is a view group as a layout, by default the generated image includes all children views, however, it is 
possible to pass a configuration to exclude certain children views in different ways.

> ExcludeMode enum defines the ways in which the children views can be excluded from the image.
> #### <a href="../reference/androidutils/com.jeovanimartinez.androidutils.watermark.config/-watermark-position/index.html" target="_blank"><b>[ Reference ]</b></a>
> ---
> ExcludeView define the configuration to exclude a children view from the image.
> #### <a href="../reference/androidutils/com.jeovanimartinez.androidutils.watermark.config/-watermark-position/index.html" target="_blank"><b>[ Reference ]</b></a>

To exemplify the ways in which a children view can be excluded, we will consider the following layout:

![img](../img/pending-image.png)

### - Hide

In this mode, the space occupied by the child view is replaced by the background color.

```kotlin
    // CODE
```

![img](../img/pending-image.png)

### - Crop Vertically

In this mode, the image of the view is cropped vertically, deleting all the space occupied by the children view.

```kotlin
    // CODE
```

![img](../img/pending-image.png)

### - Crop Horizontally

Works the same as Crop Vertically but in horizontal mode.

:::tip
You can define a padding and a padding fill color for the space between the crop.
:::

```kotlin
    // CODE
```

![img](../img/pending-image.png)

### - Crop All

In this mode, the image of the view is cropped vertically and horizontally according to the child view size.

```kotlin
    // CODE
```

![img](../img/pending-image.png)
