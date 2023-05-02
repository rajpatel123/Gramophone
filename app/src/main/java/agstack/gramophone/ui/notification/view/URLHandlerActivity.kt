package agstack.gramophone.ui.notification.view

import agstack.gramophone.BR
import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityUrlhandlerBinding
import agstack.gramophone.ui.advisory.view.AllCropProblemsActivity
import agstack.gramophone.ui.articles.ArticlesWebViewActivity
import agstack.gramophone.ui.bookmarked.BookmarkedVideosActivity
import agstack.gramophone.ui.cart.view.CartActivity
import agstack.gramophone.ui.farm.view.ViewAllFarmsActivity
import agstack.gramophone.ui.favourite.view.FavouriteProductActivity
import agstack.gramophone.ui.gramcash.GramCashActivity
import agstack.gramophone.ui.home.featured.FeaturedProductActivity
import agstack.gramophone.ui.home.product.activity.ProductDetailsActivity
import agstack.gramophone.ui.home.shop.ShopByActivity
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.notification.NotificationNavigator
import agstack.gramophone.ui.notification.NotificationsAdapter
import agstack.gramophone.ui.notification.model.Data
import agstack.gramophone.ui.notification.viewmodel.NotificationViewModel
import agstack.gramophone.ui.offerslist.OffersListActivity
import agstack.gramophone.ui.order.view.OrderListActivity
import agstack.gramophone.ui.postdetails.view.PostDetailsActivity
import agstack.gramophone.ui.referandearn.ReferAndEarnActivity
import agstack.gramophone.ui.settings.view.LanguageUpdateActivity
import agstack.gramophone.ui.tv.SingleVideoActivity
import agstack.gramophone.ui.userprofile.EditProfileActivity
import agstack.gramophone.ui.userprofile.UserProfileActivity
import agstack.gramophone.ui.weather.WeatherActivity
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.Constants.DEEP_LINK_ADVISORY
import agstack.gramophone.utils.Constants.DEEP_LINK_ARTICLE_CATEGORY
import agstack.gramophone.utils.Constants.DEEP_LINK_ARTICLE_CROPS
import agstack.gramophone.utils.Constants.DEEP_LINK_ARTICLE_DETAILS
import agstack.gramophone.utils.Constants.DEEP_LINK_ARTICLE_SUGGESTED
import agstack.gramophone.utils.Constants.DEEP_LINK_ARTICLE_TRENDING
import agstack.gramophone.utils.Constants.DEEP_LINK_CART
import agstack.gramophone.utils.Constants.DEEP_LINK_CROP_LIST
import agstack.gramophone.utils.Constants.DEEP_LINK_CROP_PROBLEM
import agstack.gramophone.utils.Constants.DEEP_LINK_CROP_PRODUCT
import agstack.gramophone.utils.Constants.DEEP_LINK_DISEASE_DETAILS
import agstack.gramophone.utils.Constants.DEEP_LINK_EDIT_LANGUAGE
import agstack.gramophone.utils.Constants.DEEP_LINK_EDIT_LOCATION
import agstack.gramophone.utils.Constants.DEEP_LINK_EDIT_PHONE_NO
import agstack.gramophone.utils.Constants.DEEP_LINK_FAV_ARTICLE
import agstack.gramophone.utils.Constants.DEEP_LINK_FAV_POSTS
import agstack.gramophone.utils.Constants.DEEP_LINK_FAV_PRODUCTS
import agstack.gramophone.utils.Constants.DEEP_LINK_FAV_TV
import agstack.gramophone.utils.Constants.DEEP_LINK_GRAM_CASH
import agstack.gramophone.utils.Constants.DEEP_LINK_HOME
import agstack.gramophone.utils.Constants.DEEP_LINK_MARKET
import agstack.gramophone.utils.Constants.DEEP_LINK_MY_FARM
import agstack.gramophone.utils.Constants.DEEP_LINK_MY_FAVORITES
import agstack.gramophone.utils.Constants.DEEP_LINK_MY_ORDERS
import agstack.gramophone.utils.Constants.DEEP_LINK_NOTIFICATION
import agstack.gramophone.utils.Constants.DEEP_LINK_OFFER
import agstack.gramophone.utils.Constants.DEEP_LINK_OFFERS
import agstack.gramophone.utils.Constants.DEEP_LINK_POST_DETAIL
import agstack.gramophone.utils.Constants.DEEP_LINK_PRODUCT_DETAIL
import agstack.gramophone.utils.Constants.DEEP_LINK_PRODUCT_LIST
import agstack.gramophone.utils.Constants.DEEP_LINK_REFERRAL
import agstack.gramophone.utils.Constants.DEEP_LINK_SHOP_BY_CATEGORY
import agstack.gramophone.utils.Constants.DEEP_LINK_SHOP_BY_COMPANY
import agstack.gramophone.utils.Constants.DEEP_LINK_SHOP_BY_COMPANY_NAME
import agstack.gramophone.utils.Constants.DEEP_LINK_SHOP_BY_STORE
import agstack.gramophone.utils.Constants.DEEP_LINK_SOCIAL
import agstack.gramophone.utils.Constants.DEEP_LINK_WEATHER_INFO
import agstack.gramophone.utils.Constants.DEEP_LINK_YOUTUBE
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import agstack.gramophone.utils.hasInternetConnection
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.activity.viewModels
import com.amnix.xtension.extensions.isNotNullOrBlank
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.moengage.core.Properties
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class URLHandlerActivity :
    BaseActivityWrapper<ActivityUrlhandlerBinding, NotificationNavigator, NotificationViewModel>(),
    NotificationNavigator {

    private val notificationViewModel: NotificationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!hasInternetConnection(this)){
            finishActivity()
        }
        try {
            val uri = Uri.parse(intent?.getStringExtra(Constants.URI))
        }catch (e:Exception){
            finishActivity()
            return
        }
        val uri = Uri.parse(intent?.getStringExtra(Constants.URI))
//        val uri =
//            Uri.parse("https://www.gramophone.in/app?category=cropProblem&problemId=338001&cropId=100029")
        if (intent?.getStringExtra(Constants.URI)?.contains("category") == true){
            openDeepLinkForIntent(uri)
        }else{
            intent.data = Uri.parse(intent?.getStringExtra(Constants.URI))

            FirebaseDynamicLinks.getInstance()
                .getDynamicLink(intent)
                .addOnSuccessListener(this) { pendingDynamicLinkData ->
                    // Get deep link from result (may be null if no link is found)
                    var deepLink: Uri? = null
                    if (pendingDynamicLinkData != null) {
                        deepLink = pendingDynamicLinkData.link
                      Log.d("LinkRaj", ""+deepLink)
                    }
                }
                .addOnFailureListener(this) { e ->

                }
        }

    }

    override fun getLayoutID(): Int = R.layout.activity_urlhandler

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): NotificationViewModel = notificationViewModel

    override fun updateNotificationList(
        body: NotificationsAdapter, notificationClicked: ((Data) -> Unit)?
    ) {
    }

    override fun handleDeepLink(it: Data) {
    }


    private fun openDeepLinkForIntent(uri: Uri) {
        try {
            val pageName = uri.getQueryParameter("category")
            val utm_source = uri.getQueryParameter("utm_source")
            val utm_url = uri.getQueryParameter("utm_url")

            if (utm_source.isNotNullOrBlank()) {
                SharedPreferencesHelper.instance?.putString(Constants.UTM_SOURCE, utm_source)
                SharedPreferencesHelper.instance?.putString(Constants.UTM_URL, utm_url)
            }


            val params = uri.queryParameterNames
            when (pageName) {
                DEEP_LINK_CROP_LIST -> {
                }


                DEEP_LINK_SHOP_BY_CATEGORY -> {
                    val category_id = uri.getQueryParameter("category_id")
                    notificationViewModel.getSubCatDetails(category_id!!)
                }


                DEEP_LINK_POST_DETAIL -> {
                    val postId = uri.getQueryParameter("postId")
                    openAndFinishActivity(
                        PostDetailsActivity::class.java,
                        Bundle().apply {
                            putString(Constants.POST_ID, postId)
                        })
                }

                DEEP_LINK_SHOP_BY_STORE -> {
                    val store_id = uri.getQueryParameter("store_id")
                    notificationViewModel.getStoreDetails(store_id!!)
                }

                DEEP_LINK_HOME -> {
                    finishActivity()
                }

                DEEP_LINK_YOUTUBE -> {
                    val videoID = uri.getQueryParameter("videoId")
                    if (!TextUtils.isEmpty(videoID))
                    openAndFinishActivity(
                        SingleVideoActivity::class.java,
                        Bundle().apply {
                            putString(Constants.VideoId, videoID)
                        })
                }

                DEEP_LINK_MARKET -> {
                    SharedPreferencesHelper.instance?.putString(Constants.TARGET_PAGE, "market")
                    finishActivity()
                }

                DEEP_LINK_PRODUCT_DETAIL -> {
                    val pID = uri.getQueryParameter("productId")
                    val bundle = Bundle()
                    bundle.putParcelable(Constants.PRODUCT, ProductData(pID?.toInt()))
                    openAndFinishActivity(ProductDetailsActivity::class.java, bundle)
                }

                DEEP_LINK_PRODUCT_LIST -> {
                    if (uri.toString().contains("subCategoryId")) {
                        val categoryId = uri.getQueryParameter("categoryId")
                        val subCategoryId = uri.getQueryParameter("subCategoryId")
                        val categoryName = uri.getQueryParameter("subCategoryName")

                        if (categoryId != null) {
                            notificationViewModel.getCategoryDetails(
                                categoryId,
                                categoryName,
                                subCategoryId!!
                            )
                        }

                    }else{
                        val categoryId = uri.getQueryParameter("categoryId")
                        val categoryName = uri.getQueryParameter("categoryName")

                        if (categoryId != null) {
                            notificationViewModel.getCategoryDetails(
                                categoryId,
                                categoryName,
                                ""
                            )
                        }
                    }

                }

                DEEP_LINK_CROP_PRODUCT -> {
                    finishActivity()
                }

                DEEP_LINK_EDIT_LANGUAGE -> {
                    openAndFinishActivity(LanguageUpdateActivity::class.java, null)
                }

                DEEP_LINK_CROP_PROBLEM -> {
                    val problemId = uri.getQueryParameter("problemId")
                    notificationViewModel.getCropProblemDetails(problemId!!)

                }

                DEEP_LINK_ADVISORY -> {
                    val farmId = uri.getQueryParameter("farmId")
                    val cropId = uri.getQueryParameter("cropId")
                    val isCustomerFarms = uri.getQueryParameter("isCustomerFarms")

                    notificationViewModel.getCropDetails(farmId,cropId,isCustomerFarms)
                }

                DEEP_LINK_MY_FARM -> {
                    openAndFinishActivity(ViewAllFarmsActivity::class.java, Bundle().apply {
                        putString(Constants.REDIRECTION_SOURCE, "Home")
                    })
                }

                DEEP_LINK_SOCIAL -> {
                    val tabId = uri.getQueryParameter("tab")
                    val tab = when (tabId) {
                        "Latest" -> {
                            0
                        }
                        "Following" -> {

                            2
                        }
                        "Trending" -> {
                            1
                        }
                        "Expert" -> {
                            3
                        }
                        else -> {
                            0
                        }
                    }

                    SharedPreferencesHelper.instance?.putBoolean(
                        Constants.TARGET_PAGE_FROM_DEEP_LINK,
                        true
                    )
                    SharedPreferencesHelper.instance?.putString(Constants.TARGET_PAGE, "social")
                    SharedPreferencesHelper.instance?.putInteger(Constants.TARGET_PAGE_TAB, tab)
                    finishActivity()
                }

                DEEP_LINK_WEATHER_INFO -> {
                    openAndFinishActivity(WeatherActivity::class.java, null)
                }

                DEEP_LINK_NOTIFICATION -> {
                    openAndFinishActivity(NotificationActivity::class.java, null)
                }

                DEEP_LINK_MY_ORDERS -> {
                    openAndFinishActivity(OrderListActivity::class.java, null)
                }

                DEEP_LINK_REFERRAL -> {
                    openAndFinishActivity(ReferAndEarnActivity::class.java, null)
                }


                DEEP_LINK_GRAM_CASH -> {
                    openAndFinishActivity(GramCashActivity::class.java, null)
                }

                DEEP_LINK_FAV_ARTICLE -> {
                    openActivity(ArticlesWebViewActivity::class.java, Bundle().apply {
                        putString(
                            Constants.PAGE_URL,
                            BuildConfig.BASE_URL_ARTICLES + Constants.FAVOURITE_ARTICLES
                        )

                        putString(
                            Constants.PAGE_SOURCE, "DeepLink"
                        )
                    })
                }

                DEEP_LINK_ARTICLE_TRENDING -> {
                    openActivity(ArticlesWebViewActivity::class.java, Bundle().apply {
                        putString(
                            Constants.PAGE_URL,
                            BuildConfig.BASE_URL_ARTICLES + Constants.TRENDING_ARTICLES
                        )

                        putString(
                            Constants.PAGE_SOURCE, "DeepLink"
                        )
                    })
                }

                DEEP_LINK_ARTICLE_SUGGESTED -> {
                    openActivity(ArticlesWebViewActivity::class.java, Bundle().apply {
                        putString(
                            Constants.PAGE_URL,
                            BuildConfig.BASE_URL_ARTICLES + Constants.SUGGESTED_ARTICLES
                        )

                        putString(
                            Constants.PAGE_SOURCE, "DeepLink"
                        )
                    })
                }

                DEEP_LINK_ARTICLE_CROPS -> {
                    openActivity(ArticlesWebViewActivity::class.java, Bundle().apply {
                        putString(
                            Constants.PAGE_URL,
                            BuildConfig.BASE_URL_ARTICLES + "/crops/"+uri.getQueryParameter("cropName")

                        )

                        putString(
                            Constants.PAGE_SOURCE, "DeepLink"
                        )
                    })
                }

                DEEP_LINK_ARTICLE_CATEGORY -> {
                    openAndFinishActivity(ArticlesWebViewActivity::class.java, Bundle().apply {
                        putString(
                            Constants.PAGE_URL,
                            BuildConfig.BASE_URL_ARTICLES + "/categories/"+ uri.getQueryParameter("categoryName")!!
                                .replace(" ","%20")

                        )

                        putString(
                            Constants.PAGE_SOURCE, "DeepLink"
                        )
                    })
                }


                DEEP_LINK_FAV_POSTS -> {
                    SharedPreferencesHelper.instance?.putBoolean(
                        Constants.TARGET_PAGE_FROM_DEEP_LINK,
                        true
                    )
                    SharedPreferencesHelper.instance?.putString(Constants.TARGET_PAGE, "social")
                    SharedPreferencesHelper.instance?.putInteger(Constants.TARGET_PAGE_TAB, 5)
                    finishActivity()
                }

                DEEP_LINK_FAV_PRODUCTS -> {
                    openActivity(FavouriteProductActivity::class.java)

                }

                DEEP_LINK_EDIT_LOCATION -> {
                    openActivity(UserProfileActivity::class.java)
                }


                DEEP_LINK_FAV_TV -> {
                    openActivity(BookmarkedVideosActivity::class.java, null)

                }


                DEEP_LINK_OFFER -> {
//                    val promotionId = uri.getQueryParameter("promotionId")
//
//                    promotionId?.toInt()?.let { notificationViewModel.getOfferDetails(it) }
//
//                   // openActivity(BookmarkedVideosActivity::class.java, null)

                    finishActivity()

                }
                DEEP_LINK_MY_FAVORITES -> {
                    SharedPreferencesHelper.instance?.putString(Constants.TARGET_PAGE, "fav")
                    finishActivity()


                }
                DEEP_LINK_CART -> {
                    openAndFinishActivity(CartActivity::class.java, null)
                }

                DEEP_LINK_OFFERS -> {
                    openAndFinishActivity(OffersListActivity::class.java, null)
                }

                DEEP_LINK_SHOP_BY_COMPANY_NAME -> {
                    val companyId = uri.getQueryParameter("companyId")
                    val companyName = uri.getQueryParameter("companyName")

                    openAndFinishActivity(FeaturedProductActivity::class.java,
                        Bundle().apply {
                            putString(Constants.COMPANY_ID, companyId)
                            putString(Constants.COMPANY_NAME, companyName)
                            putString(Constants.COMPANY_IMAGE, "")
                        })
                }

                DEEP_LINK_SHOP_BY_COMPANY-> {

                    openAndFinishActivity(ShopByActivity::class.java,
                        Bundle().apply {
                            putString(Constants.SHOP_BY_TYPE, Constants.SHOP_BY_COMPANY)
                            putString("deeplink", "")
                            putParcelable(Constants.SHOP_BY_COMPANY,
                                null)
                        })

                }


                DEEP_LINK_EDIT_PHONE_NO -> {
                    openAndFinishActivity(EditProfileActivity::class.java, null)

                    val properties1 = Properties()
                    properties1.addAttribute(
                        "Customer_Id",
                        SharedPreferencesHelper.instance?.getString(
                            SharedPreferencesKeys.CUSTOMER_ID
                        )!!
                    ).addAttribute(
                        "Redirection_Source","DeepLink"
                    )
                        .setNonInteractive()
                    sendMoEngageEvent("KA_View_ReferAndEarn", properties1)
                }

                DEEP_LINK_DISEASE_DETAILS -> {
                    val cropId = uri.getQueryParameter("cropId")!!.toInt()
                    val stageId = uri.getQueryParameter("stageId")!!.toInt()
                    openActivity(
                        AllCropProblemsActivity::class.java,
                        Bundle().apply {
                            putInt(Constants.STAGE_ID, cropId)
                            putInt(Constants.CROP_ID, stageId)
                        })
                }
                DEEP_LINK_ARTICLE_DETAILS->{

                    if (uri.toString().contains("articlesContentUrl")){
                        val url = uri.getQueryParameter("articlesContentUrl")
                        openAndFinishActivity(ArticlesWebViewActivity::class.java, Bundle().apply {
                            putString(
                                Constants.PAGE_URL, url
                            )

                            putString(
                                Constants.PAGE_SOURCE, "gramo"
                            )
                        })
                    }else if(uri.toString().contains("articlesContentUrl")){
                        val url = uri.getQueryParameter("webContentUrl")
                        openAndFinishActivity(ArticlesWebViewActivity::class.java, Bundle().apply {
                            putString(
                                Constants.PAGE_URL, url
                            )

                            putString(
                                Constants.PAGE_SOURCE, "gramo"
                            )
                        })
                    }else{
                        openAndFinishActivity(ArticlesWebViewActivity::class.java, Bundle().apply {
                            putString(
                                Constants.PAGE_URL,
                                BuildConfig.BASE_URL_ARTICLES
                            )


                            putString(
                                Constants.PAGE_SOURCE, "gramo"
                            )
                        })
                    }

                }

                else -> {
                    finishActivity()
                }
            }
        }catch (ex: Exception){
          finishActivity()
        }

    }

}