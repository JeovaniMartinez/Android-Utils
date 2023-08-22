@file:Suppress("unused")

package com.jeovanimartinez.androidutils.billing.premium

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingClient.ConnectionState
import com.android.billingclient.api.BillingClient.ProductType
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryProductDetailsParams.Product
import com.jeovanimartinez.androidutils.Base
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
         * Starts the process to retrieve details of one or more In-app products asynchronously. The result is
         * informed by [PremiumListener.onProductDetails]
         * @param context Context from which the process starts.
         * @param productIds List containing the product IDs to be queried.
         * */
        fun getProductsDetails(context: Context, productIds: List<String>) {
            log("Invoked > getProductsDetails()")
            checkInitialization()

            // A warning is logged if any of the provided product IDs is not in the premiumAccessProductIds list
            productIds.forEach {
                if (!premiumAccessProductIds.contains(it)) {
                    logw("The '$it' Id is not in the Premium.Controller.premiumAccessProductIds list")
                }
            }

            // The process is carried out through a private function, and the result is reported
            getProductsDetails(context.applicationContext, productIds) { resultCode, productDetailsList ->

                logPremiumListenerTriggered("onProductDetails()")
                premiumListener?.onProductDetails(resultCode, productDetailsList)

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
                        if (productDetailsList != null) {

                            // The purchase flow is configured and launched
                            val productDetailsParamsList = listOf(BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(productDetailsList[0]).build())
                            val billingFlowParams = BillingFlowParams.newBuilder().setProductDetailsParamsList(productDetailsParamsList).build()
                            val billingResult = billingClient.launchBillingFlow(activity, billingFlowParams)

                            val info = BillingUtils.getBillingResponseCodeInfo(billingResult.responseCode)
                            log("Billing client launchBillingFlow. Result: ${info.shortDesc} | Message: ${billingResult.debugMessage}")

                            // If the purchase flow could not be launched
                            if (billingResult.responseCode != BillingResponseCode.OK) {
                                logPremiumListenerTriggered("onStartPurchaseError()")
                                premiumListener?.onStartPurchaseError(productDetailsResultCode)
                                endBillingClientConnection()
                                /*
                                * NOTE: In tests conducted, if the product has already been purchased or if there is no internet connection,
                                * the response code remains OK and the error message is displayed in the modal of the opened purchase flow
                                * */
                            } else {
                                // If the purchase flow was launched successfully, the result is reported in the purchasesUpdatedListener
                                applicationContext = activity.applicationContext // To have it available in the listener when the result is reported
                            }

                        } else {
                            logw("Unable to start the purchase flow because the product details could not be obtained")
                            logPremiumListenerTriggered("onStartPurchaseError()")
                            premiumListener?.onStartPurchaseError(productDetailsResultCode)
                            endBillingClientConnection()
                        }
                    }

                } else {
                    logw(
                        "Unable to start the purchase flow because the connection to the billing client could not be established. " +
                                "Connection result: ${BillingUtils.getBillingResponseCodeInfo(connectionResultCode).shortDesc}"
                    )
                    logPremiumListenerTriggered("onStartPurchaseError()")
                    premiumListener?.onStartPurchaseError(connectionResultCode)
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

        private fun handlePurchase(context: Context?, purchases: List<Purchase>?): PremiumState {
            return PremiumState.PENDING_TRANSACTION // Temporary while the function is being programmed
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

                // Satisfactory result, the purchase needs to be handled to obtain the status
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

            currentPremiumState = premiumResult // Update the current premium state

            // The state is updated in the preferences
            applicationContext.whenNotNull {
                PremiumPreferences.savePremiumState(it, premiumResult)
            }

            // The function is called, but within it, closing the connection is avoided if there are pending tasks with the billing client
            endBillingClientConnection()

            log("Result (Premium State) = $premiumResult")

            logPremiumListenerTriggered("onPurchaseResult()")
            premiumListener?.onPurchaseResult(premiumResult)

        }

    }

}
