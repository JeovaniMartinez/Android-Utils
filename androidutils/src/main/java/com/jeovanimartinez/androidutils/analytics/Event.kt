package com.jeovanimartinez.androidutils.analytics

/**
 * Constants with the names of the events for Firebase Analytics.
 * */
internal object Event {

    // Check the library docs for a events and parameters description.

    const val BILLING_CHECK_PREMIUM_CLIENT = "billing_check_premium_client"
    const val BILLING_CHECK_PREMIUM_PREFERENCES = "billing_check_premium_preferences"
    const val BILLING_CLIENT_CONNECTION_OK = "billing_client_connection_ok"
    const val BILLING_CLIENT_CONNECTION_ERROR = "billing_client_connection_error"
    const val BILLING_CLIENT_DISCONNECTED = "billing_client_disconnected"
    const val BILLING_SKU_DETAILS_OK = "billing_sku_details_ok"
    const val BILLING_SKU_DETAILS_ERROR = "billing_sku_details_error"
    const val BILLING_FLOW_LAUNCH_OK = "billing_flow_launch_ok"
    const val BILLING_FLOW_LAUNCH_ERROR = "billing_flow_launch_error"
    const val BILLING_PURCHASE_CANCELLED = "billing_purchase_cancelled"
    const val BILLING_PURCHASE_COMPLETED = "billing_purchase_completed"
    const val BILLING_PURCHASE_ACKNOWLEDGE_ERROR = "billing_purchase_acknowledge_error"

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*
    * IMPORTANT
    * The above events are still not fully verified and may change and be adjusted. The events below this comment are
    * fully ready for the first final version of the library and are being moved from top to bottom as they are reviewed,
    * validated, and tested.
    * */

    // About App
    const val ABOUT_APP_SHOWN = "about_app_shown"
    const val ABOUT_APP_OSL_SHOWN = "about_app_osl_shown"
    const val ABOUT_APP_TERMS_POLICY_SHOWN = "about_app_terms_policy_shown"
    const val ABOUT_APP_HELP_SECTION_SHOWN = "about_app_help_section_shown"

    // Developer Apps
    const val DEV_APPS_SHOWN_LIST_GOOGLE_PLAY = "dev_apps_shown_list_google_play"

    // Email Utils
    const val EMAIL_UTILS_SEND_EMAIL_EXTERNAL_APP = "email_utils_send_email_external_app"

    // Rate App
    const val RATE_APP_REQUEST_REVIEW_FLOW = "rate_app_request_review_flow"
    const val RATE_APP_REVIEW_FLOW_OK = "rate_app_review_flow_ok"
    const val RATE_APP_REVIEW_FLOW_SHOWN = "rate_app_review_flow_shown"
    const val RATE_APP_SENT_GOOGLE_PLAY = "rate_app_sent_google_play"

    // Share Utils
    const val SHARE_UTILS_SHARE_LAUNCHED = "share_utils_share_launched"
    const val SHARE_UTILS_SHARE_COMPLETED = "share_utils_share_completed"

    // System Web Browser
    const val SYSTEM_WEB_BROWSER_OPEN_URL = "system_web_browser_open_url"

    /**
     * Constants with the names of the parameters for the events.
     * */
    object Parameter {

        // Email Utils
        const val EMAIL_UTILS_SEND_EMAIL_EXTERNAL_APP_CASE = "send_email_case"

        // System Web Browser
        const val SYSTEM_WEB_BROWSER_OPEN_URL_CASE = "open_url_case"

        // Share Utils
        const val SHARE_UTILS_SHARE_CASE = "share_case"
        const val SHARE_UTILS_SHARE_SELECTED_APP = "share_selected_app"

    }

    /**
     * Default values that can be applied to parameter values.
     * */
    object ParameterValue {

        const val N_A = "N/A"

    }

}
