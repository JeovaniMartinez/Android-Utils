package com.jeovanimartinez.androidutils.billing.premium

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingClient.ConnectionState
import com.android.billingclient.api.BillingClient.ProductType
import com.android.billingclient.api.Purchase.PurchaseState
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryProductDetailsParams.Product
import com.android.billingclient.api.QueryPurchasesParams
import com.jeovanimartinez.androidutils.Base
import com.jeovanimartinez.androidutils.analytics.Event
import com.jeovanimartinez.androidutils.billing.BillingUtils
import com.jeovanimartinez.androidutils.extensions.nullability.whenNotNull

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
        private var premiumListener: PremiumListener? = null  // Listener to report events

        /*
        * Determines if a purchase is currently being acknowledged, in order to prevent closing the billing client connection and releasing
        * resources during the process.
        * */
        private var acknowledgePurchaseInProgress = false

        /*
        * It's used to temporarily store the application context for use in the PurchasesUpdatedListener and the functions called when
        * the listener is triggered. Once the context is no longer needed, it's set to null to avoid unnecessary references.
        * */
        private var applicationContext: Context? = null
            set(value) {
                log("Updated applicationContext to = $value")
                field = value
            }

        // Listener for updates in the state of purchases
        private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
            log("Triggered > PurchasesUpdatedListener.onPurchasesUpdated()")
            onPurchasesUpdated(billingResult, purchases)
        }

        /**
         * Initialize and configure the utility. It must be called only once when starting the app.
         * @param context Context for initializing the utility.
         * @param premiumAccessProductIds List of all product IDs that grant premium benefits in the application.
         * @throws IllegalArgumentException If [premiumAccessProductIds] list is empty.
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

            /*
            * IMPORTANT
            * In some places, such as when connecting with the client, context.applicationContext is used instead of the provided context.
            * This is done to ensure that the BillingClient instance can be correctly re-created if required and to maintain the connection
            * open when necessary.
            * */

            currentPremiumState = PremiumPreferences.getPremiumState(context) // The last known state is obtained
            billingClient = BillingClient.newBuilder(context.applicationContext).enablePendingPurchases().setListener(purchasesUpdatedListener).build()
            this.premiumAccessProductIds = premiumAccessProductIds
            acknowledgePurchaseInProgress = false
            initialized = true
            // It's not necessary to call connect function here

            log("The premium controller has been initialized successfully. Premium Access Product Ids: ${this.premiumAccessProductIds}")

        }

        /**
         * Sets the listener to receive premium events.
         * @param listener The listener to be set.
         * */
        fun setListener(listener: PremiumListener) {
            log("Invoked > setListener()")
            checkInitialization()
            premiumListener = listener
            log("The listener to report premium events has been assigned")
        }

        /**
         * Removes the listener set to receive premium events.
         * */
        fun removeListener() {
            log("Invoked > removeListener()")
            checkInitialization()
            premiumListener = null
            log("The listener to report premium events has been removed. premiumListener = null")
        }

        /**
         * Check if the user has premium privileges, and reports the result by [PremiumListener.onCheckPremiumState].
         * - It is first verified directly on the billing client, and that result is reported.
         * - If the billing client cannot report the result, the stored value of Shared Preferences is reported
         *   which is the latest known value of PremiumState.
         *
         * _Either the billing client or the preferences, the result is ALWAYS reported._
         *
         * @param context Context from which the process starts.
         * */
        fun checkPremiumState(context: Context) {

            log("Invoked > checkPremiumState()")
            checkInitialization()

            // Check the last known premium state from preferences if it's not possible to retrieve the information from the billing client
            val checkPremiumStateFromPreferences = fun() {
                log("Invoked > checkPremiumState() > checkPremiumStateFromPreferences()")
                log("The premium state is going to be reported from the Shared Preferences")

                currentPremiumState = PremiumPreferences.getPremiumState(context)

                logPremiumListenerTriggered("onCheckPremiumState()")
                premiumListener?.onCheckPremiumState(currentPremiumState, false)
            }

            connectBillingClient(context.applicationContext) { resultCode ->
                if (resultCode == BillingResponseCode.OK) {

                    // The query is executed to determine the purchases that the user has made in the app
                    val params = QueryPurchasesParams.newBuilder().setProductType(ProductType.INAPP).build()
                    billingClient.queryPurchasesAsync(params) { billingResult, purchases ->

                        /*
                        * If a purchase is refunded but the access entitlement are not removed, the purchase will appear in the Google Play
                        * console as refunded. However, the list of purchases will still include that purchase. Although it has been refunded,
                        * the access entitlement have not been removed. As a result, the purchase will still appear here and continue to grant
                        * premium benefits to the user if the product id is in premiumAccessProductIds list.
                        * */

                        val info = BillingUtils.getBillingResponseCodeInfo(billingResult.responseCode)
                        log("Billing client queryPurchasesAsync Result: ${info.shortDesc} | Message: ${billingResult.debugMessage} | Purchases: $purchases")

                        if (billingResult.responseCode == BillingResponseCode.OK) {
                            val result = handlePurchase(context.applicationContext, purchases)
                            logPremiumListenerTriggered("onCheckPremiumState()")
                            premiumListener?.onCheckPremiumState(result, true)
                            /*
                            * IMPORTANT
                            * In handlePurchase(), the value of currentPremiumState is updated and saved in preferences, so there is no longer a need to do it here
                            * */
                        } else {
                            logw("The premium state couldn't be verified from the billing client because the purchases could not be obtained")
                            checkPremiumStateFromPreferences()
                        }

                        endBillingClientConnection()
                    }
                } else {
                    logw(
                        "The premium state couldn't be verified from the billing client because the connection to the billing client " +
                                "could not be established. Connection result: ${BillingUtils.getBillingResponseCodeInfo(resultCode).shortDesc}"
                    )
                    checkPremiumStateFromPreferences()
                }
            }

        }

        /**
         * Starts the process to retrieve details of one or more In-app products asynchronously. The result is
         * informed by [PremiumListener.onProductsDetails]
         * @param context Context from which the process starts.
         * @param productIds List containing the product IDs to be queried.
         * */
        fun getProductsDetails(context: Context, productIds: List<String>) {
            log("Invoked > getProductsDetails()")
            checkInitialization()

            require(productIds.isNotEmpty()) {
                "The productIds list must not be empty; it must have at least one element"
            }

            // A warning is logged if any of the provided product IDs is not in the premiumAccessProductIds list
            productIds.forEach {
                if (!premiumAccessProductIds.contains(it)) {
                    logw("The '$it' Id is not in the Premium.Controller.premiumAccessProductIds list $premiumAccessProductIds")
                }
            }

            // The process is carried out through a private function, and the result is reported
            getProductsDetails(context.applicationContext, productIds) { resultCode, productDetailsList ->

                logPremiumListenerTriggered("onProductsDetails()")
                premiumListener?.onProductsDetails(resultCode, productDetailsList)

                endBillingClientConnection()
            }

        }

        /**
         * Start the purchase flow of a integrated product that grants the premium benefits for the app.
         * @param activity Activity from which the process starts.
         * @param productId Id of the product to purchase.
         * */
        fun startProductPurchase(activity: Activity, productId: String) {
            log("Invoked > startProductPurchase()")
            checkInitialization()

            // The product Id must be on the premiumAccessProductIds list to be able to validate the purchase later
            if (!premiumAccessProductIds.contains(productId)) {
                throw IllegalArgumentException(
                    "Cannot start the purchase flow because the specified product Id '$productId' is not in the premiumAccessProductIds list $premiumAccessProductIds"
                )
            }

            connectBillingClient(activity.applicationContext) { connectionResultCode ->
                if (connectionResultCode == BillingResponseCode.OK) {

                    // The product details are obtained in order to initiate the purchase flow
                    getProductsDetails(activity.applicationContext, listOf(productId)) { productDetailsResultCode, productDetailsList ->

                        // As only one product is requested, if productDetailsList is not null, it must contain the data for that product
                        if (!productDetailsList.isNullOrEmpty()) {

                            // The purchase flow is configured and launched
                            val productDetailsParamsList = listOf(BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(productDetailsList[0]).build())
                            val billingFlowParams = BillingFlowParams.newBuilder().setProductDetailsParamsList(productDetailsParamsList).build()
                            val billingResult = billingClient.launchBillingFlow(activity, billingFlowParams)

                            val info = BillingUtils.getBillingResponseCodeInfo(billingResult.responseCode)
                            log("Billing client launchBillingFlow. Result: ${info.shortDesc} | Message: ${billingResult.debugMessage}")

                            /*
                             * NOTE: In tests conducted, if the product has already been purchased or if there is no internet connection,
                             * the billingResult.responseCode remains OK and the error message is displayed in the modal of the opened purchase flow.
                             * */

                            if (billingResult.responseCode == BillingResponseCode.OK) {
                                applicationContext = activity.applicationContext // To have it available in the listener when the result is reported
                                logAnalyticsEvent(Event.PREMIUM_BILLING_LAUNCHED_BILLING_FLOW)
                            } else {
                                // If the purchase flow could not be launched
                                endBillingClientConnection()
                            }

                            logPremiumListenerTriggered("onStartPurchaseResult()")
                            premiumListener?.onStartPurchaseResult(billingResult.responseCode)

                        } else {
                            logw("Unable to start the purchase flow because the product details could not be obtained")
                            logPremiumListenerTriggered("onStartPurchaseResult()")
                            premiumListener?.onStartPurchaseResult(productDetailsResultCode)
                            endBillingClientConnection()
                        }
                    }

                } else {
                    logw(
                        "Unable to start the purchase flow because the connection to the billing client could not be established. " +
                                "Connection result: ${BillingUtils.getBillingResponseCodeInfo(connectionResultCode).shortDesc}"
                    )
                    logPremiumListenerTriggered("onStartPurchaseResult()")
                    premiumListener?.onStartPurchaseResult(connectionResultCode)
                }
            }

        }

        /**
         * Check if this utility has been initialized by calling Premium.Controller.init(), and throw an exception if it has not been initialized yet.
         * @throws IllegalStateException if the utility has not been initialized yet.
         * */
        private fun checkInitialization() {
            if (!initialized) {
                throw IllegalStateException("It is necessary to call Premium.Controller.init() before calling any other function of Premium.Controller")
            }
        }

        /**
         * Utility to be called before trigger any function of the PremiumListener, used to log the state of the listener in the logcat.
         * @param functionName In text, name of the function of the PremiumListener to be triggered after calling this function.
         * */
        private fun logPremiumListenerTriggered(functionName: String) {
            if (premiumListener != null) {
                log("Triggered > PremiumListener.$functionName")
            } else {
                logw("Cannot be trigger PremiumListener.$functionName > The premiumListener is null")
            }
        }

        /**
         * Initialize the connection with the billing client. Call this when need connecting to the billing client or before
         * performing any billing-related task to ensure it is connected.
         * @param context It is used to recreate the billing client instance if the connection has already been closed,
         *        set it as null if it is certain that the connection has not been closed.
         * @param result Asynchronous function callback to report the connection result, with the result code based on [BillingResponseCode]
         * */
        private fun connectBillingClient(context: Context?, result: (resultCode: Int) -> Unit) {

            log("Invoked > connectBillingClient()")

            /*
            * BillingClient.ConnectionState
            * DISCONNECTED // Initial state, after calling billing client build(); billingClient.isReady = false
            * CONNECTING // Currently connecting when calling billingClient.startConnection; billingClient.isReady = false
            * CONNECTED // Successfully connected; billingClient.isReady = true
            * CLOSED // After calling endConnection(); billingClient.isReady = false; Once closed, it's not possible to connect again, the instance must be recreated
            * */

            if (billingClient.isReady) {
                // If billingClient.isReady then billingClient.connectionState == ConnectionState.CONNECTED
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

            if (billingClient.connectionState == ConnectionState.CONNECTING) {
                logw("The billing client is already in the process of connecting. Call connectBillingClient() later")
                // ERROR is returned as BillingResponseCode does not have an appropriate code for this situation, which ideally should not occur
                return result(BillingResponseCode.ERROR)
            }

            log("Connecting the billing client...")
            // billingClient.connectionState == ConnectionState.DISCONNECTED Proceeding to initiate the connection with the billing client
            billingClient.startConnection(object : BillingClientStateListener {

                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    val info = BillingUtils.getBillingResponseCodeInfo(billingResult.responseCode)
                    log("Billing client connection setup finished. Result: ${info.shortDesc} | Message: ${billingResult.debugMessage}")
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
         * In certain cases, even when calling this function, the connection is avoided from being closed if there are critical
         * pending processes with the billing client.
         * */
        private fun endBillingClientConnection() {

            log("Invoked > endBillingClientConnection()")

            if (currentPremiumState == PremiumState.PENDING_TRANSACTION) {
                log("currentPremiumState is PENDING_TRANSACTION, the billing client connection closure is skipped to await purchase updates")
                return
            }

            if (acknowledgePurchaseInProgress) {
                log("Acknowledge purchase in progress, the billing client connection closure is skipped to wait for the process to finish")
                return
            }

            applicationContext = null // It's no longer needed

            if (!billingClient.isReady) {
                log("No need to close the billing client connection. Billing Client is not ready or the connection has already been closed")
                return
            }

            billingClient.endConnection()

            log("The connection with the billing client was closed")

        }

        /**
         * Retrieve the details of one or more In-app products from the billing client.
         * @param context Context from which the process starts.
         * @param productIds List containing the product IDs to be queried.
         * @param result Asynchronous function callback to receive the result, with the result code based on [BillingResponseCode]
         *        and a list with the details of the products. The list only contains elements if the response code is
         *        BillingResponseCode.OK; otherwise, it will be null. The [productDetailsList] will always contain at least one
         *        element or be null, but it will never be an empty list, his ensures that if it's not null, the response code
         *        was BillingResponseCode.OK, and the list contains at least one element.
         * */
        private fun getProductsDetails(context: Context, productIds: List<String>, result: (resultCode: Int, productDetailsList: List<ProductDetails>?) -> Unit) {

            log("Invoked > getProductsDetails() [Private]")

            connectBillingClient(context) { resultCode ->
                if (resultCode == BillingResponseCode.OK) {

                    // The list of products to be queried is generated
                    val productList: MutableList<Product> = mutableListOf()
                    productIds.forEach { productId ->
                        productList.add(Product.newBuilder().setProductType(ProductType.INAPP).setProductId(productId).build())
                    }

                    // Generate the product details params and execute the query
                    val queryProductDetailsParams = QueryProductDetailsParams.newBuilder().setProductList(productList).build()
                    billingClient.queryProductDetailsAsync(queryProductDetailsParams) { billingResult, productDetailsList ->

                        // NOTE: The result can be BillingResponseCode.OK, but the list might be empty, in case any of the queried product IDs does not exist

                        if (billingResult.responseCode == BillingResponseCode.OK) {
                            if (productDetailsList.isNotEmpty()) {
                                log("The list of products details was successfully retrieved. List (size: ${productDetailsList.size}): $productDetailsList")
                                result(billingResult.responseCode, productDetailsList)
                            } else {
                                logw("The list of products details was successfully retrieved, but it is empty")
                                logw("** Verify that the product IDs are correct and try again **")
                                // In this case, the response ERROR is simulated as the product(s) with the provided IDs are not available
                                result(BillingResponseCode.ERROR, null)
                            }
                        } else {
                            val info = BillingUtils.getBillingResponseCodeInfo(billingResult.responseCode)
                            logw("The products details could not be obtained. Result: ${info.shortDesc} | Message: ${billingResult.debugMessage}")
                            result(billingResult.responseCode, null)
                        }

                    }

                } else {
                    logw(
                        "Product details could not be retrieved as a connection to the billing client could not be established. " +
                                "Connection result: ${BillingUtils.getBillingResponseCodeInfo(resultCode).shortDesc}"
                    )
                    result(resultCode, null)
                }
            }

        }

        /**
         * It is responsible for managing and validating the purchases and assign the new premium state according to the
         * performed validations.
         * @param context Context from which the process starts. It accepts null because applicationContext also accepts it,
         *        but ideally, it shouldn't be null to process everything correctly.
         * @param purchases Purchase list, to determine the new premium state.
         * @return The new premium state according to the performed validations.
         * */
        private fun handlePurchase(context: Context?, purchases: List<Purchase>?): PremiumState {

            log("Invoked > handlePurchase()")
            log("handlePurchase() > context = $context")
            log("handlePurchase() > purchases = $purchases")

            var premiumResult = PremiumState.NOT_PREMIUM // Default result, updates based on validations

            if (!purchases.isNullOrEmpty()) {
                log("Starting purchases iteration, purchases size: ${purchases.size}")
                purchases.forEach { purchase ->

                    val purchaseStateDesc = when (purchase.purchaseState) {
                        PurchaseState.PURCHASED -> "PURCHASED (${purchase.purchaseState})"
                        PurchaseState.PENDING -> "PENDING (${purchase.purchaseState})"
                        else -> "UNSPECIFIED_STATE (${purchase.purchaseState})"
                    }

                    log("Purchase Information")
                    log("Order Id: ${purchase.orderId} | Product Ids ${purchase.products}")
                    log("Purchase state: $purchaseStateDesc | Acknowledge: ${purchase.isAcknowledged}")

                    // Products IDs of the purchase, ideally there should always be only 1 since only one product is purchased at a time in startProductPurchase()
                    val purchaseProducts = purchase.products
                    // It is checked whether purchaseProducts contains at least one ID that matches those present in premiumAccessProductIds
                    val commonIds = premiumAccessProductIds.intersect(purchaseProducts.toSet())

                    // If commonIds isNotEmpty, it indicates at least one match
                    if (commonIds.isNotEmpty()) {

                        /*
                        * It is validated that premiumResult is different from PREMIUM, and only if it's different, the reassignment is performed.
                        * This is done because if in any previous iteration it was already determined that the user has purchased a product and
                        * the result has been assigned as PREMIUM, there is no need to change the result since the user should already have access
                        * to premium features having purchased any of the products from the premiumAccessProductIds list.
                        * */
                        if (premiumResult != PremiumState.PREMIUM) {
                            // The appropriate value is assigned.
                            when (purchase.purchaseState) {
                                PurchaseState.PURCHASED -> premiumResult = PremiumState.PREMIUM
                                PurchaseState.PENDING -> premiumResult = PremiumState.PENDING_TRANSACTION
                                PurchaseState.UNSPECIFIED_STATE -> premiumResult = PremiumState.NOT_PREMIUM
                            }
                            log("A new premium state is assigned according to the purchase. New premium state: $premiumResult")
                        } else {
                            log("There's no need to assign a new premium state based on this purchase, as the state is already PREMIUM")
                        }

                        /*
                        * It is verified if the purchase has already been acknowledged. If it is not already acknowledged, it acknowledges it
                        * This process is independent and it's performed on another thread.
                        * */
                        acknowledgePurchase(context, purchase)

                    } else {
                        logw(
                            "Premium Access Product Ids list $premiumAccessProductIds does not contains " +
                                    "any purchase.products $purchaseProducts | No need to handle the purchase"
                        )
                    }

                }
            } else {
                premiumResult = PremiumState.NOT_PREMIUM
                log("The purchases list is null or empty. There is nothing to process")
            }

            currentPremiumState = premiumResult // Update the current premium state

            // The state is updated in the preferences
            context.whenNotNull {
                PremiumPreferences.savePremiumState(it, premiumResult)
            }

            log("handlePurchase() > Final Premium Result = $premiumResult")
            return premiumResult

        }

        /**
         * Verifies if the [purchase] has already been acknowledged, and if not, it is acknowledged.
         * This process is independent, runs in the background and does not affect any other process.
         * The acknowledgment of the purchase is mandatory, because if it is not done after a while, Google will refund the purchase.
         * @param context Context from which the process starts. It accepts null because applicationContext also accepts it,
         *        but ideally, it shouldn't be null to process everything correctly.
         * @param purchase Purchase to validate and acknowledge (if applicable).
         * */
        private fun acknowledgePurchase(context: Context?, purchase: Purchase) {

            log("Invoked > acknowledgePurchase()")
            log("acknowledgePurchase() > context = $context")
            log("acknowledgePurchase() > purchase = $purchase")

            when {
                purchase.isAcknowledged -> {
                    log("Purchase is already acknowledged")
                    return
                }

                purchase.purchaseState == PurchaseState.UNSPECIFIED_STATE -> {
                    log("No need to acknowledge the purchase, because the purchase state is UNSPECIFIED_STATE (${PurchaseState.UNSPECIFIED_STATE})")
                    return
                }

                purchase.purchaseState == PurchaseState.PENDING -> {
                    log("No need to acknowledge the purchase, because the purchase state is PENDING (${PurchaseState.PENDING})")
                    return
                }
            }

            // If the purchase couldn't be successfully acknowledged, retry the process after 2 minutes
            val retryAcknowledgePurchase = fun() {
                log("The purchase will be retried for acknowledgment again in 2 minutes")
                Handler(Looper.getMainLooper()).postDelayed({
                    acknowledgePurchase(context, purchase)
                }, 120000)
            }

            log("The purchase is not yet acknowledged and the state is PURCHASED (${PurchaseState.PURCHASED}), started the process to acknowledge it")
            acknowledgePurchaseInProgress = true

            connectBillingClient(context) { resultCode ->

                if (resultCode == BillingResponseCode.OK) {

                    // The process to acknowledge the purchase is initiated
                    val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build()
                    billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->

                        val info = BillingUtils.getBillingResponseCodeInfo(billingResult.responseCode)
                        log("Acknowledge purchase result: ${info.shortDesc} | Message: ${billingResult.debugMessage}")

                        if (billingResult.responseCode == BillingResponseCode.OK) {
                            log("The purchase has been acknowledged successfully")
                            acknowledgePurchaseInProgress = false // It is set to false until the purchase is successfully acknowledged
                        } else {

                            // The error is logged in Firebase Crashlytics if it is enabled
                            firebaseCrashlyticsInstance?.log(info.shortDesc)
                            firebaseCrashlyticsInstance?.log(billingResult.debugMessage)
                            firebaseCrashlyticsInstance?.recordException(Exception("Error acknowledging the purchase"))

                            loge("Failed to acknowledge the purchase")
                            retryAcknowledgePurchase()
                        }
                    }

                } else {
                    // The error is not logged in Firebase Crashlytics since it's a connection error
                    logw(
                        "The purchase couldn't be acknowledged because a connection with the billing client could not be established. " +
                                "Connection result: ${BillingUtils.getBillingResponseCodeInfo(resultCode).shortDesc}"
                    )
                    retryAcknowledgePurchase()
                }

            }

            /*
            * NOTES
            * - The billing client connection is not closed upon completing the purchase acknowledgment process (regardless of whether it was successful or
            *   not). This is because this task is independent and to avoid disrupting the execution flow. It's possible that in some cases, the connection
            *   might remain open and applicationContext might stay assigned, but ideally, this will happen only once (during purchase acknowledgment), so
            *   it shouldn't be a problem.
            * */

        }

        /**
         * Handles PurchasesUpdatedListener.onPurchasesUpdated()
         * @param billingResult Result of the purchase update.
         * @param purchases List of updated purchases if present.
         * */
        private fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {

            log("Invoked > onPurchasesUpdated()")

            val info = BillingUtils.getBillingResponseCodeInfo(billingResult.responseCode)
            log("onPurchasesUpdated() | Result: ${info.shortDesc} | Message: ${billingResult.debugMessage} | Purchases: $purchases")

            val premiumResult: PremiumState // To assign the result

            when (billingResult.responseCode) {

                // Satisfactory result, the purchase needs to be handled to obtain the state
                BillingResponseCode.OK -> {
                    log("BillingResponseCode.OK > The purchase is being to handled and processed")
                    premiumResult = handlePurchase(applicationContext, purchases)
                }

                // The user already had the product, and purchases are null, so the data is updated to indicate that the user is already premium
                BillingResponseCode.ITEM_ALREADY_OWNED -> {
                    log(
                        "BillingResponseCode.ITEM_ALREADY_OWNED > The user had already purchased the product, so it is assumed that they " +
                                "should already be a premium user"
                    )
                    premiumResult = PremiumState.PREMIUM
                }

                // The user canceled the purchase process, an error occurred or the payment method was rejected. The purchase was not completed
                else -> {
                    log("The purchase has not been completed")
                    premiumResult = PremiumState.NOT_PREMIUM
                }

            }

            // If the result is BillingResponseCode.OK, these actions are performed in handlePurchase()
            if (billingResult.responseCode != BillingResponseCode.OK) {
                currentPremiumState = premiumResult // Update the current premium state

                // The state is updated in the preferences
                applicationContext.whenNotNull {
                    PremiumPreferences.savePremiumState(it, premiumResult)
                }
            }

            // The function is called, but within it, closing the connection is avoided if there are pending tasks with the billing client
            endBillingClientConnection()

            log("Result (Premium State) = $premiumResult")

            logPremiumListenerTriggered("onPurchaseResult()")
            premiumListener?.onPurchaseResult(premiumResult)

        }

    }

}
