package agstack.gramophone.di

import agstack.gramophone.data.model.SuccessStatusResponse
import agstack.gramophone.data.model.UpdateLanguageRequestModel
import agstack.gramophone.data.model.UpdateLanguageResponseModel
import agstack.gramophone.ui.address.model.AddressRequestModel
import agstack.gramophone.ui.address.model.AddressResponseModel
import agstack.gramophone.ui.address.model.StateResponseModel
import agstack.gramophone.ui.address.model.UpdateAddressRequestModel
import agstack.gramophone.ui.address.model.addressdetails.AddressRequestWithLatLongModel
import agstack.gramophone.ui.address.model.googleapiresponse.GoogleAddressResponseModel
import agstack.gramophone.ui.advisory.models.advisory.AdvisoryRequestModel
import agstack.gramophone.ui.advisory.models.advisory.AdvisoryResponseModel
import agstack.gramophone.ui.advisory.models.cropproblems.CropProblemRequestModel
import agstack.gramophone.ui.advisory.models.cropproblems.CropProblemResponseModel
import agstack.gramophone.ui.advisory.models.recomondedproducts.RecommendedProductRequestModel
import agstack.gramophone.ui.advisory.models.recomondedproducts.RecommendedProductResponseModel
import agstack.gramophone.ui.articles.SuggestedCropResponse
import agstack.gramophone.ui.cart.model.CartDataResponse
import agstack.gramophone.ui.cart.model.PlaceOrderRequest
import agstack.gramophone.ui.createnewpost.model.MentionRequestModel
import agstack.gramophone.ui.createnewpost.model.MentionTagResponsemodel
import agstack.gramophone.ui.createnewpost.model.problems.ProblemTagsResponseModel
import agstack.gramophone.ui.createnewpost.view.model.hashtags.HasgTagResponseModel
import agstack.gramophone.ui.dialog.filter.FilterRequest
import agstack.gramophone.ui.farm.model.*
import agstack.gramophone.ui.farm.model.addfarm.AddFarmResponseModel
import agstack.gramophone.ui.farm.model.unit.FarmUnitResponse
import agstack.gramophone.ui.favourite.model.FavouriteRequestModel
import agstack.gramophone.ui.favourite.model.FeaturedProductResponseModel
import agstack.gramophone.ui.home.featured.ratingeligibility.RatingEligibilityResponseModel
import agstack.gramophone.ui.home.product.model.CheckPromotionResponseModel
import agstack.gramophone.ui.home.subcategory.model.SubCategoryResponse
import agstack.gramophone.ui.home.view.fragments.community.model.quiz.AnsweredQuizPollRequestModel
import agstack.gramophone.ui.home.view.fragments.community.model.quiz.QuizPollResponseModel
import agstack.gramophone.ui.home.view.fragments.gramophone.model.MyGramophoneResponseModel
import agstack.gramophone.ui.home.view.fragments.market.model.*
import agstack.gramophone.ui.home.view.model.FCMRegistrationModel
import agstack.gramophone.ui.language.model.InitiateAppDataRequestModel
import agstack.gramophone.ui.language.model.InitiateAppDataResponseModel
import agstack.gramophone.ui.language.model.SecretKeysResponseModel
import agstack.gramophone.ui.language.model.languagelist.LanguageListResponse
import agstack.gramophone.ui.login.model.SendOtpRequestModel
import agstack.gramophone.ui.login.model.SendOtpResponseModel
import agstack.gramophone.ui.notification.model.NotificationRequestModel
import agstack.gramophone.ui.notification.model.NotificationresponseModel
import agstack.gramophone.ui.notification.model.cropdetails.CropDetailsResponse
import agstack.gramophone.ui.notification.model.cropproblem.CropProblemResponse
import agstack.gramophone.ui.notification.model.shopbycat.ShopByCatResponseModel
import agstack.gramophone.ui.notification.model.shopbystore.ShopByStoreResponseModel
import agstack.gramophone.ui.order.model.OrderListResponse
import agstack.gramophone.ui.order.model.PageLimitRequest
import agstack.gramophone.ui.order.model.PlaceOrderResponse
import agstack.gramophone.ui.orderdetails.model.OrderDetailRequest
import agstack.gramophone.ui.orderdetails.model.OrderDetailResponse
import agstack.gramophone.ui.orderdetails.model.OrderInvoiceRequest
import agstack.gramophone.ui.orderdetails.model.OrderInvoiceResponse
import agstack.gramophone.ui.profile.model.LogoutResponseModel
import agstack.gramophone.ui.profile.model.ProfileResponse
import agstack.gramophone.ui.profile.model.ValidateOtpMobileRequestModel
import agstack.gramophone.ui.profileselection.model.UpdateProfileTypeRes
import agstack.gramophone.ui.referandearn.model.GramCashResponseModel
import agstack.gramophone.ui.referandearn.transaction.TransactionRequestModel
import agstack.gramophone.ui.referandearn.transaction.model.GramCashTxnResponseModel
import agstack.gramophone.ui.search.model.GlobalSearchRequest
import agstack.gramophone.ui.search.model.GlobalSearchResponse
import agstack.gramophone.ui.search.model.SuggestionsRequest
import agstack.gramophone.ui.search.model.SuggestionsResponse
import agstack.gramophone.ui.settings.model.WhatsAppOptInResponseModel
import agstack.gramophone.ui.settings.model.blockedusers.BlockedUsersListResponseModel
import agstack.gramophone.ui.splash.model.language.CustomerLanguageResponseModel
import agstack.gramophone.ui.tv.model.BookmarkedListResponse
import agstack.gramophone.ui.tv.model.VideoBookMarkedRequest
import agstack.gramophone.ui.userprofile.model.UpdateProfileModel
import agstack.gramophone.ui.userprofile.verifyotp.model.VerifyOTPRequestModel
import agstack.gramophone.ui.verifyotp.model.ValidateOtpRequestModel
import agstack.gramophone.ui.verifyotp.model.ValidateOtpResponseModel
import agstack.gramophone.ui.weather.model.WeatherForecastDetailResponse
import agstack.gramophone.ui.weather.model.WeatherForecastSummaryResponse
import agstack.gramophone.ui.weather.model.WeatherRequest
import agstack.gramophone.ui.weather.model.WeatherResponse
import agstack.gramophone.utils.Constants
import retrofit2.Response
import retrofit2.http.*


interface GramAppService {

    @POST("traders/update-profile-type")
    suspend fun updateProfileType(@Body hashMap: HashMap<Any, Any>): Response<UpdateProfileTypeRes>

    @GET("api/v5/onboarding/get-language")
    @JvmSuppressWildcards
    suspend fun getLanguage(): Response<LanguageListResponse>


   @GET("api/v5/customer/get-customer-language")
    @JvmSuppressWildcards
    suspend fun getCustomerLanguage(): Response<CustomerLanguageResponseModel>

    @POST("api/v5/general/address-list/{type}")
    @JvmSuppressWildcards
    suspend fun getAddressData(
        @Path("type") type: String,
        @Body addressRequestModel: AddressRequestModel,
    ): Response<StateResponseModel>


    @POST("api/v5/general/address-list/{type}")
    @JvmSuppressWildcards
    suspend fun getDistrict(
        @Path("type") type: String,
        @Body addressRequestModel: AddressRequestModel,
    ): Response<AddressResponseModel>

    @PUT("api/v5/general/language-update")
    @JvmSuppressWildcards
    suspend fun updateLanguage(@Body updateLanguageRequestModel: UpdateLanguageRequestModel): Response<UpdateLanguageResponseModel>

    @GET("api/v5/onboarding/apigetSecreteKeys")
    suspend fun getSecretKeys(): Response<SecretKeysResponseModel>

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
        @Body productData: ProductData,
    ): Response<ProductReviewDataResponse>


    @PUT("api/v5/review/update-review")
    suspend fun updateReviewData(
        @Body productData: ProductData,
    ): Response<ProductReviewDataResponse>


    @POST("api/v5/review/add-review")
    suspend fun addReviewData(
        @Body productData: ProductData,
    ): Response<ProductReviewDataResponse>


    @POST("api/v5/product/get-related-product")
    suspend fun getRelatedProductsData(@Body productData: ProductData): Response<RelatedProductResponseData>


    @POST("api/v5/product/get-promotion")
    suspend fun getOffersOnProductData(@Body productData: ProductData): Response<OffersProductResponseData>


    @POST("api/v5/product/check-promotion-applicable")
    suspend fun checkPromotionApplicable(@Body verifyPromotionRequestModel: VerifyPromotionRequestModel): Response<CheckPromotionResponseModel>


    @POST("api/v5/cart/add-to-cart")
    suspend fun addToCart(@Body productData: ProductData): Response<CartDataResponse>

    @POST("api/v5/product/get-expert-advice")
    suspend fun getExpertAdvice(@Body productData: ProductData): Response<SuccessStatusResponse>

    @POST("api/v5/general/help/{type}")
    suspend fun getHelp(
        @Path("type") type: String,
        @Body productData: ProductData,
    ): Response<SuccessStatusResponse>

    @PUT("api/v5/product/update-product-favourite")
    suspend fun updateProductFavorite(@Body productData: ProductData): Response<SuccessStatusResponse>

    @GET("api/v5/cart/get-cart")
    suspend fun getCartData(): Response<CartDataResponse>

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "api/v5/cart/remove-from-cart", hasBody = true)
    suspend fun removeCartItem(@Field("product_id") productId: Int): Response<SuccessStatusResponse>

    @POST("api/v5/order/get-order/{type}")
    suspend fun getOrderData(
        @Path("type") type: String,
        @Body pageLimitRequest: PageLimitRequest,
    ): Response<OrderListResponse>

    @POST("api/v5/order/get-order-detail")
    suspend fun getOrderDetails(@Body orderDetailRequest: OrderDetailRequest): Response<OrderDetailResponse>

    @POST("api/v5/cart/place-order")
    suspend fun placeOrder(@Body placeOrderRequest: PlaceOrderRequest): Response<PlaceOrderResponse>


    @PUT("api/v5/setting/whatsapp/{opt-in}")
    suspend fun optInOutForWhatsappUpdates(@Path("opt-in") type: String): Response<WhatsAppOptInResponseModel>

    @PUT("api/v5/general/logout")
    suspend fun logoutUser(): Response<LogoutResponseModel>

    @PUT("api/v5/setting/user-block/{customer_id}")
    suspend fun unBlockUser(@Path("customer_id") customer_id: Int): Response<BlockedUsersListResponseModel>


    @POST("api/v5/general/address-fetch")
    suspend fun getAddressByLatLong(@Body addressRequestModel: AddressRequestWithLatLongModel): Response<StateResponseModel>

    @GET("api/v5/category/banner-data")
    suspend fun getBanners(): Response<BannerResponse>

    @GET("https://maps.googleapis.com/maps/api/geocode/json")
    suspend fun getLocationAddress(
        @Query("latlng") latlng: String,
        @Query("key") key: String,
    ): Response<GoogleAddressResponseModel>

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


    @PUT("api/v5/customer/profile-data")
    suspend fun updateProfile(@Body updateProfileModel: UpdateProfileModel): Response<SuccessStatusResponse>

    @GET("api/v5/category/home-data")
    suspend fun getHomeData(): Response<HomeDataResponse>


    @POST("api/v5/customer/send-otp-mobile")
    @JvmSuppressWildcards
    suspend fun sendOTPMobile(@Body sendOtpRequestModel: VerifyOTPRequestModel): Response<SendOtpResponseModel>


    @PUT("api/v5/customer/resend-otp-mobile")
    @JvmSuppressWildcards
    suspend fun resendOTPMobile(@Body resendOtpRequestModel: SendOtpRequestModel): Response<SendOtpResponseModel>

    @POST("api/v5/customer/validate-otp-mobile")
    @JvmSuppressWildcards
    suspend fun validateOTPMobile(@Body validateOtpRequestModel: ValidateOtpMobileRequestModel): Response<SuccessStatusResponse>


    @GET("api/v5/gramcash/gramcash")
    @JvmSuppressWildcards
    suspend fun getGramCash(): Response<GramCashResponseModel>


    @POST("api/v5/gramcash/gramcash-txn/{type}")
    @JvmSuppressWildcards
    suspend fun getGramCashTxn(
        @Path("type") type: String,
        @Body requestModel: TransactionRequestModel,
    ): Response<GramCashTxnResponseModel>

    @POST("api/v5/category/product-filter-data")
    suspend fun getAllProducts(
        @Body filterRequest: FilterRequest,
    ): Response<AllProductsResponse>

    @POST("api/v5/category/product-featured-data")
    suspend fun getFeaturedProduct(
        @Body pageLimitRequest: PageLimitRequest,
    ): Response<AllProductsResponse>

    @GET("api/v5/category/stores/{store_id}")
    suspend fun getStoresFilterData(@Path("store_id") storeId: String): Response<SubCategoryResponse>

    @POST("api/v5/weather")
    suspend fun getWeatherDetails(
        @Body weatherRequest: WeatherRequest,
    ): Response<WeatherResponse>

    @POST("api/v5/weather/forecast-detail")
    suspend fun getWeatherForeCastDetailHourly(
        @Body weatherRequest: WeatherRequest,
    ): Response<WeatherForecastDetailResponse>

    @POST("api/v5/weather/forecast-summary")
    suspend fun getWeatherForecastSummaryDayWise(
        @Body weatherRequest: WeatherRequest,
    ): Response<WeatherForecastSummaryResponse>

    @PUT("api/v5/cart/update-to-cart")
    suspend fun updateCartItem(@Body productData: ProductData): Response<SuccessStatusResponse>

    @POST("api/v5/farm/get-farm/{type}")
    suspend fun getFarmsData(
        @Path("type") storeId: String,
        @Body request: FarmRequest,
    ): Response<FarmResponse>

    @POST("api/v5/farm/add-farm")
    suspend fun addFarm(@Body request: AddFarmRequest): Response<AddFarmResponseModel>

    @POST("api/v5/farm/delete-farm")
    suspend fun deleteFarm(@Body request: DeletefarmReqquestModel): Response<AddFarmResponse>

    @GET("api/v5/farm/unit/{type}")
    suspend fun getFarmUnits(@Path("type") type: String): Response<FarmUnitResponse>

    @POST("api/v5/search/mention-autocomplete")
    suspend fun getMentionTags(
        @Body mentionRequestModel: MentionRequestModel,
    ): Response<MentionTagResponsemodel>

    @POST("api/v5/search/hashtags")
    suspend fun getHasTags(
        @Body mentionRequestModel: MentionRequestModel,
    ): Response<HasgTagResponseModel>


    @POST("api/v5/search/mentions-crop-problems")
    suspend fun getProblemTags(
        @Body mentionRequestModel: MentionRequestModel,
    ): Response<ProblemTagsResponseModel>

    @POST("api/v5/search/suggestions")
    suspend fun getSuggestions(@Body body: SuggestionsRequest): Response<SuggestionsResponse>


    @GET("api/v5/customer/my-gramophone")
    suspend fun getMyGramophoneData(): Response<MyGramophoneResponseModel>

    @POST("api/v5/search/global-search")
    suspend fun searchByKeyword(@Body body: GlobalSearchRequest): Response<GlobalSearchResponse>

    @POST("api/v5/search/community-search")
    suspend fun searchByKeywordInCommunity(@Body body: GlobalSearchRequest): Response<GlobalSearchResponse>

    @POST("api/v5/farm/add-harvest-ques")
    suspend fun addHarvestQues(@Body body: AddHarvestRequest): Response<MyGramophoneResponseModel>

    @POST("api/v5/category/product-favourite-data")
    suspend fun getFavouriteProduct(@Body body: FavouriteRequestModel): Response<FeaturedProductResponseModel>

    @GET("api/v5/quiz/get-quiz")
    suspend fun getQuiz(): Response<QuizPollResponseModel>

    @POST("api/v5/quiz/submit-answer")
    suspend fun answerQuiz(@Body answeredQuizPollRequestModel: AnsweredQuizPollRequestModel): Response<AnsweredQuizPollRequestModel>

    @POST("api/v5/customer/gramophone-tv-bookmark")
    suspend fun bookmarkVideo(@Body body: VideoBookMarkedRequest): Response<SuccessStatusResponse>

    @POST("api/v5/farm/crop-advisory-details/{type}")
    suspend fun getCropAdvisoryDetails(
        @Body body: AdvisoryRequestModel,
        @Path("type") type: String,
    ): Response<AdvisoryResponseModel>

    @POST("api/v5/farm/recommended-products")
    suspend fun getRecommendedProducts(@Body body: RecommendedProductRequestModel): Response<RecommendedProductResponseModel>

    @POST("api/v5/farm/crop-problems")
    suspend fun getCropProblems(@Body body: CropProblemRequestModel): Response<CropProblemResponseModel>

    @GET("api/v5/article/suggested-crops")
    suspend fun getSuggestedCrops(): Response<SuggestedCropResponse>

    @POST("api/v5/customer/get-gramophone-tv-bookmark")
    suspend fun getBookmarkedVideoList(): Response<BookmarkedListResponse>

    @POST("api/v5/order/get-order-invoice")
    suspend fun getOrderInvoiceUrl(@Body request: OrderInvoiceRequest): Response<OrderInvoiceResponse>

    @POST("api/v5/notifications/get-notifications")
    suspend fun getNotifications(@Body notificationRequestModel: NotificationRequestModel): Response<NotificationresponseModel>

    @POST("api/v5/general/save-customer-tokens")
    suspend fun saveToken(@Body fcmRegistrationModel: FCMRegistrationModel): Response<NotificationresponseModel>

 @POST("api/v5/review/check-genuine-customer")
 suspend fun getRatingEligibilityData(@Body productData: ProductData): Response<RatingEligibilityResponseModel>

 @GET("api/v5/farm/crop-details/{farm_id}/{crop_id}")
 suspend fun getCropDetails(
  @Path("farm_id") farm_id: String,
  @Path("crop_id") crop_id: String
 ): Response<CropDetailsResponse>

 @GET("api/v5/farm/crop-problems/single/{problemId}")
 suspend fun getCropProblemDetails(@Path("problemId") problemId: String): Response<CropProblemResponse>


 @GET("api/v5/category/app-category/{subcatId}")
 suspend fun getSubCatDetails(@Path("subcatId") subcatId: String): Response<ShopByCatResponseModel>


 @GET("api/v5/category/stores-details/{storeId}")
 suspend fun getStoreDetails(@Path("storeId") storeId: String): Response<ShopByStoreResponseModel>


}