@file:Suppress("unused")

package com.jeovanimartinez.androidutils.billing.premium

import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PurchasesUpdatedListener
import com.jeovanimartinez.androidutils.Base
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingClient.ConnectionState
import com.jeovanimartinez.androidutils.billing.BillingUtils

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

        /*
        * Prevents the connection from being ended if there is an important process running with the billing client in the background.
        * For example, if the status of a purchase is "pending transaction" and the result is being awaited.
        * */
        private var preventEndBillingClientConnection = false

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
            preventEndBillingClientConnection = false
            initialized = true
            // It's not necessary to call connect function here

            log("The premium controller has been initialized successfully. Premium Access Product Ids: ${this.premiumAccessProductIds}")

        }

        /**
         * Initialize the connection with the billing client. Call this when need connecting to the billing client or before
         * performing any billing-related task to ensure it is connected.
         * @param context It is used to recreate the billing client instance if the connection has already been closed,
         *        set it as null if it is certain that the connection has not been closed.
         * @param result Asynchronous function callback to report the connection result, with the result code based on [BillingResponseCode]
         * */
        fun connectBillingClient(context: Context?, result: (resultCode: Int) -> Unit) {

            log("Invoked > connectBillingClient()")

            if (billingClient.isReady) {
                log("The billing client is already connected")
                return result(BillingResponseCode.OK)
            }

            if (billingClient.connectionState == ConnectionState.CLOSED) {
                log("The billing client connection is already closed")
                if (context != null) {
                    billingClient = BillingClient.newBuilder(context).enablePendingPurchases().setListener(purchasesUpdatedListener).build()
                    log("Recreated billing client instance")
                } else {
                    logw("The context is null, the billing client instance cannot be recreated")
                    /*
                    * DEVELOPER_ERROR is returned as the response, as the context should only be null when it is certain that the connection
                    * with the billing client has not been closed
                    * */
                    return result(BillingResponseCode.DEVELOPER_ERROR)
                }
            }

            billingClient.startConnection(object : BillingClientStateListener {

                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    val info = BillingUtils.getBillingResponseCodeInfo(billingResult.responseCode)
                    log("Billing client setup finished. Result: ${info.shortDesc} | Message: ${billingResult.debugMessage}")
                    result(billingResult.responseCode)
                }

                override fun onBillingServiceDisconnected() {
                    // At the moment, no need to handle reconnection
                    log("The billing client has been disconnected")
                }

            })

        }

        /**
         * Closes/end the billing client connection.
         * If [preventEndBillingClientConnection] is true, the connection is not closed even if this function is called.
         * */
        fun endBillingClientConnection() {

            log("Invoked > endBillingClientConnection()")

            if (preventEndBillingClientConnection) {
                log("No need to close the billing client connection. preventEndBillingClientConnection is true")
                return
            }
            if (!billingClient.isReady) {
                log("No need to close the billing client connection. Billing Client is not ready or the connection has already been closed")
                return
            }

            billingClient.endConnection()

            log("The connection with the billing client was closed")

        }


        /*
        * Pruebas y validado el 19.08.2023
        * public @interface ConnectionState {
                int DISCONNECTED = 0; // Estado inicial, luego ce llamar a init y al .build() del billing client; billingClient.isReady = false
                int CONNECTING = 1; // Se esta conectando ... billingClient.isReady = false; al llamar a billingClient.startConnection
                int CONNECTED = 2; // Esta ya conectado billingClient.isReady = true; Luego de logw("onBillingSetupFinished")
                int CLOSED = 3; // Luego de llamar a endConnection(); billingClient.isReady = false; una vez cerrada no es posible conectarse nuevamente, hay que crecrear la instancia
            }
            *
            *
        * */

        fun tmpReinstanciarReconectar(context: Context) {
            billingClient = BillingClient.newBuilder(context).enablePendingPurchases().setListener(purchasesUpdatedListener).build()
            tmpConect()
        }

        fun tmpConect() {
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingServiceDisconnected() {
                    logw("onBillingServiceDisconnected")
                }

                override fun onBillingSetupFinished(p0: BillingResult) {
                    logw("onBillingSetupFinished")
                }

            })
        }


        fun billingClientCurrentState() {
            logw(billingClient.connectionState)
            logw(billingClient.isReady)
        }

        private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->

        }

    }

}
