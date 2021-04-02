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

### Security

:::warning
Before implementing this utility in your app, please read the entire documentation to determine if it is the right option for your needs.
:::

As your app grows in popularity, it can also attract the unwanted attention of malicious users that might want to abuse your app. In this case, we are
going to focus on analyzing the security of the implementation of this library utility.

If you publish a free app with ads and limited features and, and a paid app with no ads and features unlocked, a malicious user can easily generate 
an apk of your premium app and distribute it. But if you only have one app, and the premium features are limited by an in-app purchase (how this utility
does it), a malicious user must use reverse engineer in your app in order to remove the restrictions, although this may be easy for hackers, it provides 
a little more complexity than the previous example.

Based on the above, if you only need to remove the ads from your app or unlock some features through in-app purchase, this utility may be the right 
one for you. If, on the other hand, you need a more rigorous verification of the purchases in your app, you must carry out your own implementation by 
performing the validations in your back end. You can read more information about security [here.](https://developer.android.com/google/play/billing/security)

---

## Getting Ready

Before we start, we need to prepare a few things to be able to use this library utility.

:::tip
Before starting, is recommend you take a look at the [official documentation of Google Play's billing system,](https://developer.android.com/google/play/billing) 
so that you can understand how it works and know the terminology.
:::

### 1. License Testing

In order to test the implementation in your app, you must have at least one license for testing, 
[here](https://support.google.com/googleplay/android-developer/answer/6062777) you will find information and the steps to create them.

### 2. Setup The Project

2.1.- Add the Google Play Billing Library implementation in Gradle file at app level.

:::caution
It is strongly recommended to add the indicated version `3.0.3`, to avoid conflicts with dependencies.
:::

```gradle {3}
dependencies {
    ...
    implementation 'com.android.billingclient:billing-ktx:3.0.3'
}
```

2.2.- In the `AndroidManifest`, add billing permission.
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

2.- Later we must set the listener in the place where we want to receive the events related to the app premium billing.
```kotlin {8}
val listener = object : Premium.Listener {
    override fun onCheckPremium(state: Premium.State) {}
    override fun onPurchaseResult(result: Premium.State) {}
    override fun onSkuDetails(skuDetails: List<SkuDetails>?) {}
    override fun onStartPurchaseError(code: Int) {}
}

Premium.Controller.setListener(listener)
```

:::tip
You can have several listeners in different places, just remember to set and remove them where required.
```kotlin
Premium.Controller.setListener(listener) // To set the listener

Premium.Controller.removeListener() // To remove the listener
```
:::

---

### Verification

We must verify whether the user has premium privileges at least on the following occasions.

1.- On launch main activity: we must do a quick check of the current status, to know if the user is premium or not, and show the appropriate
content, for example:

```kotlin
if (Premium.getCurrentState() == Premium.State.PREMIUM) {
    // Code to execute if the user is premium, for example, hide ads
} else {
    // Code to execute if the user is not premium, for example, show ads
}
```

2.- The previous verification, aims to show the appropriate content at the launch the app, now we must do a direct verification with the billing client, 
to update the premium status, this can be done as follows:

```kotlin
Premium.Controller.checkPremium(context)
```

The verification is executed asynchronously, and the result is reported by the listener.

```kotlin
override fun onCheckPremium(state: Premium.State) {
    if (state == Premium.State.PREMIUM) {
        // Code to execute if the user is premium, for example, hide ads
    } else {
        // Code to execute if the user is not premium, for example, show ads
    }
}
```

---

### Product Details

This process is optional, and it will help us to show details of the product that provides the premium benefits (name, description, price in the 
user's currency, etc.).

The request is performed as follows:

```kotlin
Premium.Controller.getSkuDetails(context, listOf("premium_upgrade"))
```

And the result is reported by the listener.

```kotlin
override fun onSkuDetails(skuDetails: List<SkuDetails>?) {
    if (skuDetails != null) {
        // Show product details
    } else {
        // Show error message
    }
}
```

---

### Start Purchase

The purchase of the product is performed as follows.

:::note
Before starting the purchase, check the current premium status, and start the purchase only if the user is not premium. For example:

```kotlin
when (Premium.getCurrentState()) {
    Premium.State.NOT_PREMIUM -> Premium.Controller.startPurchase(activity, "premium_upgrade")
    Premium.State.PENDING_TRANSACTION -> longToast("The purchase is pending...")
    Premium.State.PREMIUM -> longToast("You are already a premium user...")
}
```
:::

To start the purchase:

```kotlin
Premium.Controller.startPurchase(activity, "premium_upgrade")
```

In case of an error, it is reported by the listener.

```kotlin
override fun onStartPurchaseError(code: Int) {
    longToast("Service unavailable, please try again later")
}
```

If the purchase flow was shown successfully, the result (the purchase was completed, its canceled, its pending, etc.) is reported through the listener.

```kotlin
override fun onPurchaseResult(result: Premium.State) {
    if (result == Premium.State.PREMIUM) {
        // Code to execute if the user is premium, for example, hide ads
    } else {
        // Code to execute if the user is not premium, for example, show ads
    }
}
```

:::note
If the purchase was made through a deferred payment, the listener function is invoked at the end of the flow, with the result `State.PENDING_TRANSACTION`, 
and it is invoked again when the transaction is completed, with the new result `State.NOT_PREMIUM` or `State.PREMIUM`.
:::

---

### Considerations

It is recommended to check the premium state in the `onResume()` of the main activity, and if the status is `State.PENDING_TRANSACTION`, check 
the state again with the billing client, this in case the listener did not inform when the state of the pending transaction was updated.

```kotlin
if (Premium.getCurrentState() == Premium.State.PENDING_TRANSACTION) {
    Premium.Controller.checkPremium(context)
}
```
