package com.jeovanimartinez.androidutils.billing.premium

import android.content.Context
import com.jeovanimartinez.androidutils.Base

/**
 * Utility for working with preferences file relating to app premium billing.
 * */
object PremiumPreferences : Base<PremiumPreferences>() {

    override val LOG_TAG = "PremiumPreferences"

    // File name, keys, and values
    private const val FILE_NAME = "aUpPkjA5IcXzlL49AfkB"
    private const val KEY_PREMIUM_STATE = "aUpS55v1xp0eOkEMo6pM"
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
