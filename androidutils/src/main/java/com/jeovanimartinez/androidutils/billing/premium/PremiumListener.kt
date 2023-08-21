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
     * with [Premium.Controller.getProductsDetails]
     * @param resultCode The result code based on [BillingResponseCode]
     * @param productDetailsList list with the details of the products. The list only contains elements if the response code
     *        is BillingResponseCode.OK; otherwise, it will be null. The [productDetailsList] will always contain at least one
     *        element or be null, but it will never be an empty list, his ensures that if it's not null, the response code was
     *        BillingResponseCode.OK, and the list contains at least one element.
     * */
    fun onProductDetails(resultCode: Int, productDetailsList: List<ProductDetails>?)

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
