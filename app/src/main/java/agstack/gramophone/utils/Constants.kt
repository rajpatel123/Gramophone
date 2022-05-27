package agstack.gramophone.utils

import agstack.gramophone.BuildConfig
import android.content.Context

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
     * For Broadcast
     */
    const val BROADCAST_ACTION_NOTIFICATIONS = BuildConfig.APPLICATION_ID + ".notifications"

    fun Context.dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    fun Context.pxToDp(px: Int): Int {
        return (px / resources.displayMetrics.density).toInt()
    }
}
