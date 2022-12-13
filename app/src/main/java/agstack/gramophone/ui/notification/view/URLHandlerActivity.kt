package agstack.gramophone.ui.notification.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityUrlhandlerBinding
import agstack.gramophone.ui.notification.NotificationNavigator
import agstack.gramophone.ui.notification.NotificationsAdapter
import agstack.gramophone.ui.notification.model.Data
import agstack.gramophone.ui.notification.viewmodel.NotificationViewModel
import agstack.gramophone.utils.LocaleManagerClass
import agstack.gramophone.utils.SharedPreferencesHelper
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.viewModels

class URLHandlerActivity :
    BaseActivityWrapper<ActivityUrlhandlerBinding, NotificationNavigator, NotificationViewModel>(),
    NotificationNavigator {

    private val notificationViewModel: NotificationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

           val uri = intent.extras?.getString("url")
        openDeepLinkForIntent(uri)

    }

    override fun getLayoutID(): Int = R.layout.activity_urlhandler

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): NotificationViewModel = notificationViewModel

    override fun updateNotificationList(
        body: NotificationsAdapter, notificationClicked: ((Data) -> Unit)?
    ) {
    }



    private fun openDeepLinkForIntent(uri: String?) {
        if(intent!=null&&intent.data!=null)
        {
            val url=uri?.toString()

            if(url?.contains("www.gramophone.in") == true)
            {
                try {
                   // handleUrl(uri)
                } catch (error: Exception) {

                }

            }
        }else{
            //openHomeFragment()
        }

    }


//    private fun handleUrl( deepLink: Uri?){
//        var delay=0
//        if(deepLink!=null) {
//            when (deepLink.getQueryParameter(ShareKeys.CategoryKey)) {
//                ShareCategories.FilteredProductList -> {
//                    //  hideProgressDialog()
//                    retrieveFilteredProductList(deepLink.getQueryParameter(ShareKeys.CategoryIdKey), deepLink.getQueryParameter(ShareKeys.CategoryNameKey), delay)
//
//                }
//                ShareCategories.Disease -> {
//                    retrieveDiseaseInfo(deepLink.getQueryParameter(ShareKeys.DiseaseIdKey), deepLink.getQueryParameter(ShareKeys.CropIdKey), deepLink.getQueryParameter(ShareKeys.ProblemCategoryKey), delay)
//
//                }
//                ShareCategories.CropProblem -> {
//                    deepLink.getQueryParameter(ShareKeys.CropIdKey)?.let { loadCropProblems(it, deepLink.getQueryParameter(ShareKeys.CropNameKey), deepLink.getQueryParameter(ShareKeys.ProductCategoryKey)) }
//
//                }
//                ShareCategories.CropProduct -> {
//                    deepLink.getQueryParameter(ShareKeys.CropIdKey)?.let { loadCropProducts(it, deepLink.getQueryParameter(ShareKeys.CropNameKey), deepLink.getQueryParameter(ShareKeys.ProductCategoryKey)) }
//
//                }
//                ShareCategories.CropDetail -> {
//                    deepLink.getQueryParameter(ShareKeys.CropIdKey)?.let { openCropDetail(it) }
//
//                }
//                ShareCategories.CropList -> {
//                    deepLink.getQueryParameter(ShareKeys.CategoryIdKey)?.let { openCropList(it) }
//
//                }
//                ShareCategories.MandiPrices -> {
//                    displayMandiPrices()
//                }
//                ShareCategories.MandiDetail -> {
//                    displayMandiDetail(deepLink.getQueryParameter(ShareKeys.MandiIdKey), deepLink.getQueryParameter(ShareKeys.CropIdKey))
//                }
//                ShareCategories.SelectedMandiByCrop -> {
//                    deepLink.getQueryParameter(ShareKeys.CropIdKey)?.let { displaySelectedMandi(deepLink.getQueryParameter(ShareKeys.CropNameKey), it) }
//                }
//                ShareCategories.NutritionProblem -> {
//                    deepLink.getQueryParameter(ShareKeys.NutritionProblemIdKey)?.let { retrieveNutritionProblemInfo(it, deepLink.getQueryParameter(ShareKeys.CropIdKey), deepLink.getQueryParameter(ShareKeys.ProblemCategoryKey)) }
//                }
//                ShareCategories.ProductDetail -> {
//                    deepLink.getQueryParameter(ShareKeys.ProductDetailProductId)?.let {
//                        displayProductWithId(
//                            it, delay = 500)
//                    }
//                }
//                ShareCategories.WeatherInfo -> {
//                    displayWeatherForecast(delay = 1000)
//                }
//                ShareCategories.GramSalah -> {
//                    deepLink.getQueryParameter(ShareKeys.GramSalahContentUrlKey)?.let { displayGramSalah(it, deepLink.getQueryParameter(ShareKeys.GramSalahContentTitleKey)) }
//                }
//                ShareCategories.GovtSchemes -> {
//                    deepLink.getQueryParameter(ShareKeys.GovtSchemesContentUrlKey)?.let { displayGovtSchemes(it, deepLink.getQueryParameter(ShareKeys.GovtSchemesContentTitleKey)) }
//
//                }
//                ShareCategories.Articles -> {
//                    deepLink.getQueryParameter(ShareKeys.ArticlesContentUrlKey)?.let { displayArticle(it, deepLink.getQueryParameter(ShareKeys.ArticlesContentTitleKey)) }
//
//                }
//                ShareCategories.Tip -> {
//                    displayTips(deepLink.getQueryParameter(ShareKeys.TipsContentUrlKey), deepLink.getQueryParameter(ShareKeys.TipsContentTitleKey))
//                }
//                ShareCategories.FarmTips -> {
//                    deepLink.getQueryParameter(ShareKeys.CropIdKey)?.let {
//                        openFarmTips(it, deepLink.getQueryParameter(ShareKeys.StageIdKey)!!) }
//                }
//                ShareCategories.CommunityPost -> {
//                    deepLink.getQueryParameter(ShareKeys.CommunityPostIdKey)?.let { openPostDetailsActivity(it) }
//
//                }
//                ShareCategories.CreatePost -> {
//                    openCreatePostActivity()
//                }
//                ShareCategories.AddFarm -> {
//                    openAddFarmActivity()
//                }
//                ShareCategories.MyFarm -> {
//                    openMyFarmFragment()
//                }
////                ShareCategories.GramCash -> {
////                    openGramCash(deepLink.getQueryParameter(IntentKeys.InAppTitle), deepLink.getQueryParameter(IntentKeys.InAppDescription), deepLink.getQueryParameter(IntentKeys.InAppImageUrl))
////                }
//                ShareCategories.Market -> {
//                    openMarketFragment()
//                }
//                ShareCategories.Social -> {
//                    openSocialFragment()
//                }
//                ShareCategories.FarmDetail -> {
//                    deepLink.getQueryParameter(IntentKeys.FarmIdKey)?.let { openFarmDetail(it) }
//                }
//                ShareCategories.RecommendationDetail -> {
//                    deepLink.getQueryParameter(IntentKeys.FarmIdKey)?.let { openRecommendationDetail(it) }
//                }
//                ShareCategories.FarmStages -> {
//                    deepLink.getQueryParameter(IntentKeys.FarmIdKey)?.let { openFarmStages(it, deepLink.getQueryParameter(IntentKeys.CropId)!!) }
//                }
//                ShareCategories.FarmProblems -> {
//                    deepLink.getQueryParameter(IntentKeys.FarmIdKey)?.let { openFarmProblems(it) }
//                }
//                ShareCategories.Profile -> {
//                    deepLink.getQueryParameter(ShareKeys.AuthorUUidKey)?.let { openUsersProfile(it) }
//                }
//                ShareCategories.Settings -> {
//                    openSettings()
//                }
//                ShareCategories.Referral -> {
//                    openReferral()
//                }
//                ShareCategories.Quiz -> {
//                    openQuiz()
//                }
//                ShareCategories.VyapaarBuyer -> {
//                    gotoVyapaarBuyer()
//                }
//
//                //Traders Page
//                ShareCategories.Trading -> {
//                    gotoVyapaarTrader()
//                }
//
//                //MyListingPage
//                ShareCategories.TraderListing -> {
//                    gotoTraderListing()
//                }
//                ShareCategories.QuizDetail -> {
//                    openQuizDetail(deepLink.getQueryParameter(ShareKeys.QuizId))
//                }
//                ShareCategories.GramophoneVideo -> {
//                    displayGramophoneVideo(deepLink.getQueryParameter(ShareKeys.VideoId), deepLink.getQueryParameter(ShareKeys.VideoName), deepLink.getQueryParameter(ShareKeys.PlayListId), deepLink.getQueryParameter(ShareKeys.PlayListName))
//                }
//                ShareCategories.GramophoneTv -> {
//                    displayGramophoneTv()
//                }
//                ShareCategories.NotificationEvent -> {
//                    displayNotificationEvent()
//                }
//                ShareCategories.Search -> {
//                    deepLink.getQueryParameter(ShareKeys.searchText)?.let { openSearch(it) }
//                }
//                ShareCategories.BuyCommodity->{
//                    deepLink.getQueryParameter(ShareKeys.ListingIdKey)?.let { openBuyDetail(it) }
//                }
//                ShareCategories.SellCommodity->{
//                    deepLink.getQueryParameter(ShareKeys.ListingIdKey)?.let { openSellDetail(it) }
//                }
//                ShareCategories.GramCashDetail->{
//                    openGramCash()
//                }
//                ShareCategories.GramCashExpiring->{
//                    openGramCashExpiring()
//                }
//                ShareCategories.Whatsapp->{
//                    openWhatsappActivity()
//                }
//                ShareCategories.YourBag->{
//                    openBagActivity()
//                }
///*
//                ShareCategories.MyBuyListing->{
//
//
//                }
//                ShareCategories.MySellListing->{
//
//                }*/
//
//
//                else -> {
//                    openHomeFragment()
//                }
//
//            }
//        }else{
//            openHomeFragment()
//        }
//    }
//
//    private fun openRecommendationDetail(farmId:String){
//        val intent = Intent(this, RecommendationDetailActivity::class.java)
//        intent.putExtra(IntentKeys.FarmIdKey,farmId);
//        launchIntent(intent)
//        var hashMap=HashMap<String,Any?>()
//        farmId?.let { hashMap.put(getString(R.string.analytic_farm_id), it) };
//        GramophoneApplication.getInstance().setMixPanelDataWithScreenName(hashMap,getString(R.string.analytic_viewFarmRecommendation), null, null, TAG)
//
//    }
//    private fun openFarmStages(farmId:String,cropId: String){
//        val intent = Intent(this, CropStagesActivity::class.java)
//        intent.putExtra(IntentKeys.FarmIdKey,farmId);
//        intent.putExtra(IntentKeys.CropId,cropId);
//        launchIntent(intent)
//        var hashMap=HashMap<String,Any?>()
//        farmId?.let { hashMap.put(getString(R.string.analytic_farm_id), it) };
//        GramophoneApplication.getInstance().setMixPanelDataWithScreenName(hashMap,getString(R.string.analytic_crop_stages), null, null, TAG)
//
//    }
//    private fun openFarmProblems(farmId:String){
//        val intent = Intent(this, FarmCropProblemsListActivity::class.java)
//        intent.putExtra(IntentKeys.FarmIdKey,farmId);
//        launchIntent(intent)
//        var hashMap=HashMap<String,Any?>()
//        farmId?.let { hashMap.put(getString(R.string.analytic_farm_id), it) };
//        GramophoneApplication.getInstance().setMixPanelDataWithScreenName(hashMap,getString(R.string.analytic_view_all_problem_non_formatted), null, null, TAG)
//
//    }
//    private fun openSettings(){
//        val intent = Intent(this, SettingsActivity::class.java)
//        launchIntent(intent)
//        GramophoneApplication.getInstance().setMixClickEventData(getString(R.string.analytic_settings), getString(R.string.analytic_setting_screen_name), null, TAG)
//
//    }
//
//    private fun openReferral(){
//        val intent = Intent(this, ReferralActivity::class.java)
//        launchIntent(intent)
//        GramophoneApplication.getInstance().setMixClickEventData(getString(R.string.analytic_referral), getString(R.string.analytic_referral_screen_name), null, TAG)
//
//    }
//    private fun openQuiz(){
//        val intent = Intent(this, QuizActivity::class.java)
//        launchIntent(intent)
//        GramophoneApplication.getInstance().setMixClickEventData(getString(R.string.analytic_quiz), getString(R.string.analytic_quiz), null, TAG)
//
//    }
//    private fun openQuizDetail(id:String?){
//        val intent = Intent(this, QuizDetailActivity::class.java)
//        intent.putExtra(IntentKeys.QuizIdKey,id);
//        launchIntent(intent)
//        GramophoneApplication.getInstance().setMixClickEventData(getString(R.string.analytic_quiz), getString(R.string.analytic_quiz), null, TAG)
//
//    }
//
//    private fun gotoVyapaarBuyer(){
//        sharedPreferencesHelper!!.putString(SharedPreferencesKeys.AppMode, Constants.Trader)
//        val intent = Intent(this, VyapaarHomeActivity::class.java)
//        intent.putExtra(IntentKeys.LaunchFragmentKey,Constants.BuySellFragment);
//        startActivity(intent
//            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
//        GramophoneApplication.getInstance().setMixClickEventData(getString(R.string.analytic_vyapaar), getString(R.string.analytic_vyapaar), null, TAG)
//
//    }
//
//    // Traders Page
//    private fun gotoVyapaarTrader(){
//        sharedPreferencesHelper!!.putString(SharedPreferencesKeys.AppMode, Constants.Trader)
//        val intent = Intent(this, VyapaarHomeActivity::class.java)
//        intent.putExtra(IntentKeys.LaunchFragmentKey,Constants.TradersFragment);
//        startActivity(intent
//            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
//        GramophoneApplication.getInstance().setMixClickEventData(getString(R.string.analytic_vyapaar), getString(R.string.analytic_vyapaar), null, TAG)
//
//    }
//
//    //MyListing Page
//    private fun gotoTraderListing(){
//        sharedPreferencesHelper!!.putString(SharedPreferencesKeys.AppMode, Constants.Trader)
//        val intent = Intent(this, VyapaarHomeActivity::class.java)
//        intent.putExtra(IntentKeys.LaunchFragmentKey,Constants.MyListingFragment);
//        startActivity(intent
//            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
//        // GramophoneApplication.getInstance().setMixClickEventData(getString(R.string.analytic_vyapaar), getString(R.string.analytic_vyapaar), null, TAG)
//
//    }
//    private fun openUsersProfile(uuid:String){
//        if(uuid!=null) {
//            SharedPreferencesHelper.initializeInstance(this);
//            var uuid2 = SharedPreferencesHelper.getInstance().getString(SharedPreferencesKeys.UUIdKey)
//            if (uuid.equals(uuid2)) {
//                val intent = Intent(this, MyAccountActivity::class.java)
//                launchIntent(intent)
//                var hashMap = HashMap<String, Any?>()
//                uuid?.let { hashMap.put(getString(R.string.analytic_profile_id), it) };
//                GramophoneApplication.getInstance().setMixPanelDataWithScreenName(hashMap, getString(R.string.analytic_view_my_account), null, null, TAG);
//
//            } else {
//                val intent = Intent(this, AccountOtherActivity::class.java)
//                intent.putExtra(IntentKeys.authorUUID, uuid);
//                launchIntent(intent)
//                var hashMap = HashMap<String, Any?>()
//                uuid?.let { hashMap.put(getString(R.string.analytic_profile_id), it) };
//                GramophoneApplication.getInstance().setMixPanelDataWithScreenName(hashMap, getString(R.string.analytic_view_profile), null, null, TAG)
//            }
//        }
//    }
//
//
//    fun saveActionType() {
//        SharedPreferencesHelper.initializeInstance(this)
//        val sharedPreferencesHelper = SharedPreferencesHelper.getInstance()
//        if (intent.action != null) {
//            sharedPreferencesHelper.putString(SharedPreferencesKeys.ActionTypeKey, intent.action)
//        }
//
//        MySingleton.getInstance().viewIntent=intent
//
//        LanguageSelectionActivity()
//
//    }
//
//    fun LanguageSelectionActivity() {
//        val intent = Intent(this, LanguageSelectionActivity::class.java)
//        launchIntent(intent)
//    }
//
//    override fun onNetworkConnectionChanged(isConnected: Boolean) {
//        if(isConnected) {
//
//        }
//
//    }
//    internal fun displayGramSalah(contentUrlString: String, contentTitle: String?) {
//        val localizedContentUrl = ContentProviderWebActivity.localizedContentUrl(contentUrlString, LocaleManagerClass.getLangCodeFromPreferences(this))
//        if (localizedContentUrl == null) {
//            Toast.makeText(this, String.format(getString(R.string.web_content_malformed_url_error_msg), contentUrlString, "displayGramSalah"), Toast.LENGTH_LONG).show()
//        } else {
//            val fragmentActivity = this
//            val langIsoCode = LocaleManagerClass.getLangCodeFromPreferences(this)
//            if (langIsoCode != null && !langIsoCode.equals("", ignoreCase = true)) {
//                LocaleManagerClass.updateLocale(langIsoCode, resources)
//            }
//            val handler = Handler()
//            handler.postDelayed({
//                fragmentActivity.launchFromUrlContentProviderWebActivity(
//                    localizedContentUrl,
//                    getString(R.string.gram_salah_detail_title),
//                    contentTitle!!,
//                    getString(R.string.gram_salah_share_social_media_title),
//                    getString(R.string.gram_salah_share_embeded_social_description_msg),
//                    ShareCategories.GramSalah,
//                    ShareKeys.GramSalahContentUrlKey,
//                    ShareKeys.GramSalahContentTitleKey)
//            }, 500)
//        }
//        var hashMap=HashMap<String,Any?>()
//        contentUrlString?.let { hashMap.put(getString(R.string.analytic_blog_url), it) };
//        contentTitle?.let { hashMap.put(getString(R.string.analytic_blog_title), it) };
//        GramophoneApplication.getInstance().setMixPanelDataWithScreenName(hashMap,getString(R.string.analytic_view_gram_salah), null, null, TAG)
//
//    }
//    internal fun displayArticle(contentUrlString: String, contentTitle: String?) {
//        val localizedContentUrl = ContentProviderWebActivity.localizedContentUrl(contentUrlString, LocaleManagerClass.getLangCodeFromPreferences(this))
//        if (localizedContentUrl == null) {
//            Toast.makeText(this, String.format(getString(R.string.web_content_malformed_url_error_msg), contentUrlString, "displayGramSalah"), Toast.LENGTH_LONG).show()
//        } else {
//            val fragmentActivity = this
//            val langIsoCode = LocaleManagerClass.getLangCodeFromPreferences(this)
//            if (langIsoCode != null && !langIsoCode.equals("", ignoreCase = true)) {
//                LocaleManagerClass.updateLocale(langIsoCode, resources)
//            }
//            val handler = Handler()
//            handler.postDelayed({
//                fragmentActivity.launchFromUrlContentProviderWebActivity(
//                    localizedContentUrl,
//                    getString(R.string.article_detail_title),
//                    contentTitle!!,
//                    getString(R.string.article_share_social_media_title),
//                    getString(R.string.article_share_embeded_social_description_msg),
//                    ShareCategories.Articles,
//                    ShareKeys.ArticlesContentUrlKey,
//                    ShareKeys.ArticlesContentTitleKey)
//            }, 100)
//        }
//        var hashMap=HashMap<String,Any?>()
//        contentUrlString?.let { hashMap.put(getString(R.string.analytic_blog_url), it) };
//        contentTitle?.let { hashMap.put(getString(R.string.analytic_blog_title), it) };
//        GramophoneApplication.getInstance().setMixPanelDataWithScreenName(hashMap,getString(R.string.analytic_view_all_articles), null, null, TAG)
//
//    }
//
//    internal fun displayTips(contentUrlString: String?, contentTitle: String?) {
//        val localizedContentUrl = ContentProviderWebActivity.localizedContentUrl(contentUrlString, LocaleManagerClass.getLangCodeFromPreferences(this))
//        if (localizedContentUrl == null) {
//            Toast.makeText(this, String.format(getString(R.string.web_content_malformed_url_error_msg), contentUrlString, "displayGramSalah"), Toast.LENGTH_LONG).show()
//        } else {
//            val fragmentActivity = this
//            val langIsoCode = LocaleManagerClass.getLangCodeFromPreferences(this)
//            if (langIsoCode != null && !langIsoCode.equals("", ignoreCase = true)) {
//                LocaleManagerClass.updateLocale(langIsoCode, resources)
//            }
//            val handler = Handler()
//            handler.postDelayed({
//                fragmentActivity.launchFromUrlContentProviderWebActivity(
//                    localizedContentUrl,
//                    getString(R.string.tip_detail_title),
//                    contentTitle!!,
//                    getString(R.string.tip_share_social_media_title),
//                    getString(R.string.tip_share_embeded_social_description_msg),
//                    ShareCategories.Tip,
//                    ShareKeys.TipsContentUrlKey,
//                    ShareKeys.TipsContentTitleKey)
//            }, 500)
//        }
//        var hashMap=HashMap<String,Any?>()
//        contentUrlString?.let { hashMap.put(getString(R.string.analytic_blog_url), it) };
//        contentTitle?.let { hashMap.put(getString(R.string.analytic_blog_title), it) };
//        GramophoneApplication.getInstance().setMixPanelDataWithScreenName(hashMap,getString(R.string.analytic_view_farm_tips), null, null, TAG)
//
//    }
//
//    internal fun displayGovtSchemes(contentUrlString: String, contentTitle: String?) {
//        val localizedContentUrl = ContentProviderWebActivity.localizedContentUrl(contentUrlString, LocaleManagerClass.getLangCodeFromPreferences(this))
//        if (localizedContentUrl == null) {
//            Toast.makeText(this, String.format(getString(R.string.web_content_malformed_url_error_msg), contentUrlString, "displayGramSalah"), Toast.LENGTH_LONG).show()
//        } else {
//            val fragmentActivity = this
//            val langIsoCode = LocaleManagerClass.getLangCodeFromPreferences(this)
//            if (langIsoCode != null && !langIsoCode.equals("", ignoreCase = true)) {
//                LocaleManagerClass.updateLocale(langIsoCode, resources)
//            }
//            val handler = Handler()
//            handler.postDelayed({
//                fragmentActivity.launchFromUrlContentProviderWebActivity(
//                    localizedContentUrl,
//                    getString(R.string.gov_schemes_detail_title),
//                    contentTitle!!,
//                    getString(R.string.gov_schemes_share_social_media_title),
//                    getString(R.string.gov_schemes_share_embeded_social_description_msg),
//                    ShareCategories.GovtSchemes,
//                    ShareKeys.GovtSchemesContentUrlKey,
//                    ShareKeys.GovtSchemesContentTitleKey)
//            }, 500)
//            var hashMap=HashMap<String,Any?>()
//            contentUrlString?.let { hashMap.put(getString(R.string.analytic_blog_url), it) };
//            contentTitle?.let { hashMap.put(getString(R.string.analytic_blog_title), it) };
//            GramophoneApplication.getInstance().setMixPanelDataWithScreenName(hashMap,getString(R.string.analytic_view_govt_schemes), null, null, TAG)
//
//        }
//
//    }
//
//    private fun launchIntent(intent: Intent){
//        if(noClearTask) {
//            startActivity(intent);
//            this.finish();
//        }else{
//            intent.putExtra(IntentKeys.FromUrlHandlerActivity, IntentKeys.UrlHandlerActivity)
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//            intent.setAction(Intent.ACTION_VIEW)
//            startActivity(intent)
//        }
//    }
//    private fun retrieveFilteredProductList(categoryId: String?, categoryName: String?, delay: Int) {
//        //showProgressDialog();
//
//        val intent = Intent(this, Product::class.java)
//
//        intent.putExtra(IntentKeys.CategoryIdKey, categoryId)
//        intent.putExtra(IntentKeys.CategoryNameKey, categoryName)
//        launchIntent(intent)
//        var newcategoryName=categoryName;
//        var hashMap=HashMap<String,Any?>()
//        categoryId?.let { hashMap.put(getString(R.string.analytic_product_category_id), it) };
//        if(categoryName!=null)
//        {
//            newcategoryName=
//                String.format(getString(R.string.analytic_view_all_product),
//                    categoryName)
//        }else{
//            newcategoryName=getString(R.string.analytic_view_all_product_non_formatted)
//        }
//        GramophoneApplication.getInstance().setMixPanelDataWithScreenName(hashMap,newcategoryName, categoryName, null, TAG)
//
//    }

}