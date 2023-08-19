package com.jeovanimartinez.androidutils.billing.premium

import android.content.Context
import com.jeovanimartinez.androidutils.Base
import java.util.UUID

/**
 * Utility for working with preferences file relating to app premium billing.
 * */
internal object PremiumPreferences : Base<PremiumPreferences>() {

    override val LOG_TAG = "PremiumPreferences"

    // File name, keys, and values
    private const val FILE_NAME = "029e3ff6-ba8f-410a-b517-b01ffe530fa5"
    private const val KEY_PREMIUM_STATE = "f8c2f92f-7746-4d5a-a26b-82e933272d52"
    private const val VALUE_PREMIUM_STATE_NOT_PREMIUM = "4dafc9f2-43f2-4168-8af5-5804446a5f5a"
    private const val VALUE_PREMIUM_STATE_PENDING_TRANSACTION = "5758e6ae-fe94-4571-a919-c6a55b7514ff"
    private const val VALUE_PREMIUM_STATE_PREMIUM = "2426afc4-2a32-4989-af31-b2c48f105f95"

    /**
     * Save the premium state in the preferences.
     * @param context Context from which the process starts.
     * @param premiumState The premiums state to save in the preferences file.
     * */
    fun savePremiumState(context: Context, premiumState: PremiumState) {

        log("Invoked > savePremiumState()")

        val preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

        // Random keys and values are inserted to enhance security. This process is executed only once.
        if (!preferences.contains(KEY_PREMIUM_STATE)) {

            log("The preference file does not have KEY_PREMIUM_STATE, proceeding to insert random keys and values")

            for (i in 0..49) {
                val uuidKey = UUID.randomUUID().toString()
                val uuidValue = UUID.randomUUID().toString()

                // To ensure it's not repeated, as KEY_PREMIUM_STATE is the key that matters
                if (uuidKey != KEY_PREMIUM_STATE) {
                    preferences.edit().putString(uuidKey, uuidValue).apply()
                }
            }

            log("Random keys and values insertion completed")

        }

        when (premiumState) {
            PremiumState.NOT_PREMIUM -> preferences.edit().putString(KEY_PREMIUM_STATE, VALUE_PREMIUM_STATE_NOT_PREMIUM).apply()
            PremiumState.PENDING_TRANSACTION -> preferences.edit().putString(KEY_PREMIUM_STATE, VALUE_PREMIUM_STATE_PENDING_TRANSACTION).apply()
            PremiumState.PREMIUM -> preferences.edit().putString(KEY_PREMIUM_STATE, VALUE_PREMIUM_STATE_PREMIUM).apply()
        }

        log("Saved premium state in preferences. State = $premiumState")

    }

    /**
     * Retrieves the current premium state from the preferences.
     * @param context Context from which the process starts.
     * @return Current premium state obtained from the preferences.
     * */
    fun getPremiumState(context: Context): PremiumState {

        log("Invoked > getPremiumState()")

        val preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

        val state = when (preferences.getString(KEY_PREMIUM_STATE, VALUE_PREMIUM_STATE_NOT_PREMIUM)) {
            VALUE_PREMIUM_STATE_NOT_PREMIUM -> PremiumState.NOT_PREMIUM
            VALUE_PREMIUM_STATE_PENDING_TRANSACTION -> PremiumState.PENDING_TRANSACTION
            VALUE_PREMIUM_STATE_PREMIUM -> PremiumState.PREMIUM
            else -> PremiumState.NOT_PREMIUM
        }

        log("Premium state was obtained from preferences. State = $state")

        return state

    }

}
