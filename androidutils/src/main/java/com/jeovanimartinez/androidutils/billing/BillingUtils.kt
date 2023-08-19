package com.jeovanimartinez.androidutils.billing

import com.jeovanimartinez.androidutils.Base
import com.android.billingclient.api.BillingClient.BillingResponseCode
import androidx.annotation.StringRes
import com.jeovanimartinez.androidutils.R

/**
 * Set of utilities to work with Google Play Billing Library.
 * */
object BillingUtils : Base<BillingUtils>() {

    override val LOG_TAG = "BillingUtils"

    /**
     * Data class with information about a [BillingResponseCode]
     * @param code The [BillingResponseCode]
     * @param shortDesc Short description to identify the response code for development purposes.
     * @param messageResId IDs of string resources with a user-understandable message that can be displayed
     *        according to the current response code.
     * */
    data class BillingResponseCodeInfo(val code: Int, val shortDesc: String, @StringRes val messageResId: Int)

    /**
     * Returns an [BillingResponseCodeInfo] object according to the received [code] of [BillingResponseCode]
     * */
    fun getBillingResponseCodeInfo(code: Int): BillingResponseCodeInfo {
        return when (code) {
            BillingResponseCode.BILLING_UNAVAILABLE -> {
                BillingResponseCodeInfo(code, "BILLING_UNAVAILABLE", R.string.about_app_contact_email_chooser_title)
            }

            BillingResponseCode.DEVELOPER_ERROR -> {
                BillingResponseCodeInfo(code, "DEVELOPER_ERROR", R.string.about_app_contact_email_chooser_title)
            }

            BillingResponseCode.ERROR -> {
                BillingResponseCodeInfo(code, "ERROR", R.string.about_app_contact_email_chooser_title)
            }

            BillingResponseCode.FEATURE_NOT_SUPPORTED -> {
                BillingResponseCodeInfo(code, "FEATURE_NOT_SUPPORTED", R.string.about_app_contact_email_chooser_title)
            }

            BillingResponseCode.ITEM_ALREADY_OWNED -> {
                BillingResponseCodeInfo(code, "ITEM_ALREADY_OWNED", R.string.about_app_contact_email_chooser_title)
            }

            BillingResponseCode.ITEM_NOT_OWNED -> {
                BillingResponseCodeInfo(code, "ITEM_NOT_OWNED", R.string.about_app_contact_email_chooser_title)
            }

            BillingResponseCode.ITEM_UNAVAILABLE -> {
                BillingResponseCodeInfo(code, "ITEM_UNAVAILABLE", R.string.about_app_contact_email_chooser_title)
            }

            BillingResponseCode.NETWORK_ERROR -> {
                BillingResponseCodeInfo(code, "NETWORK_ERROR", R.string.about_app_contact_email_chooser_title)
            }

            BillingResponseCode.OK -> {
                BillingResponseCodeInfo(code, "OK", R.string.about_app_contact_email_chooser_title)
            }

            BillingResponseCode.SERVICE_DISCONNECTED -> {
                BillingResponseCodeInfo(code, "SERVICE_DISCONNECTED", R.string.about_app_contact_email_chooser_title)
            }

            @Suppress("DEPRECATION")
            BillingResponseCode.SERVICE_TIMEOUT -> {
                BillingResponseCodeInfo(code, "SERVICE_TIMEOUT", R.string.about_app_contact_email_chooser_title)
            }

            BillingResponseCode.SERVICE_UNAVAILABLE -> {
                BillingResponseCodeInfo(code, "SERVICE_UNAVAILABLE", R.string.about_app_contact_email_chooser_title)
            }

            BillingResponseCode.USER_CANCELED -> {
                BillingResponseCodeInfo(code, "USER_CANCELED", R.string.about_app_contact_email_chooser_title)
            }

            else -> {
                BillingResponseCodeInfo(code, "INVALID_CODE", R.string.about_app_contact_email_chooser_title)
            }
        }
    }

}
