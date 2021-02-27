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
Event logging into Firebase Analytics is optional and is disabled by default.
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

## Event list

The following list shows the events that the library can log and their description.

:::note
Some events have <Parameter>parameters</Parameter>, for the correct analysis of these events it is necessary to configure them in the Firebase console or Google Analytics console.
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
<br/><br/>

### MoreAppsGPlay

---
<Event>more_apps_sent_to_google_play</Event><br/>
Event Description ...

---
<Event>more_apps_unable_to_show_dev_page</Event><br/>
Event Description ...

---
<br/><br/>

### RateApp

---
<Event>rate_app_sent_to_google_play_app</Event><br/>
Event Description ...

---
<Event>rate_app_sent_to_google_play_web</Event><br/>
Event Description ...

---
<Event>rate_app_unable_to_show_on_google_play</Event><br/>
Event Description ...

---
<br/><br/>

### RateInApp

---
<Event>rate_app_review_flow_successful</Event><br/>
Event Description ...

---
<Event>rate_app_request_review_flow_error</Event><br/>
Event Description ...

---
<Event>rate_app_review_flow_showed</Event><br/>
Event Description ...

---
<Event>rate_app_review_flow_failure</Event><br/>
Event Description ...

---
<Event>rate_app_dialog_shown</Event><br/>
Event Description ...

---
<br/><br/>

### SystemWebBrowser

---
<Event>open_url_in_system_web_browser</Event><br/>
Event Description ...
<br/><br/>
<Parameter>open_url_case</Parameter><br/>
Parameter Description ...
