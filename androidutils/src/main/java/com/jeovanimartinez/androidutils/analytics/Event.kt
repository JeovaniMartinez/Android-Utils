package com.jeovanimartinez.androidutils.analytics

/**
 * Constants with the names of the events for Firebase Analytics.
 * */
internal object Event {

    // Check the library docs for a events and parameters description.

    const val ABOUT_APP_SHOWN = "about_app_shown"
    const val ABOUT_APP_OSL_SHOWN = "about_app_osl_shown"
    const val ABOUT_APP_TERMS_POLICY_SHOWN = "about_app_terms_policy_shown"

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

    const val MORE_APPS_SHOWN_GOOGLE_PLAY_OK = "more_apps_shown_google_play_ok"
    const val MORE_APPS_SHOWN_GOOGLE_PLAY_ERROR = "more_apps_shown_google_play_error"

    const val OPEN_URL_SYSTEM_WEB_BROWSER = "open_url_system_web_browser"

    const val RATE_APP_FLOW_REQUEST_OK = "rate_app_flow_request_ok"
    const val RATE_APP_FLOW_REQUEST_ERROR = "rate_app_flow_request_error"
    const val RATE_APP_FLOW_LAUNCH_OK = "rate_app_flow_launch_ok"
    const val RATE_APP_FLOW_LAUNCH_ERROR = "rate_app_flow_launch_error"
    const val RATE_APP_FLOW_SHOWN = "rate_app_flow_shown"
    const val RATE_APP_SENT_GOOGLE_PLAY = "rate_app_sent_google_play"
    const val RATE_APP_SENT_GOOGLE_PLAY_ERROR = "rate_app_sent_google_play_error"

    const val SHARE_LAUNCHED = "share_launched"
    const val SHARE_COMPLETED = "share_completed"
    const val SHARE_OLD_API = "share_old_api"

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Constants with the names of the parameters for the events.
     * */
    object Parameter {

        const val OPEN_URL_CASE = "open_url_case"

        const val SHARE_CASE = "share_case"
        const val SHARE_SELECTED_APP = "share_selected_app"

    }

    /**
     * Default values that can be applied to parameter values.
     * */
    object ParameterValue {

        const val N_A = "N/A"

    }

}
