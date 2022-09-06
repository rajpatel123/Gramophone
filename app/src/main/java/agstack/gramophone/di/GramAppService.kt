package agstack.gramophone.di

import agstack.gramophone.data.model.SuccessStatusResponse
import agstack.gramophone.data.model.UpdateLanguageRequestModel
import agstack.gramophone.data.model.UpdateLanguageResponseModel
import agstack.gramophone.ui.address.model.AddressRequestModel
import agstack.gramophone.ui.address.model.AddressResponseModel
import agstack.gramophone.ui.address.model.StateResponseModel
import agstack.gramophone.ui.address.model.UpdateAddressRequestModel
import agstack.gramophone.ui.address.model.addressdetails.AddressDataByLatLongResponseModel
import agstack.gramophone.ui.address.model.addressdetails.AddressRequestWithLatLongModel
import agstack.gramophone.ui.cart.model.AddToCartRequest
import agstack.gramophone.ui.cart.model.CartDataResponse
import agstack.gramophone.ui.home.subcategory.model.SubCategoryResponse
import agstack.gramophone.ui.home.view.fragments.market.model.*
import agstack.gramophone.ui.language.model.InitiateAppDataRequestModel
import agstack.gramophone.ui.language.model.InitiateAppDataResponseModel
import agstack.gramophone.ui.language.model.languagelist.LanguageListResponse
import agstack.gramophone.ui.login.model.SendOtpRequestModel
import agstack.gramophone.ui.login.model.SendOtpResponseModel
import agstack.gramophone.ui.order.model.OrderListResponse
import agstack.gramophone.ui.order.model.PlaceOrderResponse
import agstack.gramophone.ui.orderdetails.model.OrderDetailRequest
import agstack.gramophone.ui.orderdetails.model.OrderDetailResponse
import agstack.gramophone.ui.profile.model.LogoutResponseModel
import agstack.gramophone.ui.profile.model.ProfileResponse
import agstack.gramophone.ui.profileselection.model.UpdateProfileTypeRes
import agstack.gramophone.ui.settings.model.WhatsAppOptInResponseModel
import agstack.gramophone.ui.settings.model.blockedusers.BlockedUsersListResponseModel
import agstack.gramophone.ui.verifyotp.model.ValidateOtpRequestModel
import agstack.gramophone.ui.verifyotp.model.ValidateOtpResponseModel
import agstack.gramophone.utils.Constants
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*


interface GramAppService {

    @POST("traders/update-profile-type")
    suspend fun updateProfileType(@Body hashMap: HashMap<Any, Any>): Response<UpdateProfileTypeRes>

    @GET("api/v5/onboarding/get-language")
    @JvmSuppressWildcards
    suspend fun getLanguage(): Response<LanguageListResponse>

    @POST("api/v5/general/address-list/{type}")
    @JvmSuppressWildcards
    suspend fun getAddressData(
        @Path("type") type: String,
        @Body addressRequestModel: AddressRequestModel
    ): Response<StateResponseModel>


    @POST("api/v5/general/address-list/{type}")
    @JvmSuppressWildcards
    suspend fun getDistrict(
        @Path("type") type: String,
        @Body addressRequestModel: AddressRequestModel
    ): Response<AddressResponseModel>

    @PUT("api/v5/general/language-update")
    @JvmSuppressWildcards
    suspend fun updateLanguage(@Body updateLanguageRequestModel: UpdateLanguageRequestModel): Response<UpdateLanguageResponseModel>

    @PUT("api/v5/onboarding/update-language")
    @JvmSuppressWildcards
    suspend fun updateLanguageWhileOnBoarding(@Body updateLanguageRequestModel: UpdateLanguageRequestModel): Response<UpdateLanguageResponseModel>


    @PUT("api/v5/general/address-update")
    @JvmSuppressWildcards
    suspend fun updateAddress(@Body updateAddressRequestModel: UpdateAddressRequestModel): Response<SendOtpResponseModel>

    @POST("api/v5/onboarding/app-initiate")
    @JvmSuppressWildcards
    suspend fun getInitialData(@Body initiateAppDataRequestModel: InitiateAppDataRequestModel): Response<InitiateAppDataResponseModel>

    @POST("api/v5/onboarding/send-otp")
    @JvmSuppressWildcards
    suspend fun sendOTP(@Body sendOtpRequestModel: SendOtpRequestModel): Response<SendOtpResponseModel>


    @PUT("api/v5/onboarding/resend-otp")
    @JvmSuppressWildcards
    suspend fun resendOTP(@Body sendOtpRequestModel: SendOtpRequestModel): Response<SendOtpResponseModel>


    @POST("api/v5/onboarding/validate-otp")
    @JvmSuppressWildcards
    suspend fun validateOTP(@Body validateOtpRequestModel: ValidateOtpRequestModel): Response<ValidateOtpResponseModel>


    @POST("api/v5/product/get-product-detail")
    suspend fun getProductData(@Body productData: ProductData): Response<ProductDataResponse>

    @POST("api/v5/review/get-review/")
    suspend fun getReviewData(
        @Query("sort_by") sortBy: String? = Constants.TOP,
        @Query("page") page: String? = "1",
        @Body productData: ProductData
    ): Response<ProductReviewDataResponse>


    @PUT("api/v5/review/update-review")
    suspend fun updateReviewData(
        @Body productData: ProductData
    ): Response<ProductReviewDataResponse>


    @POST("api/v5/review/add-review")
    suspend fun addReviewData(
        @Body productData: ProductData
    ): Response<ProductReviewDataResponse>



    @POST("api/v5/product/get-related-product")
    suspend fun getRelatedProductsData(@Body productData: ProductData): Response<RelatedProductResponseData>


    @POST("api/v5/product/get-promotion")
    suspend fun getOffersOnProductData(@Body productData: ProductData): Response<OffersProductResponseData>


    @POST("api/v5/cart/add-to-cart")
    suspend fun addToCart(@Body productData: ProductData): Response<CartDataResponse>

    @POST("api/v5/product/get-expert-advice")
    suspend fun getExpertAdvice(@Body productData: ProductData):Response<SuccessStatusResponse>

    @PUT("api/v5/product/update-product-favourite")
    suspend fun updateProductFavorite(@Body productData: ProductData):Response<SuccessStatusResponse>

    @POST("api/v5/cart/add-to-cart")
    suspend fun addToCart(@Body addToCartRequest: AddToCartRequest): Response<SuccessStatusResponse>

    @GET("api/v5/cart/get-cart")
    suspend fun getCartData(): Response<CartDataResponse>

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "api/v5/cart/remove-from-cart", hasBody = true)
    suspend fun removeCartItem(@Field("product_id") productId : Int): Response<SuccessStatusResponse>

    @GET("api/v5/order/get-order/{type}")
    suspend fun getOrderData(@Path("type") type: String): Response<OrderListResponse>

    @POST("api/v5/order/get-order-detail")
    suspend fun getOrderDetails(@Body orderDetailRequest : OrderDetailRequest): Response<OrderDetailResponse>

    @POST("api/v5/cart/place-order")
    suspend fun placeOrder(): Response<PlaceOrderResponse>

    @GET("api/v5/setting/user-blocked-list")
    suspend fun getBlockedUsersList(): Response<BlockedUsersListResponseModel>


    @PUT("api/v5/setting/whatsapp/{opt-in}")
    suspend fun optInOutForWhatsappUpdates(@Path("opt-in") type: String): Response<WhatsAppOptInResponseModel>

    @PUT("api/v5/general/logout")
    suspend fun logoutUser(): Response<LogoutResponseModel>

    @PUT("api/v5/setting/user-block/{customer_id}")
    suspend fun unBlockUser(@Path("customer_id") customer_id :  Int): Response<BlockedUsersListResponseModel>


    @POST("api/v5/general/address-fetch")
    suspend fun updateAddressByLatLong(@Body addressRequestModel: AddressRequestWithLatLongModel): Response<AddressDataByLatLongResponseModel>

    @GET("api/v5/category/banner-data")
    suspend fun getBanners(): Response<BannerResponse>

    suspend fun getLocationAddress(@Url url:String): Response<JSONObject>

    @GET("api/v5/category/product-app-category")
    suspend fun getCategories(): Response<CategoryResponse>

    @GET("api/v5/category/companies")
    suspend fun getCompanies(): Response<CompanyResponse>

    @GET("api/v5/category/stores")
    suspend fun getStores(): Response<StoreResponse>

    @GET("api/v5/category/crops")
    suspend fun getCrops(): Response<CropResponse>

    @GET("api/v5/category/product-app-category/{category_id}")
    suspend fun getSubCategory(@Path("category_id") categoryId: String): Response<SubCategoryResponse>

    @GET("api/v5/customer/profile-data")
    suspend fun getProfile(): Response<ProfileResponse>

    @GET("api/v5/category/home-data")
    suspend fun getHomeData(): Response<HomeDataResponse>
}