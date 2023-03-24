package agstack.gramophone.ui.notification.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityUrlhandlerBinding
import agstack.gramophone.ui.advisory.view.AllCropProblemsActivity
import agstack.gramophone.ui.articles.ArticlesWebViewActivity
import agstack.gramophone.ui.farm.view.ViewAllFarmsActivity
import agstack.gramophone.ui.gramcash.GramCashActivity
import agstack.gramophone.ui.home.product.activity.ProductDetailsActivity
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.notification.NotificationNavigator
import agstack.gramophone.ui.notification.NotificationsAdapter
import agstack.gramophone.ui.notification.model.Data
import agstack.gramophone.ui.notification.viewmodel.NotificationViewModel
import agstack.gramophone.ui.referandearn.ReferAndEarnActivity
import agstack.gramophone.ui.settings.view.LanguageUpdateActivity
import agstack.gramophone.ui.userprofile.EditProfileActivity
import agstack.gramophone.ui.weather.WeatherActivity
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.Constants.DEEP_LINK_ADVISORY
import agstack.gramophone.utils.Constants.DEEP_LINK_ARTICLE_DETAILS
import agstack.gramophone.utils.Constants.DEEP_LINK_CROP_LIST
import agstack.gramophone.utils.Constants.DEEP_LINK_CROP_PROBLEM
import agstack.gramophone.utils.Constants.DEEP_LINK_CROP_PRODUCT
import agstack.gramophone.utils.Constants.DEEP_LINK_DISEASE_DETAILS
import agstack.gramophone.utils.Constants.DEEP_LINK_EDIT_LANGUAGE
import agstack.gramophone.utils.Constants.DEEP_LINK_EDIT_PHONE_NO
import agstack.gramophone.utils.Constants.DEEP_LINK_GRAM_CASH
import agstack.gramophone.utils.Constants.DEEP_LINK_HOME
import agstack.gramophone.utils.Constants.DEEP_LINK_MARKET
import agstack.gramophone.utils.Constants.DEEP_LINK_MY_FARM
import agstack.gramophone.utils.Constants.DEEP_LINK_PRODUCT_DETAIL
import agstack.gramophone.utils.Constants.DEEP_LINK_PRODUCT_LIST
import agstack.gramophone.utils.Constants.DEEP_LINK_REFERRAL
import agstack.gramophone.utils.Constants.DEEP_LINK_SHOP_BY_CATEGORY
import agstack.gramophone.utils.Constants.DEEP_LINK_SHOP_BY_STORE
import agstack.gramophone.utils.Constants.DEEP_LINK_SOCIAL
import agstack.gramophone.utils.Constants.DEEP_LINK_WEATHER_INFO
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
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

            val params = uri.queryParameterNames
            when (pageName) {
                DEEP_LINK_CROP_LIST -> {
                }


                DEEP_LINK_SHOP_BY_CATEGORY -> {
                    val category_id = uri.getQueryParameter("category_id")
                    notificationViewModel.getSubCatDetails(category_id!!)
                }

                DEEP_LINK_SHOP_BY_STORE -> {
                    val store_id = uri.getQueryParameter("store_id")
                    notificationViewModel.getStoreDetails(store_id!!)
                }

                DEEP_LINK_HOME -> {
                    finishActivity()
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
                    val categoryId = uri.getQueryParameter("categoryId")
                    val categoryName = uri.getQueryParameter("categoryName")
                    if (categoryId != null) {
                        notificationViewModel.getCategoryDetails(categoryId,categoryName)
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
                    SharedPreferencesHelper.instance?.putString(Constants.TARGET_PAGE,"social")
                    finishActivity()
                }

                DEEP_LINK_WEATHER_INFO -> {
                    openAndFinishActivity(WeatherActivity::class.java, null)
                }

                DEEP_LINK_REFERRAL -> {
                    openAndFinishActivity(ReferAndEarnActivity::class.java, null)
                }


                DEEP_LINK_GRAM_CASH -> {
                    openAndFinishActivity(GramCashActivity::class.java, null)
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
                    }else{
                        val url = uri.getQueryParameter("webContentUrl")
                        openAndFinishActivity(ArticlesWebViewActivity::class.java, Bundle().apply {
                            putString(
                                Constants.PAGE_URL, url
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