package agstack.gramophone.utils

import agstack.gramophone.BuildConfig

object Constants {
    /**
     * Server base url without end slash.
     * Moving this to have environment spacific base urls
     */
    //const val SERVER_BASE_URL = "https://gorest.co.in/public"



    /**
     * For MyNotificationOpenedHandler
     */
    const val INTENT_EXTRA_TARGET_KEY = "target"
    const val INTENT_EXTRA_TARGET_VAL_NOTIFICATIONS = "notifications"

    /**
     * Keys
     */
    val PHONE: String="phone"
    val LANGUAGE: String="language"






    /**
     * For Broadcast
     */
    const val BROADCAST_ACTION_NOTIFICATIONS = BuildConfig.APPLICATION_ID + ".notifications"
}
