@file:Suppress("unused")

package com.jeovanimartinez.androidutils.billing.premium

/*
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.android.billingclient.api.*
import com.jeovanimartinez.androidutils.Base
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingClient.SkuType
import com.android.billingclient.api.Purchase.PurchaseState
import com.jeovanimartinez.androidutils.analytics.Event
import com.jeovanimartinez.androidutils.extensions.nullability.whenNotNull

/**
 * Set of utilities to simplify the verification and purchase process of the premium version of the app.
 * */
object Premium : Base<Premium>() {

    override val LOG_TAG = "Premium"

    /** Current premium state. */
    private var currentState: State = State.NOT_PREMIUM // Initial value
        set(value) {
            log("Update Premium.currentState from $field to $value")
            field = value
        }

    /**
     * Returns the current premium state.
     *
     * **Its value must be used in all conditions within the app where it is necessary to verify the premium state.**
     * */
    fun getCurrentState(): State {
        return currentState
    }

    /**
     * Controller for the purchase and verification process of the premium version of the app.
     * */
    object Controller {

        private lateinit var sharedPreferences: SharedPreferences // To manipulate preferences
        private lateinit var billingClient: BillingClient // Billing client for communication with Google Play billing
        private lateinit var premiumSkus: List<String> // List of all ids that grant premium benefits of the app

        private var listener: Listener? = null // Listener to report events
        private var initialized = false // Helper to determine if init was already called
        private var endedConnection = false // Helper to determine if the connection with the billing client has been ended

        // Prevents the connection from being ended if there is an important process running with the background billing client
        private var preventEndConnection = false

        /**
         * Initialize and configure the utility, it must always be called only once from the app.
         * @param context Context for initializing the utility.
         * @param premiumSkus List of all ids that grant premium benefits of the app.
         * */
        fun init(context: Context, premiumSkus: List<String>) {
            if (initialized) {
                logw("Premium.Controller is already initialized")
                return
            }

            this.premiumSkus = premiumSkus

            sharedPreferences = context.getSharedPreferences(Preferences.NAME, Context.MODE_PRIVATE)
            billingClient = BillingClient.newBuilder(context).enablePendingPurchases().setListener(purchasesUpdatedListener).build()
            endedConnection = false

            initialized = true

            log("Premium.Controller initialized. Skus: ${this.premiumSkus}")

            currentState = getPremiumStateFromPreferences() // To load the current state

            // It's not necessary to call connect function here
        }

        /**
         * Set the listener to be used when an event occurs.
         * @param listener the listener to use.
         */
        fun setListener(listener: Listener) {
            log("Called > setListener()")
            checkInitialization()
            this.listener = listener
            log("The listener for the class has been settled")
        }

        /**
         * Removes the current listener.
         * */
        fun removeListener() {
            log("Called > removeListener()")
            checkInitialization()
            listener = null
            log("The listener for the class was removed")
        }

        /**
         * Starts the process to get the details of products based on their sku (id).
         * @param context Context from which the process starts.
         * @param skuList List with the skus (ids) of the products of which you want to obtain the details.
         * The result is informed by [Premium.Listener.onSkuDetails].
         * */
        fun getSkuDetails(context: Context, skuList: List<String>) {
            log("Called > getSkuDetails(skus = $skuList)")
            checkInitialization()

            getSkuDetails(context, skuList) { skuDetails: List<SkuDetails>? ->
                listener.whenNotNull { log("Listener function invoked > onSkuDetails()"); it.onSkuDetails(skuDetails) }
                endConnection()
            }
        }

        /**
         * Start the purchase flow of the integrated product that grants the premium benefits.
         * @param activity Activity from which the process starts.
         * @param sku Id of the product to purchase.
         * */
        fun startPurchase(activity: Activity, sku: String) {
            log("Called > startPurchase()")
            checkInitialization()

            // The sku must be on the list to be able to validate the purchase later
            if (!premiumSkus.contains(sku)) {
                throw IllegalArgumentException("Cannot start the purchase because the specified sku (id) is not in the premium skus list")
            }

            connect(activity) { code ->
                if (code == BillingResponseCode.OK) {
                    getSkuDetails(activity, listOf(sku)) { skuDetails: List<SkuDetails>? ->
                        if (skuDetails != null) {
                            val billingFlowParams = BillingFlowParams.newBuilder().setSkuDetails(skuDetails[0]).build()
                            billingClient.launchBillingFlow(activity, billingFlowParams)
                            log("Started billing flow")
                            logAnalyticsEvent(Event.BILLING_FLOW_LAUNCH_OK)
                        } else {
                            logw("Unable to start the purchase flow because the product details could not be obtained")
                            logAnalyticsEvent(Event.BILLING_FLOW_LAUNCH_ERROR)
                            listener.whenNotNull {
                                log("Listener function invoked > onStartPurchaseError()")
                                it.onStartPurchaseError(BillingResponseCode.ERROR)
                            }
                            endConnection()
                        }
                    }
                } else {
                    logw("Unable to start the purchase flow because the billing client could not connect")
                    logAnalyticsEvent(Event.BILLING_FLOW_LAUNCH_ERROR)
                    listener.whenNotNull { log("Listener function invoked > onStartPurchaseError()"); it.onStartPurchaseError(code) }
                }
            }
        }

        /**
         * Check if the user has premium privileges, and reports the result by [Premium.Listener.onCheckPremium].
         * - It is first verified on the billing client, and that result is reported.
         * - If the billing client cannot report the result, the preferences result is reported.
         *
         * _Either the billing client or the preferences, the result is ALWAYS reported._
         *
         * @param context Context from which the process starts.
         * */
        fun checkPremium(context: Context) {
            log("Called > checkPremium()")
            checkInitialization()

            // Checks if the user is premium using the preferences
            val checkPremiumWithPreferences = fun() {
                log("Invoked > checkPremium() > checkPremiumWithPreferences()")

                val result = getPremiumStateFromPreferences()
                currentState = result // Update the current state
                listener.whenNotNull {
                    log("Listener function invoked > onCheckPremium() | The result is informed by the preferences")
                    logAnalyticsEvent(Event.BILLING_CHECK_PREMIUM_PREFERENCES)
                    it.onCheckPremium(result)
                }
            }

            // Checks if the user is premium using the billing client
            val checkPremiumWithBillingClient = fun() {
                log("Invoked > checkPremium() > checkPremiumWithBillingClient()")

                val purchases = billingClient.queryPurchases(SkuType.INAPP) // Get the purchases

                // Checks if the list was obtained successfully
                if (purchases.responseCode == BillingResponseCode.OK) {
                    log("The purchases list was successfully obtained. ${purchases.purchasesList}")
                    val result = handlePurchase(purchases.purchasesList) // The status of purchases is validated
                    currentState = result // Update the current state
                    listener.whenNotNull {
                        log("Listener function invoked > onCheckPremium() | The result is informed by the billing client")
                        logAnalyticsEvent(Event.BILLING_CHECK_PREMIUM_CLIENT)
                        it.onCheckPremium(result)
                    }
                    endConnection()
                } else {
                    log("Error on getting purchases list. ${getResCodeDesc(purchases.responseCode)}. Premium state it will be verified with the preferences")
                    checkPremiumWithPreferences() // In case of error, the result is checked with the preferences
                    endConnection()
                }
            }

            // The connection is verified and connected (if necessary), and based on the result, the appropriate function is invoked
            connect(context) { code ->
                if (code == BillingResponseCode.OK) {
                    log("Billing client is ready to check premium state")
                    checkPremiumWithBillingClient()
                } else {
                    log("Billing client is not ready to check premium, premium state will be verified with the preferences")
                    checkPremiumWithPreferences()
                }
            }
        }

        /**
         * Verify that this utility is already initialized, and throw an exception if it is not.
         * */
        private fun checkInitialization() {
            if (!initialized) {
                throw IllegalStateException("It is necessary to call Premium.Controller.init() before calling any other function of Premium.Controller")
            }
        }

        /**
         * Returns a short description of the [code] of BillingResponseCode.
         *
         * **Used for development purposes only.**
         * */
        private fun getResCodeDesc(code: Int): String {
            val desc = when (code) {
                BillingResponseCode.SERVICE_TIMEOUT -> "SERVICE_TIMEOUT"
                BillingResponseCode.FEATURE_NOT_SUPPORTED -> "FEATURE_NOT_SUPPORTED"
                BillingResponseCode.SERVICE_DISCONNECTED -> "SERVICE_DISCONNECTED"
                BillingResponseCode.OK -> "OK"
                BillingResponseCode.USER_CANCELED -> "USER_CANCELED"
                BillingResponseCode.SERVICE_UNAVAILABLE -> "SERVICE_UNAVAILABLE"
                BillingResponseCode.BILLING_UNAVAILABLE -> "BILLING_UNAVAILABLE"
                BillingResponseCode.ITEM_UNAVAILABLE -> "ITEM_UNAVAILABLE"
                BillingResponseCode.DEVELOPER_ERROR -> "DEVELOPER_ERROR"
                BillingResponseCode.ERROR -> "ERROR"
                BillingResponseCode.ITEM_ALREADY_OWNED -> "ITEM_ALREADY_OWNED"
                BillingResponseCode.ITEM_NOT_OWNED -> "ITEM_NOT_OWNED"
                else -> "N/A"
            }
            return "Response code: $code $desc"
        }

        /**
         * Save premium [state] in preferences.
         * */
        private fun savePremiumStateInPreferences(state: State) {
            sharedPreferences.edit().putInt(Preferences.PREMIUM_STATE, state.id).apply()
            log("Saved premium state in preferences. State = $state Value = ${state.id}")
        }

        /**
         * Gets and returns the current premium state based on preferences.
         * */
        private fun getPremiumStateFromPreferences(): State {
            val state = when (sharedPreferences.getInt(Preferences.PREMIUM_STATE, State.NOT_PREMIUM.id)) {
                State.PREMIUM.id -> State.PREMIUM
                State.PENDING_TRANSACTION.id -> State.PENDING_TRANSACTION
                else -> State.NOT_PREMIUM
            }
            log("Premium state was obtained from preferences. State = $state Value = ${state.id}")
            return state
        }

        /**
         * Initialize the connection with the billing client.
         * @param context It is used to recreate the billing client instance if the connection has already been ended,
         *        leave it as null if it is certain that the connection has not been ended.
         * @param result Asynchronous function to report the connection result, with the result code based on BillingResponseCode.
         * */
        private fun connect(context: Context?, result: (code: Int) -> Unit) {
            log("Invoked > connect()")

            if (billingClient.isReady) {
                log("Billing client is already connected")
                return result(BillingResponseCode.OK)
            }

            // If the connection with the billing client has been ended, the instance of billing client is recreated to be able to connect it again
            context.whenNotNull {
                if (endedConnection) {
                    billingClient = BillingClient.newBuilder(it).enablePendingPurchases().setListener(purchasesUpdatedListener).build()
                    endedConnection = false // It is returned to false since the instance of billing client has been recreated
                    log("Recreated billing client instance")
                }
            }

            // Tries to connect billing client
            log("Connecting billing client...")
            billingClient.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingResponseCode.OK) {
                        log("Billing client successfully connected")
                        logAnalyticsEvent(Event.BILLING_CLIENT_CONNECTION_OK)
                    } else {
                        logw("Failed to connect the Billing Client. ${getResCodeDesc(billingResult.responseCode)}")
                        logAnalyticsEvent(Event.BILLING_CLIENT_CONNECTION_ERROR)
                    }
                    result(billingResult.responseCode) // The result is reported
                }

                override fun onBillingServiceDisconnected() {
                    // At the moment, no need to handle reconnection
                    log("Billing client has been disconnected")
                    logAnalyticsEvent(Event.BILLING_CLIENT_DISCONNECTED)
                }
            })
        }

        /**
         * Ends the connection with the billing client.
         * */
        private fun endConnection() {
            log("Called > endConnection()")
            if (preventEndConnection) {
                return log("No need to end the connection, preventEndConnection is true")
            }
            if (!billingClient.isReady) {
                return log("No need to end the connection")
            }
            billingClient.endConnection()
            endedConnection = true
            log("The connection with the billing client was ended")
        }

        /**
         * Get the details of products based on their sku (id).
         * @param context Context from which the process starts.
         * @param skuList List with the skus (ids) of the products of which you want to obtain the details.
         * @param result Asynchronous function to report the result, If the information cannot be obtained, skuDetails list will be null.
         * */
        private fun getSkuDetails(context: Context, skuList: List<String>, result: (skuDetails: List<SkuDetails>?) -> Unit) {
            log("Invoked > getSkuDetails(skus = $skuList)")

            connect(context) { code ->
                if (code == BillingResponseCode.OK) {
                    // Create and configure the query
                    val skuDetailsParams = SkuDetailsParams.newBuilder().setSkusList(skuList).setType(SkuType.INAPP).build()

                    // Start the request
                    billingClient.querySkuDetailsAsync(skuDetailsParams) { billingResult, skuDetailsList ->
                        if (billingResult.responseCode == BillingResponseCode.OK) {
                            if (skuDetailsList != null && skuDetailsList.isNotEmpty()) {
                                log("Sku details list was successfully obtained. Size = ${skuDetailsList.size} Expected size = ${skuList.size}")
                                log("$skuDetailsList")
                                logAnalyticsEvent(Event.BILLING_SKU_DETAILS_OK)
                                result(skuDetailsList)
                            } else {
                                logw("Error on getting the sku details, the result list is empty.")
                                logAnalyticsEvent(Event.BILLING_SKU_DETAILS_ERROR)
                                result(null) // The result is null because it could not be obtained the details of the product
                            }
                        } else {
                            log("Failed querySkuDetailsAsync. ${getResCodeDesc(billingResult.responseCode)}")
                            logAnalyticsEvent(Event.BILLING_SKU_DETAILS_ERROR)
                            result(null) // The result is null because it could not be obtained the details of the product
                        }
                    }

                } else {
                    logw("Unable to get sku details list because the billing client could not connect")
                    logAnalyticsEvent(Event.BILLING_SKU_DETAILS_ERROR)
                    result(null) // The result is null because it could not be obtained the details of the product
                }
            }
        }

        /**
         * It is responsible for managing and validating purchases.
         * @param purchases Purchases list, to determine if any purchase of the list corresponds to a sku with premium benefits.
         * @return Current premium state.
         * */
        private fun handlePurchase(purchases: List<Purchase>?): State {
            log("Invoked > handlePurchase(purchases = $purchases)")

            var result = State.NOT_PREMIUM // Default result, updates based on validations

            if (purchases != null && purchases.isNotEmpty()) {
                log("Purchases list size: ${purchases.size}")
                purchases.forEach {
                    val sate = when (it.purchaseState) {
                        PurchaseState.PURCHASED -> "${it.purchaseState} PURCHASED"
                        PurchaseState.PENDING -> "${it.purchaseState} PENDING"
                        else -> "${it.purchaseState} UNSPECIFIED_STATE"
                    }
                    log("Purchase information = sku: ${it.sku} state: $sate acknowledge: ${it.isAcknowledged}")
                    if (premiumSkus.contains(it.sku)) {
                        // If a premium purchase was already found, there is no need to reassign the result
                        if (result != State.PREMIUM) {
                            when (it.purchaseState) {
                                PurchaseState.PURCHASED -> result = State.PREMIUM
                                PurchaseState.PENDING -> result = State.PENDING_TRANSACTION
                                PurchaseState.UNSPECIFIED_STATE -> State.NOT_PREMIUM
                            }
                        }
                        log("Premium Skus list does contain ${it.sku}")
                        log("Current result based on all validations: $result")
                        // It is verified if the purchase has already been acknowledged. If it is not already acknowledged, it acknowledges it (this process is independent)
                        acknowledgePurchase(it)
                    } else {
                        log("Premium Skus list does not contains ${it.sku} | No need to handle the purchase")
                    }
                }
            } else {
                // The State.NOT_PREMIUM result is kept, since there are no purchases
                result = State.NOT_PREMIUM
                log("The purchases list is null or empty")
            }

            log("handlePurchase final result (Premium state) = $result")
            savePremiumStateInPreferences(result) // The preference is updated with the value obtained whenever the purchase is validated
            return result
        }

        /**
         * Verifies if the [purchase] has already been acknowledged, and if not, it is acknowledged.
         * This process is independent, runs in the background and does not affect any other process.
         * The acknowledgment of the purchase is mandatory, because if it is not done after a while, Google will refund the purchase.
         * */
        private fun acknowledgePurchase(purchase: Purchase) {
            log("Invoked > acknowledgePurchase(purchase = $purchase)")

            when {
                !premiumSkus.contains(purchase.sku) -> {
                    log("The purchase cannot be acknowledged because its sku is not on the premiumSkus list")
                    return
                }
                purchase.isAcknowledged -> {
                    log("Purchase is already acknowledged")
                    return
                }
                purchase.purchaseState == PurchaseState.UNSPECIFIED_STATE -> {
                    log("No need to acknowledge the purchase, because the purchase state is ${PurchaseState.UNSPECIFIED_STATE} UNSPECIFIED_STATE")
                    return
                }
                purchase.purchaseState == PurchaseState.PENDING -> {
                    log("No need to acknowledge the purchase, because the purchase state is ${PurchaseState.PENDING} PENDING")
                    return
                }
            }

            log("The purchase is not yet recognized and the state is ${PurchaseState.PURCHASED} PURCHASED, started the process to acknowledge it")

            preventEndConnection = true
            connect(null) { code ->
                if (code == BillingResponseCode.OK) {
                    log("The billing client is ready to acknowledge the purchase")
                    val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build()
                    billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                        if (billingResult.responseCode == BillingResponseCode.OK) {
                            log("The purchase has been acknowledged successfully")
                            logAnalyticsEvent(Event.BILLING_PURCHASE_COMPLETED) // The purchase is completed until acknowledged it
                        } else {
                            logw("Failed to acknowledge the purchase. ${getResCodeDesc(billingResult.responseCode)}")
                            logAnalyticsEvent(Event.BILLING_PURCHASE_ACKNOWLEDGE_ERROR)
                        }
                        preventEndConnection = false
                        // The connection is ended as the places from where acknowledgePurchase() is invoked no longer need the connection
                        endConnection()
                    }
                } else {
                    preventEndConnection = false
                    log("The billing client is not ready to acknowledge the purchase")
                    logAnalyticsEvent(Event.BILLING_PURCHASE_ACKNOWLEDGE_ERROR)
                }
            }
        }

        /**
         * Listener to changes in purchases.
         *
         * It is invoked in two situations:
         *  - At the end of the purchase flow, whatever the result.
         *  - When the status of a pending transaction is updated.
         * */
        private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
            log("Invoked > purchasesUpdatedListener. ${getResCodeDesc(billingResult.responseCode)}. Purchases = $purchases")

            val result: State // To determine the result
            when (billingResult.responseCode) {
                BillingResponseCode.OK -> {
                    result = handlePurchase(purchases) // Purchase status is validated
                }
                BillingResponseCode.ITEM_ALREADY_OWNED -> {
                    // If this code is obtained, it is that the user already had the product, and purchases are null, so the data is updated to indicate that the user is already premium
                    result = State.PREMIUM
                    savePremiumStateInPreferences(result)
                }
                else -> {
                    // The purchase was canceled, an error occurred or the payment method was rejected. The purchase was not completed
                    result = State.NOT_PREMIUM
                    log("Purchase canceled")
                    logAnalyticsEvent(Event.BILLING_PURCHASE_CANCELLED)
                }
            }
            currentState = result // Update the current state
            log("Result (Premium state) = $result")
            listener.whenNotNull { log("Listener function invoked > onPurchaseResult()"); it.onPurchaseResult(result) }

            // If a specific result is obtained, the connection is ended
            if (result == State.PREMIUM || result == State.NOT_PREMIUM) {
                endConnection()
            } else {
                // If the transaction is pending, the connection is not ended to wait for the result
                preventEndConnection = true
            }

        }

    }

    /**
     * Define the possible states when checking if a user has premium privileges in the app.
     * @param id Numeric id of the state.
     * */
    enum class State(val id: Int) {

        /**
         * The user does not have premium privileges.
         * This indicates that the user has never purchased a premium version of the app, so
         * should be denied access to premium features.
         * */
        NOT_PREMIUM(0),

        /**
         * The user has premium privileges.
         * This indicates that at some point the user has already paid for the purchase of a
         * premium version of the app, so should be granted access to premium features.
         * */
        PREMIUM(1),

        /**
         * Indicates that the user has started the process of purchasing a premium version of
         * the app, but the purchase has not yet been completed.
         * In this case, access to premium features should be denied until the transaction
         * status changes, whether it is completed or canceled.
         * */
        PENDING_TRANSACTION(2)

    }

    /**
     * Listener for events relating to app premium billing.
     * */
    interface Listener {

        /**
         * Informs if the user has premium rights to the app. It's invoked after calling [Premium.Controller.checkPremium].
         * The reported [state] is the one obtained directly from the billing client, and in the event that it is not possible to
         * obtain that value, the value of the preferences is reported.
         * */
        fun onCheckPremium(state: State)

        /**
         * Informs the details of the products (title, price, description, etc.). It's invoked after requesting products details
         * with [Premium.Controller.getSkuDetails].
         * @param skuDetails List with details of the products, or null if products details could not be obtained.
         * */
        fun onSkuDetails(skuDetails: List<SkuDetails>?)

        /**
         * It's invoked when an error occurs and it is not possible to start the purchase flow (generally it is a connection error
         * in the billing client).
         * @param code Error code based on [BillingResponseCode]
         * */
        fun onStartPurchaseError(code: Int)

        /**
         * Reports the result of the purchase, and when the premium status changes.
         *
         * Can be invoked in the following cases:
         * - When launching the billing flow, this function is invoked at the end of the flow to report the [result].
         * - When the status of a pending transaction changes, it is invoked to report the [result].
         * */
        fun onPurchaseResult(result: State)

    }

    /**
     * Object with constants for the preferences.
     * */
    private object Preferences {
        const val NAME = "aUpPkjY1IcXzlW47AfkB" // Name for the preferences file
        const val PREMIUM_STATE = "aUpS17v1xp7eOkEMo7pM"
    }

}
*/