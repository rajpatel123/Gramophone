package agstack.gramophone.utils


import android.Manifest
import android.content.Context


object Constants {
    val REMAINING_TIME: String="remaining_time"
    val RESEND_OTP_TIME: Long=30000
    val CHANGE_LANGUAGE:String = "change_language"
    val CHANGE_STATE:String ="change"
    val BUNDLE: String="bundle"
    val Product_Base_Name="PRODUCT BASE NAME"
    val PRODUCT_RATING_DATA_KEY="PRODUCT RATING DATA"
    val RATING_SELECTED="RATING_SELECTED"
    val OFFERSDATA="OFFERSDATA"
    val SHOP_BY_TYPE="SHOP_BY_TYPE"
    val SHOP_BY_CROP="SHOP_BY_CROP"
    val SHOP_BY_STORE="SHOP_BY_STORE"
    val SHOP_BY_COMPANY="SHOP_BY_COMPANY"


    val CAMERA_PERMISSION = Manifest.permission.CAMERA
    val REQUEST_CAMERA = 1012
    val REQUEST_GALLERY = 1013

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
    const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 10f

    // The minimum time between updates in milliseconds
    const val MIN_TIME_BW_UPDATES = (1000 * 60 * 1 ).toLong()

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
    const val VOICE = "voice"


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
    const val Product_Id_Key = "product_id"

    //Product
    const val ORDER_ID = "order_id"
    const val BOTTOM_SHEET = "bottom_sheet"
    const val LOCATION_ACCESS_DIALOG = "location_access_dialog"
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


    object Profile {
        const val MAX_AVATAR_SIZE = 1280 //px, side of square
        const val MIN_AVATAR_SIZE = 100 //px, side of square
        const val MAX_NAME_LENGTH = 120
        const val FollowerType = "follower"
        const val FollowingType = "following"
    }
}
