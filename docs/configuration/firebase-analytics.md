---
id: firebase-analytics
title: Firebase Analytics
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

:::note
Event logging into Firebase Analytics is **optional** and is disabled by default.
:::

## Configuration

:::important
If you want to enable event logging, your project must have Firebase Analytics configured.<br/>
[Guide](https://firebase.google.com/docs/analytics/get-started?platform=android)
:::

The custom event log is very useful to understand how users interact with the app, the library utilities record useful events in Firebase Analytics.
This is disabled by default, to enable it, you must define the Firebase Analytics instance, this must be done in the `onCreate()` of the singleton or 
main activity, when assigning the instance, the record of events will be enabled for all library utilities.

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
Some events have <Parameter>parameters</Parameter> for the correct analysis of these events it is necessary to configure them in the Firebase console 
or Google Analytics console.
:::

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



<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>

# Pending to adjust


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
<Event>more_apps_shown_google_play</Event><br/><br/>
The user is directed to Google Play, to the list of the developer apps.

---
<Event>more_apps_shown_google_play_error</Event><br/><br/>
It is not possible to direct the user to the developer apps list in Google Play.

---
<br/>

### Rate App

---
<Event>rate_app_flow_request_ok</Event><br/><br/>
<Event>rate_app_flow_request_error</Event><br/><br/>
They indicate if the purchase flow could or could not be started.

---
























### System Web Browser

---
<Event>open_url_system_web_browser</Event><br/><br/>
The user is directed to a web page in the system web browser.
<br/><br/>
<Parameter>open_url_case</Parameter><br/><br/>
Parameter that indicates the reason why the user was directed to the system web browser, and also records when an error occurs.

---
<br/>
