@file:Suppress("unused")

package com.jeovanimartinez.androidutils.billing.premium

import com.jeovanimartinez.androidutils.Base
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.BillingClient.BillingResponseCode

/**
 * Set of utilities to simplify the verification and purchase process of the premium version of the app.
 * */
object Premium : Base<Premium>() {

    override val LOG_TAG = "Premium"

    /**
     * Define the possible states when checking if a user has premium privileges in the app.
     * */
    enum class State {

        /**
         * The user does not have premium privileges.
         * This indicates that the user has never purchased a premium version of the app, so
         * should be denied access to premium features.
         * */
        NOT_PREMIUM,

        /**
         * Indicates that the user has started the process of purchasing a premium version of
         * the app, but the purchase has not yet been completed.
         * In this case, access to premium features should be denied until the transaction
         * status changes, whether it is completed or canceled.
         * */
        PENDING_TRANSACTION,

        /**
         * The user has premium privileges.
         * This indicates that at some point the user has already paid for the purchase of a
         * premium version of the app, so should be granted access to premium features.
         * */
        PREMIUM,

    }

    /**
     * Listener for events relating to app premium billing.
     * */
    interface Listener {

        /**
         * Informs if the user has premium rights to the app. It's invoked after calling **PENDING TO UPDATE**.
         * The reported [premiumState] is the one obtained directly from the billing client, and in the event that it is not possible to
         * obtain that value, the value of the preferences is reported.
         * */
        fun onCheckPremium(premiumState: State)

        /**
         * Informs the details of the products (title, price, description, etc.). It's invoked after requesting products details
         * with **PENDING TO UPDATE**
         * @param productDetailsList List with details of the products, or null if products details could not be obtained.
         * */
        fun onProductDetails(productDetailsList: List<ProductDetails>?)

        /**
         * It's invoked when an error occurs and it is not possible to start the purchase flow.
         * @param errorCode Error code based on [BillingResponseCode]
         * */
        fun onStartPurchaseError(errorCode: Int)

        /**
         * Reports the result of the purchase, and when the premium status changes.
         *
         * Can be invoked in the following cases:
         * - When launching the billing flow, this function is invoked at the end of the flow to report the [result].
         * - When the status of a pending transaction changes, it is invoked to report the [result].
         *
         * @param result Final result based on [Premium.State] indicating whether the user has premium privileges in the app.
         * */
        fun onPurchaseResult(result: State)

    }

    /**
     * Object with constants for the preferences.
     * */
    private object Preferences {
        const val FILE_NAME = "aUpPkjY1IcXzlW47AfkB" // Name for the preferences file
        const val PREMIUM_STATE = "aUpS17v1xp7eOkEMo7pM" // Key to store the last known value of the Premium status
    }

}
