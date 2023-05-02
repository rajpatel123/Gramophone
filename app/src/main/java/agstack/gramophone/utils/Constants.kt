package agstack.gramophone.utils


import android.Manifest
import android.content.Context


object Constants {

    val PID_FROM_SEARCH: String="product_id_from_search"
    val TARGET_PAGE_FROM_DEEP_LINK: String= "from_deeplink"
    val TARGET_PAGE_TAB: String="tabId"
    val CROP_ADVISORY: String="crop_advisory"
    val UTM_SOURCE: String="utm_source"
    val UTM_URL: String="utm_url"
    val IS_PUSH_SHOWN_TODAY: String = "isPushShownToday"
    val FIRST_OPEN: String="first"
    val CATEGORY_EVENT: String="cat_event"
    val PUSH_ASKED: String ="push_permission"
    val PUSH_ASKED_COUNT: String ="AskedCount"
    val PUSH_ASKED_DATE: String ="AskedDate"
    val AFTER_KEY: String="after_key"
    val REFERRAL_CODE_VALUE: String="referralValue"
    val LATITUDE: String="lat"
    val LONGITUDE: String="longitude"
    val TARGET_PAGE: String="target_page"
    val URI: String="uri"
    val UTM_SOURCE_UPDATED: String="utm_update"
    val CROP_END_DATE: String="end_date"
    val GO_TO_COMMUNITY: String="community_page"
    val CROP_DAYS: String="crop_days"
    val CROP_DURATION: String="crop_duration"
    val CROP_STAGE: String="crop_stage"
    val DESEASE_TYPE: String="diseaseType"
    val DESEASE_NAME: String="diseaseName"
    val DESEASE_DESC: String="diseaseDesc"
    val DESEASE_IMAGE: String="diseaseImage"
    val DESEASE_ID: String="desease_id"
    val STAGE_ID: String="stage_id"
    val FARM_ID: String = "farm_id"
    val FARM_TYPE: String = "farm_type"
    val BLOCKED_STATUS: String="blockStatus"
    val POLL: String = "poll"
    val QUIZ: String = "quiz"
    val PAGE: String = "follower"
    val PAGE_SOURCE: String = "gram"
    val GET_SELECTED_TAGS: Int = 32
    val IsUrlForPlayStore: String = "play.google.com"
    val sharedContent: String = "sharedContent"
    val DISCOUNT = "discount"
    val FREEBIE = "freebie"
    const val SearchUrl = "app://www.gramophone.in/"
    const val SearchUrlPARAMETER = "search/?category=search&text="
    const val ProfileUrlPARAMETER = "profile/?category=profile&uuid="


    val POST_LATEST: String = "Latest"
    val POST_TRENDING: String = "Trending"
    val POST_FOLLOWING: String = "Following"
    val POST_EXPERT: String = "Expert"
    val POST_SELF: String = "My Post"
    val POST_BOOKMARK: String = "Saved Post"
    val POST_ID: String = "postId"
    val AUTHER_ID: String = "auth_id"
    val AUTHER_UUID: String = "auth_uuid"

    val ADDRESSOBJECT: String = "address"
    val REFERRAL_CODE: String = "referralCOde"
    val UNAUTHORIZED: Int = 401
    val GP_API_MESSAGE: String = "gp_api_message"
    val MESSAGE: String = "message"
    val PIN_POST: String = "pinpost"
    val EDIT_POST: String = "edit_post"
    val COPY_POST: String = "copy_post"
    val DELETE_POST: String = "delete_post"
    val BLOCK_USER: String = "block"
    val REPORT_POST: String = "report"
    val DELAY: Long = 3_000
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
    val SHOP_BY_SUB_CATEGORY = "SHOP_BY_SUB_CATEGORY"
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
    val HOME_FARMS = "crops_and_farm"
    val HOME_ARTICLES = "new_articles"
    val HOME_COMMUNITY = "community"
    val HOME_WEATHER = "home_weather"
    val HOME_GRAMOPHONE_TV = "gramophone_tv"



    //notification

    const val PushNotificationDeepLinkKey = "deep_link"
    private const val POST_ID_KEY = "postId"
    private const val AUTHOR_ID_KEY = "authorId"
    private const val ACTION_TYPE_KEY = "actionType"
    private const val TITLE_KEY = "title"
    private const val BODY_KEY = "body"
    private const val ICON_KEY = "icon"
    private const val ACTION_TYPE_NEW_LIKE = "new_like"
    private const val ACTION_TYPE_NEW_COMMENT = "new_comment"
    private const val ACTION_TYPE_NEW_POST = "new_post"
    private const val ACTION_TYPE_DEEP_LINK = "deep_link"
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
    val HOME_FARMS_VIEW_TYPE = 11
    val HOME_ARTICLES_VIEW_TYPE = 12
    val HOME_COMMUNITY_VIEW_TYPE = 13
    val HOME_WEATHER_VIEW_TYPE = 14
    val HOME_GRAMOPHONE_TV_VIEW_TYPE = 15
    val GramCashResponse = "GramCashResponse"
    val ReferralPointBalanceData = "ReferralPointBalanceData"
    val GramCashFAQList = "GramCashFAQList"
    val GramCashReferralRulesList = "GramCashReferralRulesList"


    val CAMERA_PERMISSION = Manifest.permission.CAMERA
    val READ_EXTERNAL_STORAGE = Manifest.permission.CAMERA
    val REQUEST_CAMERA = 1012
    val REQUEST_GALLERY = 1013
    val API_DATA_LIMITS_IN_ONE_TIME = "99"


    val IV_ONE = 10131
    val IV_TWO = 10132
    val IV_THREE = 10133


    //sortBy options
    val TOP = "top"
    val ORDER_TYPE = "order_type"
    val PLACED = "placed"
    val RECENT = "recent"
    val TOP_REVIEWS = "Top Reviews"
    val RECENT_REVIEWS = "Recent Reviews"
    val REFERRAL = "referral"
    val EXPIRE = "expire"
    val GC_Expiring_soon: String? = "gramcash_expiring_soon"
    val SUB_CATEGORY = "Subcategory"
    val BRAND = "Brand"
    val CROP = "Crop"
    val TECHNICAL = "Technical"
    val RELEVANCE = "Relevance"
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
    const val GP_TOKEN: String = "gp_token"
    const val PAGE_URL: String = "url"
    const val PAGE_TITLE: String = "title"
    const val ARTICLES = "/articles"
    const val FEATURED_ARTICLES = "featured_articles"
    const val TRENDING_ARTICLES = "/trending_articles"
    const val SUGGESTED_ARTICLES = "/suggested_articles"
    const val FAVOURITE_ARTICLES = "/favourite-articles"

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
    const val DefaultLangISOCode = "en"
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
    const val ORDER_PRICE = "order_price"
    const val CATEGORY_ID = "category_id"
    const val CATEGORY_NAME = "category_name"
    const val CATEGORY_IMAGE = "category_image"
    const val SUB_CATEGORY_ID = "sub_category_id"
    const val SUB_CATEGORY_NAME = "sub_category_name"
    const val SUB_CATEGORY_IMAGE = "sub_category_image"
    const val STORE_ID = "store_id"
    const val STORE_NAME = "store_name"
    const val STORE_IMAGE = "store_image"
    const val COMPANY_ID = "company_id"
    const val COMPANY_NAME = "company_name"
    const val COMPANY_IMAGE = "company_image"
    const val CROP_ID = "crop_id"
    const val CROP_REF_ID = "crop_ref_id"
    const val CROP_NAME = "crop_name"
    const val CROP_IMAGE = "crop_image"
    const val BOTTOM_SHEET = "bottom_sheet"
    const val LOCATION_ACCESS_DIALOG = "location_access_dialog"
    const val DEVELOPER_KEY =
        "525118102961-uv60hb4hl2kdrnofh39crb9fq47dqjmo.apps.googleusercontent.com"
    const val postShareRequestKey = 7001
    const val ITEM = 0
    const val LOADING = 1

    /*Share keys*/
    const val CategoryKey = "category"
    const val VideoId = "videoId"
    const val VideoName = "videoName"
    const val PlayListId = "playListId"
    const val PlayListName = "playListName"
    const val GramophoneVideo = "gramophoneVideo"
    const val REDIRECTION_SOURCE = "redirection_source"
    const val LANDING_SCREEN = "Landing Screen"
    const val HAMBURGER_HOME = "Burger Drawer / Home"


    /**
     * DeeplinkConstants
     *
     */

    const val DEEP_LINK_CROP_LIST = "cropList"
    const val DEEP_LINK_HOME = "home"
    const val DEEP_LINK_YOUTUBE = "youtubeVideo"
    const val DEEP_LINK_MARKET = "market"
    const val DEEP_LINK_MY_FARM = "myFarm"
    const val DEEP_LINK_MY_ORDERS = "orders"
    const val DEEP_LINK_SOCIAL = "social"
    const val DEEP_LINK_WEATHER_INFO = "weatherInfo"
    const val DEEP_LINK_REFERRAL = "referral"
    const val DEEP_LINK_GRAM_CASH = "gramCashDetail"
    const val DEEP_LINK_PRODUCT_LIST = "productList"
    const val DEEP_LINK_PRODUCT_DETAIL = "productDetail"
    const val DEEP_LINK_CROP_PRODUCT = "cropProduct"
    const val DEEP_LINK_EDIT_LANGUAGE = "editLanguage"
    const val DEEP_LINK_CROP_PROBLEM = "cropProblem"
    const val DEEP_LINK_ADVISORY = "advisory"
    const val DEEP_LINK_EDIT_PHONE_NO = "editPhoneNo"
    const val DEEP_LINK_DISEASE_DETAILS = "diseaseDetails"
    const val DEEP_LINK_ARTICLE_DETAILS = "articles"
    const val DEEP_LINK_ARTICLE_TRENDING = "trendingArticles"
    const val DEEP_LINK_ARTICLE_SUGGESTED = "suggestedArticles"
    const val DEEP_LINK_ARTICLE_CROPS = "cropArticles"
    const val DEEP_LINK_ARTICLE_CATEGORY = "categoryArticles"
    const val DEEP_LINK_SHOP_BY_STORE = "shop_by_store"
    const val DEEP_LINK_SHOP_BY_CATEGORY = "shop_by_category"
    const val DEEP_LINK_CART = "cart"
    const val DEEP_LINK_FAV_ARTICLE = "favoriteArticles"
    const val DEEP_LINK_FAV_POSTS = "favoritePosts"
    const val DEEP_LINK_FAV_PRODUCTS = "favoriteProducts"
    const val DEEP_LINK_FAV_TV = "favoriteTV"
    const val DEEP_LINK_OFFERS = "allOffers"
    const val DEEP_LINK_OFFER = "offer"
    const val DEEP_LINK_NOTIFICATION = "notification"
    const val DEEP_LINK_EDIT_LOCATION = "editLocation"
    const val DEEP_LINK_SHOP_BY_COMPANY= "shopByCompany"
    const val DEEP_LINK_SHOP_BY_COMPANY_NAME= "shopByCompanyName"
    const val DEEP_LINK_MY_FAVORITES= "myFavorites"
    const val DEEP_LINK_POST_DETAIL= "postDetails"

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

    object PostType {
        const val AdvertisedProduct = "FEATURED_PRODUCTS"
        const val NearByProfile = "NEARBY_PROFILES"
        const val Post = "POST"
        const val POLL = "POLL"
        const val QUIZ = "QUIZ"
        const val Loader = "LOADER"
        const val Banner = "Banner"
    }
    object PushNotification {
        const val LARGE_ICON_WIDTH_SIZE = 355 //px
        const val LARGE_ICONE_SIZE = 267 //px
    }
}
