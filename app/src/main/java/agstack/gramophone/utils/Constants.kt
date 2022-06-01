package agstack.gramophone.utils

import agstack.gramophone.BuildConfig

object Constants {

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
    const val HindiISOCode = "hi"
    const val MarathiISOCode = "mr"
    const val EnglishISOCode = "en"
    const val DefaultLangISOCode = "hi"
    const val Hindi = "Hindi"
    const val English = "English"





    /**
     * For Broadcast
     */
    const val BROADCAST_ACTION_NOTIFICATIONS = BuildConfig.APPLICATION_ID + ".notifications"
}
