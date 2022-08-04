package agstack.gramophone.utils


import android.content.Context


object Constants {
    val Product_Base_Name="PRODUCT BASE NAME"
    val PRODUCT_RATING_DATA_KEY="PRODUCT RATING DATA"
    val RATING_SELECTED="RATING_SELECTED"

    val OFFERSDATA="OFFERSDATA"

    //sortBy options
    val TOP = "top"
    val RECENT = "recent"
    val TOP_REVIEWS ="Top Reviews"
    val RECENT_REVIEWS= "Recent Reviews"
    const val PAST = "past"
    const val HELP_PHONE_NUMBER: String = "customer_support_no"
    const val LANG: String = "lang"
    const val PAGE_URL: String = "url"
    const val PAGE_TITLE: String = "title"


    val NORMAL: Int = 0
    val ALL: Int = 1

    /**
     * For MyNotificationOpenedHandler
     */
    const val INTENT_EXTRA_TARGET_KEY = "target"
    const val INTENT_EXTRA_TARGET_VAL_NOTIFICATIONS = "notifications"

    /**
     * Keys
     */
    val PHONE: String = "phone"
    val LANGUAGE: String = "language"
    const val HindiISOCode = "hi"
    const val MarathiISOCode = "mr"
    const val EnglishISOCode = "en"
    const val DefaultLangISOCode = "hi"
    const val Hindi = "Hindi"
    const val English = "English"

    //OnBoarding const
    const val MOBILE_NO = "mobileNo"
    const val ReferralCode = "referral_code"
    const val Otp = "otp"
    const val GP_API_STATUS = "GP_1"
    const val AUTHORIZATION = "Authorization"
    const val STATE_NAME = "state_name"
    const val OTP_REFERENCE = "otp_ref"
    const val DISTRICT = "district"
    const val TEHSIL = "tehsil"
    const val VILLAGE = "village"
    const val PINCODE = "pincode"
    const val SMS = "sms"


    //MyFarm
    const val Unit = "unit"
    const val STATE = "state"
    const val STATE_IMAGE_URL = "image"
    const val STATE_LIST = "state_list"


    //Community
    const val Content = "content"
    const val price = "price"

    //Product
    const val PRODUCTREVIEWDATA = "PRODUCTREVIEWDATA"
    const val PRODUCTID = "PRODUCTID"

    //Product
    const val ORDER_ID = "order_id"
    const val BOTTOM_SHEET = "bottom_sheet"
    /**
     * For Broadcast
     */
    //const val BROADCAST_ACTION_NOTIFICATIONS = BuildConfig.APPLICATION_ID + ".notifications"

    fun Context.dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    fun Context.pxToDp(px: Int): Int {
        return (px / resources.displayMetrics.density).toInt()
    }

    interface RemoteConfigKeys {
        companion object {
            const val GOOGLE_API_KEY = "google_api_key"
        }
    }
}
