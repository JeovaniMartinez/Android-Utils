---
id: firebase-analytics
title: Firebase Analytics
---

export const Event = ({children}) => ( <span style={{
    backgroundColor: '#004234',
    borderRadius: '6px',
    color: '#fff',
    padding: '0.2rem',
}}>{children}</span> );

export const Parameter = ({children}) => ( <span style={{
    backgroundColor: '#807300',
    borderRadius: '6px',
    color: '#fff',
    padding: '0.2rem',
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
Some events have <Parameter>parameters</Parameter>, for the correct analysis of these events it is necessary to configure them in the Firebase console 
or Google Analytics console.
:::

<br/>

### About App

---
<Event>about_app_shown</Event><br/>
The about activity is shown to the user.

---
<Event>about_app_open_source_licenses_shown</Event><br/>
Open source licenses are shown in the about activity.

---
<Event>about_app_terms_policy_shown</Event><br/>
The license terms and privacy policy are shown in the about activity.

---
<br/>

### More Apps

---
<Event>more_apps_shown_on_google_play</Event><br/>
The user is directed to Google Play, to the list of the developer apps.

---
<Event>more_apps_unable_to_show_on_google_play</Event><br/>
It is not possible to direct the user to the developer apps list in Google Play.

---
<br/>

### Rate App

---
<Event>rate_app_request_review_flow_successful</Event><br/>
The request to get the review flow was successful.

---
<Event>rate_app_request_review_flow_error</Event><br/>
Error on get review flow request.

---
<Event>rate_app_launch_review_flow_successful</Event><br/>
The review flow to rate app was launched successful.

---
<Event>rate_app_launch_review_flow_error</Event><br/>
Error on launch the review flow to rate app.

---
<Event>rate_app_review_flow_shown</Event><br/>
The flow to invite the user to rate the app was shown (the review API does not provide the result, but it can be inferred, although it is not 
100% accurate).

---
<Event>rate_app_dialog_shown</Event><br/>
The dialog to rate the app was shown, it only applies in versions prior to Android 5 where the revision API is not available.

---
<Event>rate_app_sent_to_google_play_app</Event><br/>
The user is directed to the Google Play app to rate the app.

---
<Event>rate_app_sent_to_google_play_web</Event><br/>
The user is directed to the Google Play website to rate the app, as it was not possible to direct them to the Google Play app.

---
<Event>rate_app_unable_to_send_to_google_play</Event><br/>
The user cannot be directed to the Google Play app or the website to rate the app.

---
<br/>

### System Web Browser

---
<Event>open_url_in_system_web_browser</Event><br/>
The user is directed to a web page in the system web browser.
<br/><br/>
<Parameter>open_url_case</Parameter><br/>
Parameter that indicates the reason why the user was directed to the system web browser, and also records when an error occurs.