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
This is disabled by default, to enable it, you must define the Firebase Analytics instance, this must be done in in the `onCreate()` of the singleton or 
main activity, when assigning the instance, the record of events will be enabled for all library utilities.

```kotlin
Base.firebaseAnalyticsInstance = FirebaseAnalytics.getInstance(context)
```

In case you want to disable the event log for a specific utility, it can be done by:
```kotlin
MoreAppsGPlay.firebaseAnalyticsEnabled = false
```
In the example, the `MoreAppsGPlay` utility does not log events to Firebase Analytics, but all the other utilities will.

---

## Event list

The following list shows the events that the library can log and their description.

:::note
Some events have <Parameter>parameters</Parameter>, for the correct analysis of these events it is necessary to configure them in the Firebase console 
or Google Analytics console.
:::

<br/>

### AboutApp

---
<Event>about_app_shown</Event><br/>
About activity is displayed.

---
<Event>about_app_open_source_licenses_shown</Event><br/>
Open source licenses are displayed in the about activity.

---
<Event>about_app_terms_policy_shown</Event><br/>
The license terms and privacy policy are displayed in the about activity.

---
<br/>

### MoreAppsGPlay

---
<Event>more_apps_sent_to_google_play</Event><br/>
The user is directed to the developer page, either in the web browser or on Google Play app.

---
<Event>more_apps_unable_to_show_dev_page</Event><br/>
It is not possible to direct the user to the developer page, since the URL could not be opened in the web browser or in Google Play.

---
<br/>

### RateApp

---
<Event>rate_app_review_flow_successful</Event><br/>
The flow to invite the user to rate the app was successfully obtained.

---
<Event>rate_app_request_review_flow_error</Event><br/>
There was an error getting the flow to invite the user to rate the app.

---
<Event>rate_app_review_flow_showed</Event><br/>
The flow to invite the user to rate the app was shown (the review API does not provide the result, but it can be inferred, although it is not 100% accurate).

---
<Event>rate_app_review_flow_failure</Event><br/>
Event Description ...The flow could be fetched (rate_app_review_flow_successful) but could not be displayed.

---
<Event>rate_app_dialog_shown</Event><br/>
The dialog to rate the app was shown, it only applies in versions prior to Android 5 where the revision API is not available.

---
The count of flow requested can be calculated by summing the `rate_app_review_flow_successful`, `rate_app_request_review_flow_error` and `rate_app_review_flow_failure`.

---
<Event>rate_app_sent_to_google_play_app</Event><br/>
The user is directed to the Google Play app to rate the application.

---
<Event>rate_app_sent_to_google_play_web</Event><br/>
The user is directed to the Google Play website to rate the app, as it was not possible to direct them to the Google Play app.

---
<Event>rate_app_unable_to_show_on_google_play</Event><br/>
The user cannot be directed to the Google Play app or the website to rate the app.

---
<br/>

### SystemWebBrowser

---
<Event>open_url_in_system_web_browser</Event><br/>
The user is directed to a web page in the system web browser.
<br/><br/>
<Parameter>open_url_case</Parameter><br/>
Parameter that indicates the reason why the user was directed tosyste web browser, and also records when an error occurs.
