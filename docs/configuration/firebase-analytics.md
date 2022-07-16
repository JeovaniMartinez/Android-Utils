---
id: firebase-analytics
title: Firebase And Google Analytics
sidebar_label: Analytics
description: Optional event logging in Firebase Analytics.
---

export const Event = ({children}) => ( <span style={{
    backgroundColor: '#004234',
    borderRadius: '6px',
    color: '#fff',
    padding: '0.2rem 0.8rem',
}}>{children}</span> );

export const Parameter = ({children}) => ( <span style={{
    backgroundColor: '#807300',
    borderRadius: '6px',
    color: '#fff',
   padding: '0.2rem 0.8rem',
}}>{children}</span> );

## Description

The custom event log is very useful to understand how users interact with the app. Some library utilities can record useful events in Firebase and
Google Analytics.

:::note
Event logging in Firebase and Google Analytics is **optional** and is disabled by default.
:::

:::important
If you enable custom event logging, all logs will be visible in your Firebase or Google Analytics project. This event log is intended for 
you to analyze how the user interacts with your application. Events are only sent to the Firebase account you have configured in your project.
:::

---

## Configuration

:::important
If you want to enable event logging, your project must have Firebase Analytics configured and optionally Google Analytics enabled.
**[Guide.](https://firebase.google.com/docs/analytics/get-started?platform=android)**
:::

To enable library custom event log, you must define the Firebase Analytics instance, this must be done in the `onCreate()` of the
application class or in the main activity. When assigning the instance, the record of events will be enabled for all library utilities.

```kotlin
Base.firebaseAnalyticsInstance = FirebaseAnalytics.getInstance(context)
```

In case you want to disable the event log for a specific utility, it can be done by:
```kotlin
MoreApps.firebaseAnalyticsEnabled = false
```
In the example, the `MoreApps` utility does not log events to Firebase Analytics, but all the other utilities will.

---

## Event list

The following list shows the events that the library can log and their description.

:::note
Some events have <Parameter>parameters.</Parameter> For an analysis of these parameters, 
[custom dimensions and metrics](https://support.google.com/analytics/answer/10075209?hl=en)
can be created in Firebase or Google Analytics console.
:::

<br/>

### About App

---
<Event>about_app_shown</Event><br/><br/>
The about activity is shown to the user.

---
<Event>about_app_osl_shown</Event><br/><br/>
The open source licenses are shown in the about activity.

---
<Event>about_app_terms_policy_shown</Event><br/><br/>
The license terms and privacy policy are shown in the about activity.

---
<br/>



### More Apps

---
<Event>more_apps_shown_google_play_ok</Event><br/><br/>
The user is directed to Google Play, to the list of the developer apps.

---
<Event>more_apps_shown_google_play_error</Event><br/><br/>
It is not possible to direct the user to the developer apps list in Google Play.

---
<br/>



### Premium App

---
<Event>billing_check_premium_client</Event><br/><br/>
The premium state of the app is verified directly with the Google Play billing client.

---
<Event>billing_check_premium_preferences</Event><br/><br/>
The premium state of the app is verified through the preferences, since it was not possible to do so through the Google Play billing client.

---
<Event>billing_client_connection_ok</Event><br/><br/>
<Event>billing_client_connection_error</Event><br/><br/>
<Event>billing_client_disconnected</Event><br/><br/>
They indicate if the connection with the Google Play billing client could be established, and if the connection was lost at any time.

---
<Event>billing_sku_details_ok</Event><br/><br/>
<Event>billing_sku_details_error</Event><br/><br/>
They indicate whether or not the details of a product could be obtained.

---
<Event>billing_flow_launch_ok</Event><br/><br/>
<Event>billing_flow_launch_error</Event><br/><br/>
They indicate if the purchase flow could or could not be started.

---
<Event>billing_purchase_cancelled</Event><br/><br/>
The purchase was canceled, either by the user or because the payment method was rejected or an error occurred.

---
<Event>billing_purchase_completed</Event><br/><br/>
<Event>billing_purchase_acknowledge_error</Event><br/><br/>
Indicates if the purchase was completed successfully or if there was an error on acknowledged it. These events are always emitted after the 
payment is approved.

---
<br/>



### Rate App

---
<Event>rate_app_flow_request_ok</Event><br/><br/>
<Event>rate_app_flow_request_error</Event><br/><br/>
They indicate whether or not the review flow could be obtained.

---
<Event>rate_app_flow_launch_ok</Event><br/><br/>
<Event>rate_app_flow_launch_error</Event><br/><br/>
Indicates whether or not the review flow could be launched (show to the user).

---
<Event>rate_app_flow_shown</Event><br/><br/>
The flow to invite the user to rate the app was shown (the review API does not provide the result, but it can be inferred, although it is not 
100% accurate).

---
<Event>rate_app_dialog_shown</Event><br/><br/>
The dialog to rate the app was shown, it only applies in versions prior to Android 5 where the revision API is not available.

---
<Event>rate_app_sent_google_play_app</Event><br/><br/>
<Event>rate_app_sent_google_play_web</Event><br/><br/>
<Event>rate_app_sent_google_play_error</Event><br/><br/>
They indicate where the user was directed to rate the app. And the last event indicates that the user could not be directed to either the Google Play 
app or the website.

---
<br/>



### System Web Browser

---
<Event>open_url_system_web_browser</Event><br/><br/>
The user is directed to a web page in the system web browser.
<br/><br/>
<Parameter>open_url_case</Parameter><br/><br/>
Parameter that indicates the reason why the user was directed to the system web browser, and also records when an error occurs.

---
<br/>
