package agstack.gramophone.utils

import agstack.gramophone.BuildConfig
import android.content.Context

object Constants {

    const val HELP_PHONE_NUMBER: String="customer_support_no"
    const val LANG: String="lang"
    const val PAGE_URL: String="url"
    const val PAGE_TITLE: String="title"

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
    const val PROFILE_TYPE = "type"
    const val FARMER = "farmer"
    const val TRADER = "trader"


    const val Category_Id_key = "category_id"
    const val Crop_Id_key = "crop_id"
    const val Profile_uuid_key = "profile_uuid"
    const val Mandi_Id_key = "mandi_id"
    const val Selected_Crop_key = "crop_ids"

    // public static final String Crop_Name_key = "crop_name";
    const val Stage_Id_key = "stage_id"
    const val Chemical_Id_key = "chemical_id"
    const val Brand_Id_key = "company_id"
    const val Category_Type_key = "type"
    const val Product_Type_key = "product_type"
    const val Product_key = "product"
    const val Crop_key = "crop"
    const val Featured_key = "featured"
    const val Limit_key = "limit"

    const val Question_Id_key = "question_id"
    const val Options_key = "option_ids"
    const val Notification_Id_key = "notification_id"

    const val Product_Id_Key = "product_id"

    const val Latitude_Key = "latitude"
    const val Longitude_Key = "longitude"
    const val Language_Key = "language"
    const val Location_name = "location_name"
    const val Date_Key = "date"
    const val Token_Key = "tokens"
    const val Otp_Retry_Key = "retry_type"
    const val PopUp_Id_Key = "ad_id"

    const val FeedbackContent = "content"
    const val FeedbackMobileNo = "mobile_no"
    const val FeedbackName = "name"
    const val FeedbackComment = "comment"
    const val UserMobileNo = "mobile_number"
    const val Mandis = "mandis"

    //    // Bag Keys
    //    public static final String BagProductId = "product_id";
    //    public static final String BagUpdateProductId = "ProductId";
    //    public static final String BagQuantity = "Quantity";
    //    public static final String BagCartItems = "cart_items";
    //    public static final String BagProductName = "product_name";
    //    public static final String BagProductImage = "product_image";
    //    public static final String BagProductQuantity = "quantity";
    //    public static final String BagProductCurrency = "currency";
    //    public static final String BagProductPrice = "price";
    //    public static final String BagTotalPrice = "total";
    //    public static final String BagTotalUnitsCount = "count";
    //    public static final String BagProductTotalPrice = "total_price";
    //
    //
    //    public static final String ResultsKey = "results";

    //OnBoarding const
    const val MOBILE_NO = "mobileNo"
    const val ReferralCode = "referral_code"
    const val Otp = "otp"
    const val GP_API_STATUS = "GP_1"
    const val AUTHORIZATION = "Authorization"

    //MyFarm
    const val FarmsStatusActive = "active"
    const val FarmsStatusInActive = "inactive"
    const val FarmsStatusFeatured = "featured"
    const val FarmsStatusArchived = "archived"
    const val FarmsStatus = "status"
    const val FarmsIdKey = "farm_id"
    const val Field_name = "field_name"
    const val Area = "area"
    const val Unit = "unit"
    const val UnitId = "unit_id"
    const val Crop_sowing_date = "crop_sowing_date"
    const val Farm_id = "farm_id"
    const val UUID = "uuid"
    const val State = "state"
    const val FarmLat = "farm_lat"
    const val FarmLng = "farm_lng"

    //Community

    //Community
    const val Post_id = "postId"
    const val SearchKey = "search"

    const val SearchKeyWordKey = "keyword"

    //token
    const val OneSignalPlayerIdKey = "one_signal_player_id"
    const val FirebaseFcmTokenKey = "fcm_token"
    const val OneSignalFcmTokenKey = "one_signal_fcm_token"
    const val DeviceNameKey = "device_name"
    const val FirebaseDbUserIdKey = "firebase_db_user_id"
    const val MixPanelDistinctIdKey = "mixpanel_distinct_id"
    const val FirebaseMessagingInstanceIdKey = "firebase_messaging_instance_id"
    const val Content = "content"
    const val AndroidID = "android_id"
    const val DeviceId = "device_id"
    const val Token = "token"
    const val OneSignalId = "one_signal_id"
    const val OsVersion = "os_version"
    const val DeviceModel = "device_model"
    const val AppVersionName = "app_version_name"
    const val AppVersionCode = "app_version_code"
    const val Keyboard = "keyboard"
    const val Path = "path"
    const val default_indore_lat = 22.7196
    const val default_indore_lng = 75.8577
    const val BalanceReferralType = "referral"
    const val BalanceGramCashType = "gramcash"
    const val TransactionExpireType = "expire"

    //Map
    const val TypePolygon = "Polygon"
    const val TypeFeature = "Feature"
    const val FarmPolygon = "farm_polygon"
    const val Polygon = "polygon"

    //Vyapaar
    const val commodityId = "commodity_id"
    const val commodities = "commodities"
    const val quantity = "quantity"
    const val quantity_units = "quantity_units"
    const val price = "price"
    const val price_per_unit = "price_per_unit"
    const val expected_delivery_date = "expected_delivery_date"
    const val description = "description"
    const val offer_valid_till = "offer_valid_till"
    const val offer_valid_from = "offer_valid_from"
    const val points = "points"
    const val images = "images"
    const val Locations = "locations"
    const val Verified = "verified"

    const val FirmName = "firmname"
    const val MandiId = "mandiId"
    const val Latitude = "latitude"
    const val Longitude = "longitude"
    const val Address = "address"


    //vyapar
    const val Following = "following"
    const val Trader_id = "trader_id"
    const val Listing_id = "listing_id"
    const val Trader_uuid = "trader_uuid"
    const val Search = "search"
    const val SearchOn = "search_on"
    const val CropType = "type"
    const val Query = "query"
    const val need_help = "Need help while adding Address"


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
