@file:Suppress("unused")

package com.jeovanimartinez.androidutils.billing.premium

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PurchasesUpdatedListener
import com.jeovanimartinez.androidutils.Base
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingClient.ConnectionState
import com.android.billingclient.api.BillingClient.ProductType
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryProductDetailsParams.Product
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
        private var premiumListener: PremiumListener? = null  // Listener to report events

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
         * @param context Context from which the process starts. Using the Application Context is highly recommended.
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
            getProductsDetails(context, productIds) { resultCode, productDetailsList ->

                logPremiumListenerInvocation("onProductDetails()")
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
            /*log("Invoked > startProductPurchase()")
            checkInitialization()

            // The product Id must be on the premiumAccessProductIds list to be able to validate the purchase later
            if (!premiumAccessProductIds.contains(productId)) {
                throw IllegalArgumentException("Cannot start the purchase because the specified product Id '$productId' is not in the premiumAccessProductIds list")
            }

            connectBillingClient(activity) { resultCode ->
                if (resultCode == BillingResponseCode.OK) {

                } else {
                    logw(
                        "Unable to start the purchase flow because the connection to the billing client could not be established. " +
                                "Connection result: ${BillingUtils.getBillingResponseCodeInfo(resultCode).shortDesc}"
                    )
                    if (premiumListener != null) {
                        log("Invoked > PremiumListener.onStartPurchaseError()")
                        premiumListener?.onStartPurchaseError(resultCode)
                    } else {
                        logw("Cannot be invoked PremiumListener.onStartPurchaseError() > The premiumListener is null")
                    }
                }
            }*/

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
         * Utility to be called before invoking any function of the PremiumListener, used to log the state of the listener in the logcat.
         * @param functionName In text, name of the function of the PremiumListener to be invoked after calling this function.
         * */
        private fun logPremiumListenerInvocation(functionName: String) {
            if (premiumListener != null) {
                log("Invoked > PremiumListener.$functionName")
            } else {
                logw("Cannot be invoked PremiumListener.$functionName > The premiumListener is null")
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
         * If [preventEndBillingClientConnection] is true, the connection is not closed even if this function is called.
         * */
        private fun endBillingClientConnection() {

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


        private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->

        }

    }

}
