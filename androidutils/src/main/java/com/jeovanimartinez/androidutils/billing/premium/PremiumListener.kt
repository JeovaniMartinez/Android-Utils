package com.jeovanimartinez.androidutils.billing.premium

import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.ProductDetails

/**
 * Listener for events relating to app premium billing.
 * */
interface PremiumListener {

    /**
     * Informs if the user has premium rights to the app. It's invoked after calling **PENDING TO UPDATE**.
     * The reported [premiumState] is the one obtained directly from the Google Play billing client, and in the event that
     * it is not possible to obtain that value, the value of the preferences is reported.
     * @param premiumState The current premium state.
     * */
    fun onCheckPremium(premiumState: PremiumState)

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
     * @param result Final result based on [PremiumState] indicating whether the user has premium privileges in the app.
     * */
    fun onPurchaseResult(result: PremiumState)

}
