@file:Suppress("unused")

package com.jeovanimartinez.androidutils.billing.premium

import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.PurchasesUpdatedListener
import com.jeovanimartinez.androidutils.Base

/**
 * Set of utilities to simplify the verification and purchase process of the premium version of the app.
 * */
object Premium : Base<Premium>() {

    override val LOG_TAG = "Premium"

    /** Current premium state. */
    private var currentPremiumState: PremiumState = PremiumState.NOT_PREMIUM // Initial value
        set(value) {
            log("Updated Premium.currentPremiumState from $field to $value")
            field = value
        }

    /**
     * Returns the current premium state.
     * **Its value must be used in all conditions within the app where it is necessary to verify the premium state.**
     * */
    fun getCurrentPremiumState(): PremiumState {
        if (!Controller.initialized) {
            throw IllegalStateException("It is necessary to call Premium.Controller.init() before being able to retrieve the current premium state")
        }
        return currentPremiumState
    }

    /**
     * Controller for the purchase and verification process of the premium version of the app.
     * */
    object Controller {

        internal var initialized = false // Helper to determine if init was already called
        private lateinit var billingClient: BillingClient // Billing client for communication with Google Play billing
        private lateinit var premiumAccessProductIds: List<String> // List of all product IDs that grant premium benefits in the application

        /**
         * Initialize and configure the utility. It must be called only once when starting the app.
         * @param context Context for initializing the utility. Using the Application Context is highly recommended.
         * @param premiumAccessProductIds List of all product IDs that grant premium benefits in the application.
         * */
        fun init(context: Context, premiumAccessProductIds: List<String>) {

            log("Invoked > init()")

            // Validations
            if (initialized) {
                logw(
                    """
                        This call to Premium.Controller.init() HAS BEEN IGNORED AND WILL HAVE NO EFFECT, 
                        as Premium.Controller.init() should only be called once when the application starts.
                    """.trimIndent()
                )
                return
            }
            require(premiumAccessProductIds.isNotEmpty()) {
                "The premiumAccessProductIds list must not be empty; it must have at least one element"
            }

            currentPremiumState = PremiumPreferences.getPremiumState(context) // The last known state is obtained
            billingClient = BillingClient.newBuilder(context).enablePendingPurchases().setListener(purchasesUpdatedListener).build()
            this.premiumAccessProductIds = premiumAccessProductIds
            initialized = true
            // It's not necessary to call connect function here

            log("The premium controller has been initialized successfully. Premium Access Product Ids: ${this.premiumAccessProductIds}")

        }

























        private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->

        }

    }

}
