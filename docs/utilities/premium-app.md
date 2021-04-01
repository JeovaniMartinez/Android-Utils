---
id: premium-app
title: Premium App
---

## Description

Set of utilities to simplify the verification and purchase process of the premium version of the app.

It provides an abstraction layer to interact with the [Google Play Billing Library,](https://developer.android.com/google/play/billing) simplifying 
the process and allowing a quick and easy integration 
into any app.

---

### Use cases

On many occasions, when we develop an app we integrate ads or premium functions. A common practice is to create a free app on Google Ply, which 
implements the ads and has certain restrictions, and another paid app without ads and with Premium features. This usually has some drawbacks, and the 
best practice is to create only a free app, which shows ads and restricts certain functions, and then, through a purchase, remove these ads and 
restrictions, which allows us to have only one app that is easier to manage and update. That is where this library utility comes into play, allowing 
such an implementation to be carried out easily and with few code lines.

---

## Getting Ready

Before we start, we need to prepare a few things to be able to use this library utility.

:::tip
Before starting, Is recommend you take a look at the [official documentation of Google Play's billing system,](https://developer.android.com/google/play/billing) 
so that you can understand how it works and know the terminology.
:::

### 1. License Testing

In order to test the implementation in your app, you must have at least one license for testing, 
[here](https://support.google.com/googleplay/android-developer/answer/6062777) you will find information and the steps to create them.

### 2. Setup The Project

2.1.- Add the Google Play Billing Library implementation in Gradle file at app level.
```gradle {3}
dependencies {
    ...
    implementation 'com.android.billingclient:billing-ktx:3.0.3'
}
```

2.2.- In the `AndroidManifest`, add billing permission
```xml
 <uses-permission android:name="com.android.vending.BILLING" />
```

### 3. Publish The App

Once you have the project configured, you must generate an apk or bundle of it and publish it on Google Play in any track, including the internal test track.
_This is necessary to be able to create the products integrated in the Google Play Console, since when uploading the app with the indicated configuring, 
the billing functions are enabled._

### 4. Create In-app Product

Now it is necessary to create one or more products in the Google Play console that grant premium access to the app, this can be done by following 
[these steps.](https://support.google.com/googleplay/android-developer/answer/1153481?hl#zippy=%2Ccreate-a-single-managed-product)

#### Considerations

- The id of the created product we will call `sku`, since this is how it handles it the Google Play billing library.
- The product must have the `active` status to be able to handle it in the app.

---

## Components

The utility is made up of the following components:

### Premium.State
> Enum defining the possible states when checking if a user has premium privileges in the app.
> <a href="../reference/androidutils/com.jeovanimartinez.androidutils.billing.premium/-premium/-state/index.html" target="_blank"><b>[ Reference ]</b></a>

### Premium.Listener
> Listener for events relating to app premium billing.
> <a href="../reference/androidutils/com.jeovanimartinez.androidutils.billing.premium/-premium/-listener/index.html" target="_blank"><b>[ Reference ]</b></a>

### Premium.Controller
> Controller for the purchase and verification process of the premium version of the app.
> <a href="../reference/androidutils/com.jeovanimartinez.androidutils.billing.premium/-premium/-controller/index.html" target="_blank"><b>[ Reference ]</b></a>

### Premium.getCurrentState()
> Function that returns the current premium state. It value must be used in all conditions within the app where it is necessary to verify the premium state.
> <a href="../reference/androidutils/com.jeovanimartinez.androidutils.billing.premium/-premium/get-current-state.html" target="_blank"><b>[ Reference ]</b></a>


---


## Implementation

Since we have all the above steps covered, we can now implement the utility as shown below.

### General

General implementation.

1.- In the `onCreate()` of the singleton or main activity, the utility should be initialized.

> #### <a href="../reference/androidutils/com.jeovanimartinez.androidutils.billing.premium/-premium/-controller/init.html" target="_blank"><b>[ Configuration Parameters ]</b></a>

```kotlin
Premium.Controller.init(context, listOf("premium_upgrade"))
```

2.- Later we must set the listener in the place where we want to receive the events related to the components of the utility.
```kotlin
    // CODE...
```

---

### Verification

We must verify whether the user has premium privileges at least on the following occasions.

1.- On launch main activity: we must do a quick check of the current status, to know if the user is premium or not, and show the appropriate
content, for example:

```kotlin
if (Premium.getCurrentState() == Premium.State.PREMIUM) {
    showAds()
} else {
    hideAds()
}
```

2.- The previous verification, aims to show the appropriate content at the launch the app, now we must do a direct verification with the billing client, 
to update the premium status, this can be done as follows:

```kotlin
Premium.Controller.checkPremium(context)
```

The verification is executed asynchronously, and the result is reported by the listener.

```kotlin
 // CODE...
```

---

### Product Details

This process is optional, and it will help us to show details of the product that provides the premium benefits (name, description, price in the 
user's currency, etc.).

The request is performed as follows:

```kotlin
 // CODE...
```

And the result is reported by the listener.

```kotlin
 // CODE...
```

---

### Start Purchase

The purchase of the product is performed as follows.

:::note
Before starting the purchase, check the current premium status, and start the purchase only if the user is not premium. For example:

```kotlin
 // CODE...
```
:::

To start the purchase:

```kotlin
 // CODE...
```

In case of an error, it is reported by the listener.

```kotlin
 // CODE...
```

If the purchase flow was shown successfully, the result (the purchase was completed, its canceled, its pending, etc.) is reported through the listener.

```kotlin
 // CODE...
```

:::note
If the purchase was made through a deferred payment, the listener function is invoked at the end of the flow, with the result `State.PENDING_TRANSACTION`, 
and it is invoked again when the transaction is completed, with the new result `State.NOT_PREMIUM` or `State.PREMIUM`.
:::


