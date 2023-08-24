package com.jeovanimartinez.androidutils.billing.premium

import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.ProductDetails

/**
 * Listener for events relating to app premium billing.
 * */
interface PremiumListener {

    /**
     * Reports if the user has premium rights to the app. It's invoked after calling [Premium.Controller.checkPremiumState].
     * The reported [premiumState] is the one obtained directly from the Google Play billing client, and in the event that
     * it is not possible to obtain that value, the stored  value of the Shared Preferences is reported, which is the latest
     * known value of PremiumState.
     * @param premiumState The current premium state.
     * @param informedByBillingClient If it's true, it indicates that the [premiumState] has been obtained and informed
     *        directly via the Google Play Billing Client. If it's false, it indicates that the result has been obtained
     *        from the Shared Preferences.
     * */
    fun onCheckPremiumState(premiumState: PremiumState, informedByBillingClient: Boolean)

    /**
     * Reports the details of the products (title, price, description, etc.). It's invoked after requesting products details
     * with [Premium.Controller.getProductsDetails].
     * @param resultCode The result code based on [BillingResponseCode].
     * @param productDetailsList list with the details of the products. The list only contains elements if the response code
     *        is BillingResponseCode.OK; otherwise, it will be null. The [productDetailsList] will always contain at least one
     *        element or be null, but it will never be an empty list, his ensures that if it's not null, the response code was
     *        BillingResponseCode.OK, and the list contains at least one element.
     * */
    fun onProductsDetails(resultCode: Int, productDetailsList: List<ProductDetails>?)

    /**
     * Reports the result of initiating the purchase flow. It's invoked after launch billing flow
     * with [Premium.Controller.startProductPurchase].
     * @param resultCode Result code based on [BillingResponseCode]. If the response code is [BillingResponseCode.OK],
     *        it indicates that the flow was launched and displayed correctly to the user; any other code indicates
     *        that the flow was not displayed to the user.
     * */
    fun onStartPurchaseResult(resultCode: Int)

    /**
     * Reports the result of the purchase, and when the premium state changes.
     *
     * Can be invoked in the following cases:
     * - When launching the billing flow with [Premium.Controller.startProductPurchase], this function is invoked
     *   at the end of the flow to report the [result].
     * - When the state of a pending transaction changes, it is invoked to report the [result].
     *
     * @param result Final result based on [PremiumState] indicating whether the user has premium rights in the app.
     * */
    fun onPurchaseResult(result: PremiumState)

}
