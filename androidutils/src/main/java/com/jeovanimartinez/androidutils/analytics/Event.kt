package com.jeovanimartinez.androidutils.analytics

/**
 * Constants with the names of the events for Firebase Analytics.
 * */
internal object Event {

    // Check the library docs for a parameters description.

    const val ABOUT_APP_SHOWN = "about_app_shown"
    const val ABOUT_APP_OPEN_SOURCE_LICENSES_SHOWN = "about_app_open_source_licenses_shown"
    const val ABOUT_APP_TERMS_POLICY_SHOWN = "about_app_terms_policy_shown"

    const val MORE_APPS_SENT_TO_GOOGLE_PLAY = "more_apps_sent_to_google_play"
    const val MORE_APPS_UNABLE_TO_SHOW_DEV_PAGE = "more_apps_unable_to_show_dev_page"

    const val RATE_APP_REVIEW_FLOW_SUCCESSFUL = "rate_app_review_flow_successful"
    const val RATE_APP_REQUEST_REVIEW_FLOW_ERROR = "rate_app_request_review_flow_error"
    const val RATE_APP_REVIEW_FLOW_SHOWED = "rate_app_review_flow_showed"
    const val RATE_APP_REVIEW_FLOW_FAILURE = "rate_app_review_flow_failure"
    const val RATE_APP_DIALOG_SHOWN = "rate_app_dialog_shown"
    const val RATE_APP_SENT_TO_GOOGLE_PLAY_APP = "rate_app_sent_to_google_play_app"
    const val RATE_APP_SENT_TO_GOOGLE_PLAY_WEB = "rate_app_sent_to_google_play_web"
    const val RATE_APP_UNABLE_TO_SHOW_ON_GOOGLE_PLAY = "rate_app_unable_to_show_on_google_play"

    const val OPEN_URL_IN_SYSTEM_WEB_BROWSER = "open_url_in_system_web_browser"


    /**
     * Constants with the names of the parameters for the events.
     * */
    object Parameter {

        const val OPEN_URL_CASE = "open_url_case"

    }

}
