package com.jeovanimartinez.androidutils.billing.premium

/**
 * Define the possible states when checking if a user has premium privileges in the app.
 * */
enum class PremiumState {

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
