package agstack.gramophone.utils


import android.Manifest
import android.content.Context


object Constants {

    val ADDRESSOBJECT: String = "address"
    val REFERRAL_CODE: String="referralCOde"
    val UNAUTHORIZED: Int=401
    val GP_API_MESSAGE: String="gp_api_message"
    val PIN_POST: String="pinpost"
    val EDIT_POST: String="edit_post"
    val COPY_POST: String="copy_post"
    val DELETE_POST: String="delete_post"
    val BLOCK_USER: String="block"
    val REPORT_POST: String="report"
    val DELAY: Long=3_000
    val INDEX: String = "pageIndex"
    val FROM_EDIT_PROFILE = "FROM_EDIT_PROFILE"
    val REMAINING_TIME: String = "remaining_time"
    val RESEND_OTP_TIME: Long = 30000
    val CHANGE_LANGUAGE: String = "change_language"
    val CHANGE_STATE: String = "change"
    val BUNDLE: String = "bundle"
    val Product_Base_Name = "PRODUCT BASE NAME"
    val PRODUCT_RATING_DATA_KEY = "PRODUCT RATING DATA"
    val RATING_SELECTED = "RATING_SELECTED"
    val OFFERSDATA = "OFFERSDATA"
    val OFFERSDATA_OFFERSLIST = "OFFERSDATA_OFFERSLIST"
    val SHOP_BY_TYPE = "SHOP_BY_TYPE"
    val SHOP_BY_CROP = "SHOP_BY_CROP"
    val SHOP_BY_STORE = "SHOP_BY_STORE"
    val SHOP_BY_COMPANY = "SHOP_BY_COMPANY"
    val HOME_BANNER_1 = "home_banner_1"
    val HOME_SHOP_BY_CATEGORY = "shop_by_category"
    val HOME_FEATURED_PRODUCTS = "featured_products"
    val HOME_SHOP_BY_CROP = "shop_by_crop"
    val HOME_SHOP_BY_STORE = "shop_by_store"
    val HOME_SHOP_BY_COMPANY = "shop_by_company"
    val HOME_BANNER_EXCLUSIVE = "gramophone_exclusive"
    val HOME_BANNER_REFERRAL = "home_referral_banner"
    val HOME_GRAMOPHONE_PROMISE = "gramophone_promise"
    val HOME_CART = "products_in_your_cart"

    val HOME_EMPTY_VIEW_TYPE = 0
    val HOME_BANNER_VIEW_TYPE = 1
    val HOME_BANNER_EXCLUSIVE_VIEW_TYPE = 2
    val HOME_BANNER_REFERRAL_VIEW_TYPE = 3
    val HOME_SHOP_BY_CATEGORY_VIEW_TYPE = 4
    val HOME_FEATURED_PRODUCTS_VIEW_TYPE = 5
    val HOME_SHOP_BY_CROP_VIEW_TYPE = 6
    val HOME_SHOP_BY_STORE_VIEW_TYPE = 7
    val HOME_SHOP_BY_COMPANY_VIEW_TYPE = 8
    val HOME_GRAMOPHONE_PROMISE_VIEW_TYPE = 9
    val HOME_CART_VIEW_TYPE = 10
    val GramCashResponse = "GramCashResponse"
    val ReferralPointBalanceData ="ReferralPointBalanceData"
    val GramCashFAQList = "GramCashFAQList"
    val GramCashReferralRulesList = "GramCashReferralRulesList"


    val CAMERA_PERMISSION = Manifest.permission.CAMERA
    val READ_EXTERNAL_STORAGE = Manifest.permission.CAMERA
    val REQUEST_CAMERA = 1012
    val REQUEST_GALLERY = 1013

    //sortBy options
    val TOP = "top"
    val RECENT = "recent"
    val TOP_REVIEWS = "Top Reviews"
    val RECENT_REVIEWS = "Recent Reviews"
    val REFERRAL = "referral"
    val EXPIRE = "expire"
    val GC_Expiring_soon: String?="gramcash_expiring_soon"
    val SUB_CATEGORY = "Subcategory"
    val BRAND = "Brand"
    val CROP = "Crop"
    val TECHNICAL = "Technical"
    val RELAVENT = "Relavent"
    val RELAVENT_CODE = "relevant"
    val AVG_CUSTOMER_RATING = "Avg Customer Rating"
    val AVG_CUSTOMER_RATING_CODE = "rating"
    val PRICE_LOW_TO_HIGH = "Price - Low to High"
    val PRICE_LOW_TO_HIGH_CODE = "price_asc"
    val PRICE_HIGH_TO_LOW = "Price - High to Low"
    val PRICE_HIGH_TO_LOW_CODE = "price_desc"
    const val PAST = "past"
    const val HELP_PHONE_NUMBER: String = "customer_support_no"
    const val LANG: String = "lang"
    const val PAGE_URL: String = "url"
    const val PAGE_TITLE: String = "title"

val SHAREIMAGEURIStRING = "imageuriString"

    val NORMAL: Int = 0
    val ALL: Int = 1
    const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 10f

    // The minimum time between updates in milliseconds
    const val MIN_TIME_BW_UPDATES = (1000 * 60 * 1).toLong()

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
    const val ALL_STRING = "all"
    const val STATE = "state"
    const val DISTRICT = "district"
    const val TEHSIL = "tehsil"
    const val VILLAGE = "village"
    const val PINCODE = "pincode"
    const val SMS = "sms"
    const val VOICE = "voice"
    const val USER_PROFILE_DATA = "USERPROFILEDATA"


    //MyFarm
    const val Unit = "unit"
    const val STATE_IMAGE_URL = "image"
    const val STATE_LIST = "state_list"


    //Community
    const val Content = "content"
    const val price = "price"

    //Product
    const val PRODUCTREVIEWDATA = "PRODUCTREVIEWDATA"
    const val PRODUCTID = "PRODUCTID"
    const val Product_Id_Key = "product_id"
    const val EXPERT_ADVICE = "expert-advice"
    const val HELP = "help"
    const val FEEDBACK = "feedback"
    const val CONTACTFORPRICE = "contact-for-price"


    //Product
    const val PRODUCT = "product"
    const val ORDER_ID = "order_id"
    const val CATEGORY_ID = "category_id"
    const val CATEGORY_NAME = "category_name"
    const val CATEGORY_IMAGE = "category_image"
    const val COMPANY_ID = "company_id"
    const val STORE_ID = "store_id"
    const val STORE_NAME = "store_name"
    const val STORE_IMAGE = "store_image"
    const val CROP_ID = "crop_id"
    const val BOTTOM_SHEET = "bottom_sheet"
    const val LOCATION_ACCESS_DIALOG = "location_access_dialog"

    const val postShareRequestKey = 7001

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
