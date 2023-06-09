package agstack.gramophone.ui.home.view

import agstack.gramophone.BR
import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityHomeBinding
import agstack.gramophone.databinding.AllowNotificationHomeBinding
import agstack.gramophone.ui.cart.view.CartActivity
import agstack.gramophone.ui.home.navigator.HomeActivityNavigator
import agstack.gramophone.ui.home.view.fragments.community.CommunityFragment
import agstack.gramophone.ui.home.view.fragments.gramophone.MyGramophoneFragment
import agstack.gramophone.ui.home.view.fragments.market.MarketFragment
import agstack.gramophone.ui.home.view.fragments.market.model.BannerResponse
import agstack.gramophone.ui.home.view.model.FCMRegistrationModel
import agstack.gramophone.ui.home.viewmodel.HomeViewModel
import agstack.gramophone.ui.language.model.InitiateAppDataResponseModel
import agstack.gramophone.ui.language.view.LanguageActivity
import agstack.gramophone.ui.notification.view.NotificationActivity
import agstack.gramophone.ui.notification.view.URLHandlerActivity
import agstack.gramophone.ui.othersporfile.view.OtherUserProfileActivity
import agstack.gramophone.ui.search.view.GlobalSearchActivity
import agstack.gramophone.utils.*
import agstack.gramophone.utils.SharedPreferencesHelper.Companion.instance
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.forEach
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.amnix.xtension.extensions.isNotNullOrEmpty
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.Gson
import com.moe.pushlibrary.MoEHelper
import com.moengage.core.Properties
import com.moengage.core.analytics.MoEAnalyticsHelper
import com.moengage.core.model.AppStatus
import com.moengage.firebase.MoEFireBaseHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_menu_cart_with_counter.*
import kotlinx.android.synthetic.main.navigation_layout.*
import kotlinx.android.synthetic.main.navigation_layout.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity :
    BaseActivityWrapper<ActivityHomeBinding, HomeActivityNavigator, HomeViewModel>(),
    HomeActivityNavigator, View.OnClickListener {

    var from: String = "home"
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var mFirebaseRemoteConfig: FirebaseRemoteConfig
    private lateinit var marketFragment: MarketFragment
    private lateinit var communityFragment: CommunityFragment
    private lateinit var profileFragment: MyGramophoneFragment
//    private lateinit var tradeFragment: TradeFragment
    private lateinit var activeFragment: Fragment
    var drawer: DrawerLayout? = null
    var backPressedTime: Long = 0

    var advisoryActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data?.hasExtra(Constants.GO_TO_COMMUNITY) == true) {
                    showCommunityFragment(data.getStringExtra(Constants.GO_TO_COMMUNITY)!!)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupUi()

        registerWithFCM()
        processDeepLink()

        val initiateAppDataResponseModel = SharedPreferencesHelper.instance?.getParcelable(
            SharedPreferencesKeys.app_data, InitiateAppDataResponseModel::class.java
        )

        var askedCount =  SharedPreferencesHelper.instance?.getInteger(Constants.PUSH_ASKED_COUNT)
        Log.d("Raj",askedCount.toString()+" == "+getLastShownDayCount())
        if (!SharedPreferencesHelper.instance?.getBoolean(Constants.IS_PUSH_SHOWN_TODAY)!! && getLastShownDayCount() > initiateAppDataResponseModel?.gp_api_response_data?.notifi_perm_ask_days!!
            && askedCount!! < initiateAppDataResponseModel?.gp_api_response_data?.notifi_max_attempts!!
        ) {
            SharedPreferencesHelper.instance?.putLong(Constants.PUSH_ASKED_DATE,System.currentTimeMillis())
            askedCount++
            SharedPreferencesHelper.instance?.putInteger(Constants.PUSH_ASKED_COUNT,
                askedCount
            )
            SharedPreferencesHelper.instance?.putBoolean(Constants.IS_PUSH_SHOWN_TODAY,true)
            when {
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                        PackageManager.PERMISSION_GRANTED -> {
                }
                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    val mDialogView =
                        LayoutInflater.from(this).inflate(R.layout.allow_notification_home, null)
                    val dialogBinding = AllowNotificationHomeBinding.bind(mDialogView)
                    dialogBinding.setVariable(BR.viewModel, homeViewModel)
                    dialogBinding.tvDailogueBox.text =
                        initiateAppDataResponseModel?.gp_api_response_data?.notifi_messages
                    //AlertDialogBuilder
                    val mBuilder = AlertDialog.Builder(this)
                        .setView(dialogBinding.root)
                    //show dialog
                    val mAlertDialog = mBuilder.show()
                    mAlertDialog.setCancelable(false)
                    homeViewModel.setDialog(mAlertDialog)
                    mAlertDialog.getWindow()
                        ?.setBackgroundDrawableResource(R.drawable.transparent_background);


                }
                else -> {
                    requestForLocation()
                }
            }
        }
        if (!hasInternetConnection(this)) {
            val intent = Intent(this, LostConnectionActivity::class.java)
            startActivity(intent)
        }

        if(!SharedPreferencesHelper.instance?.getBoolean(SharedPreferencesKeys.IsAlreadyInstallKey)!!) {
            MoEAnalyticsHelper.setAppStatus(this,AppStatus.UPDATE)
        }
        else {
            MoEAnalyticsHelper.setAppStatus(this,AppStatus.INSTALL)
            SharedPreferencesHelper.instance?.putBoolean(SharedPreferencesKeys.IsAlreadyInstallKey, true)
        }
    }

    private fun getLastShownDayCount(): Long {
        if (SharedPreferencesHelper.instance?.getLong(Constants.PUSH_ASKED_DATE)!!>0){
            val milliseconds =System.currentTimeMillis() - SharedPreferencesHelper.instance?.getLong(Constants.PUSH_ASKED_DATE)!!
            return (milliseconds / (1000 * 60 * 60 * 24))
        }else{
            return 0
        }

    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun requestForLocation() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d("permission","Granted")
        }else{

        }

    }
    private fun registerWithFCM() {
        if (!TextUtils.isEmpty(
                SharedPreferencesHelper.instance!!.getString(SharedPreferencesKeys.FirebaseTokenKey)
                    .toString()
            )
        ) {
            val fcmRegistrationModel = FCMRegistrationModel(
                "fcm_token",
                SharedPreferencesHelper.instance!!.getString(SharedPreferencesKeys.FirebaseTokenKey)
                    .toString(),
                BuildConfig.VERSION_CODE
            )

            homeViewModel.sendFCMToServer(fcmRegistrationModel)

            MoEFireBaseHelper.getInstance()
                .passPushToken(GramAppApplication.getAppContext(), fcmRegistrationModel.token_value)

            Log.d(
                "Raj",
                SharedPreferencesHelper.instance!!.getString(SharedPreferencesKeys.FirebaseTokenKey)
                    .toString()
            )
        }

    }

    private fun setUpFirebaseConfig() {
        FirebaseApp.initializeApp(this)
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(1000)
            .build()
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        mFirebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    mFirebaseRemoteConfig.fetchAndActivate()
                    val google_api_key =
                        mFirebaseRemoteConfig.getString(Constants.RemoteConfigKeys.GOOGLE_API_KEY)
                    instance!!.putString(SharedPreferencesKeys.GOOGLE_API_KEY, google_api_key)
                } else {
                    Log.d("Remote Config Failed", "Failure of Remote Config")
                }
            }

            .addOnFailureListener { exception ->
                Log.d(
                    "remoteConfig",
                    "remoteConfig exception: $exception"
                )
            }
            .addOnCanceledListener {
                Log.d(
                    "remoteConfig",
                    "remoteConfig initAssetList: cancelled "
                )
            }
    }

    override fun setImageNameMobile(
        name: String,
        mobile: String,
        profileImage: String,
        gramCash: String?,
    ) {
        viewDataBinding.navigationlayout.tvName.text = name
        viewDataBinding.navigationlayout.tvContact.text =
            getString(R.string.dialing_code).plus(mobile)
        viewDataBinding.navigationlayout.tvGc.text = gramCash
        profileFragment.refreshProfile()

        GramAppApplication.userInfoMoEngage(this)
        GramAppApplication.userLoginMoEngage(this)
        if (profileImage.isNotNullOrEmpty())
            Glide.with(this)
                .load(profileImage)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .fitCenter()
                .into(viewDataBinding.navigationlayout.ivProfile)
    }

    override fun openNotificationSetting() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val i = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse(
                    "package:$packageName"
                )
            )
            SharedPreferencesHelper.instance?.putBoolean(Constants.PUSH_ASKED, true)
            startActivity(i)
        }
    }

    private fun setupUi() {
        marketFragment = MarketFragment()
        communityFragment = CommunityFragment()
        profileFragment = MyGramophoneFragment()
//        tradeFragment = TradeFragment()
        activeFragment = marketFragment

//        supportFragmentManager.beginTransaction()
//            .add(R.id.fragment_container, tradeFragment, TradeFragment::class.java.simpleName)
//            .hide(tradeFragment).commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container,
                profileFragment,
                MyGramophoneFragment::class.java.simpleName)
            .hide(profileFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.fragment_container,
            communityFragment,
            CommunityFragment::class.java.simpleName).hide(communityFragment).commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, marketFragment, MarketFragment::class.java.simpleName)
            .commit()

        setUpNavigationDrawer()
        viewDataBinding.navView.itemIconTintList = null

        updateMenuItemVisibility(showHomeItems = false, showCommunityItems = true)
        viewDataBinding.navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    viewDataBinding.toolbar.myToolbar.title =
                        "  " + resources.getString(R.string.app_name)
                    updateMenuItemVisibility(showHomeItems = false, showCommunityItems = true)
                    supportFragmentManager.beginTransaction().hide(activeFragment)
                        .show(marketFragment).commit()
                    activeFragment = marketFragment
                    viewDataBinding.llCreateAPost.visibility = GONE
                    viewDataBinding.llCallPost.visibility = VISIBLE

                    return@setOnItemSelectedListener true
                }
                R.id.navigation_community -> {
                    if (hasInternetConnection(this)){
                        viewDataBinding.llCreateAPost.visibility = VISIBLE
                        viewDataBinding.llCallPost.visibility = GONE
                        viewDataBinding.toolbar.myToolbar.title =
                            "  " + resources.getString(R.string.community)
                        updateMenuItemVisibility(showHomeItems = false, showCommunityItems = true)

                        supportFragmentManager.beginTransaction().hide(activeFragment)
                            .show(communityFragment).commit()
                        activeFragment = communityFragment

                        val properties = Properties()
                        properties.addAttribute(
                            "Customer_Id",
                            SharedPreferencesHelper.instance?.getString(
                                SharedPreferencesKeys.CUSTOMER_ID
                            )!!
                        )
                            .addAttribute("Redirection_Source", "Home Tab")
                            .addAttribute("User ID", SharedPreferencesHelper.instance?.getString( SharedPreferencesKeys.UUIdKey)!!)
                        sendMoEngageEvent("KA_View_Community_Wall", properties)
                    }else{
                        openActivity(LostConnectionActivity::class.java)
                    }

                    return@setOnItemSelectedListener true
                }
                R.id.navigation_profile -> {
                    if (hasInternetConnection(this)) {
                        viewDataBinding.toolbar.myToolbar.title =
                            "  " + resources.getString(R.string.my_profile)

                        updateMenuItemVisibility(false, true)
                        supportFragmentManager.beginTransaction().hide(activeFragment)
                            .show(profileFragment).commit()
                        activeFragment = profileFragment
                        viewDataBinding.llCreateAPost.visibility = GONE
                        viewDataBinding.llCallPost.visibility = GONE

                        val properties = Properties()
                        properties.addAttribute(
                            "Customer_Id",
                            SharedPreferencesHelper.instance?.getString(
                                SharedPreferencesKeys.CUSTOMER_ID
                            )!!)
                            .addAttribute("Redirection_Source","Main Navigation")
                            .setNonInteractive()
                        sendMoEngageEvent("KA_View_MyGramophone", properties)

                    }else{
                        openActivity(LostConnectionActivity::class.java)
                    }

                    return@setOnItemSelectedListener true
                }
//                R.id.navigation_trade -> {
//                    viewDataBinding.toolbar.myToolbar.title =
//                        "  " + resources.getString(R.string.vyapar)
//
//                    updateMenuItemVisibility(false, false)
//                    supportFragmentManager.beginTransaction().hide(activeFragment)
//                        .show(tradeFragment).commit()
//                    activeFragment = tradeFragment
//                    viewDataBinding.llCreateAPost.visibility = GONE
//
//                    return@setOnItemSelectedListener true
//                }
            }
            false
        }

        tvAppVersion.text="v-".plus(BuildConfig.VERSION_NAME)
    }

    fun showCommunityFragment(from: String) {
        this.from = from
        viewDataBinding.navView.selectedItemId = R.id.navigation_community
        if (SharedPreferencesHelper.instance?.getBoolean(Constants.TARGET_PAGE_FROM_DEEP_LINK) != true) {
            communityFragment.selectTab(from)
        }
    }

    fun showHomeFragment() {
        viewDataBinding.navView.selectedItemId = R.id.navigation_home
    }

    private fun setUpNavigationDrawer() {
        setUpToolBar(true, resources.getString(R.string.app_name), R.drawable.ic_cart_menu)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        title = "Gram"

        viewDataBinding.toolbar.myToolbar.navigationIcon =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_burger_menu, null)
        viewDataBinding.toolbar.myToolbar.title = "  " + resources.getString(R.string.app_name)
        viewDataBinding.toolbar.myToolbar.logo =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_gramophone_leaf, null)
        //Logo,title
        val actionbar = supportActionBar
        actionbar?.let {
            actionbar.setDisplayHomeAsUpEnabled(true)
        }

        drawer = viewDataBinding.drawerLayout as DrawerLayout

        val toggle = ActionBarDrawerToggle(
            this,
            drawer,
            findViewById<Toolbar>(R.id.myToolbar),
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer?.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()

        viewDataBinding.toolbar.myToolbar.inflateMenu(R.menu.menu_home)
        viewDataBinding.toolbar.myToolbar.setOnMenuItemClickListener { menuItem ->
            onOptionsItemSelected(menuItem)
        }

        viewDataBinding.toolbar.myToolbar.setNavigationOnClickListener {
            drawer?.open()
        }
    }

    private fun updateMenuItemVisibility(showHomeItems: Boolean, showCommunityItems: Boolean) {
        viewDataBinding.toolbar.myToolbar.menu.forEach {
            if (showHomeItems) {
                it.isVisible =
                    (it.itemId == R.id.item_search || it.itemId == R.id.item_favorite || it.itemId == R.id.item_cart)
            } else if (showCommunityItems) {
                it.isVisible =
                    (it.itemId == R.id.item_search || it.itemId == R.id.item_notification || it.itemId == R.id.item_cart)
            } else {
                it.isVisible = it.itemId == R.id.item_search
            }
        }
    }

    fun updateCartCount(cartCount: Int) {
        try {
            if (cartCount > 0) {
                tvCartCount!!.text = cartCount.toString()
                frameCartRedCircle!!.visibility = VISIBLE
            } else {
                frameCartRedCircle!!.visibility = GONE
            }
            rlItemMenuCart.setOnClickListener {
                openActivity(CartActivity::class.java, Bundle().apply {
                    putString(Constants.REDIRECTION_SOURCE, Constants.LANDING_SCREEN)
                })
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun closeDrawer() {
        drawer?.close()
    }

    override fun onClick(p0: View?) {
        openActivity(CartActivity::class.java, Bundle().apply {
            putString(Constants.REDIRECTION_SOURCE, Constants.LANDING_SCREEN)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cart -> {
                openActivity(CartActivity::class.java, Bundle().apply {
                    putString(Constants.REDIRECTION_SOURCE, Constants.LANDING_SCREEN)
                })
            }
            R.id.item_search -> {
                openActivity(GlobalSearchActivity::class.java, Bundle().apply {
                    putBoolean("searchInCommunity", (activeFragment is CommunityFragment))
                })
            }

            R.id.item_notification -> {
                openActivity(NotificationActivity::class.java)
            }
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        if (hasInternetConnection(this)){
            mViewModel?.getProfile()
            mViewModel?.getCrops()
            updateCartCount(SharedPreferencesHelper.instance?.getInteger(SharedPreferencesKeys.CART_ITEM_COUNT)!!)
            marketFragment.marketFragmentViewModel.getFarms()
            profileFragment.refreshProfile()


            if (!TextUtils.isEmpty(SharedPreferencesHelper.instance?.getString(Constants.TARGET_PAGE))) {
                when(SharedPreferencesHelper.instance?.getString(Constants.TARGET_PAGE)){
                    "social"-> {
                        if (SharedPreferencesHelper.instance?.getBoolean(Constants.TARGET_PAGE_FROM_DEEP_LINK) == true){
                            viewDataBinding.navView.selectedItemId = R.id.navigation_community

                            val tab =
                                SharedPreferencesHelper.instance?.getInteger(Constants.TARGET_PAGE_TAB)
                            CoroutineScope(Dispatchers.Main).launch {
//                        delay(1000)
                                communityFragment.selectTabFromDeeplink(tab)
                            }
                        }else{
                            showCommunityFragment("social")

                        }

                    }
                    "market"->{
                        showHomeFragment()
                    }

                    "fav"->{
                        viewDataBinding.navView.selectedItemId = R.id.navigation_profile

                    }
                }

            }

        }


    }


    override fun onRestart() {
        super.onRestart()
        if (SharedPreferencesHelper.instance?.getBoolean(
                SharedPreferencesKeys.LANGUAGE_UPDATE
            ) == true
        ) {
            SharedPreferencesHelper.instance?.putBoolean(
                SharedPreferencesKeys.LANGUAGE_UPDATE, false
            )

            openAndFinishActivity(HomeActivity::class.java, null)

        }
    }

    private fun processDeepLink() {
        if (hasInternetConnection(this) && intent.extras!=null){
            if (!TextUtils.isEmpty(intent.extras?.getString(Constants.URI))) {
                SharedPreferencesHelper.instance?.putString(Constants.URI,intent.extras?.getString(Constants.URI))
                val intent = Intent(this, URLHandlerActivity::class.java)

                intent.putExtra(Constants.URI, SharedPreferencesHelper.instance?.getString(Constants.URI))
                startActivity(intent)
            }
        }

    }

    override fun getLayoutID(): Int {
        return R.layout.activity_home
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): HomeViewModel {
        return homeViewModel
    }

    override fun logout() {
        GramAppApplication.userLogoutMoEngage(this)

        ActivityCompat.finishAffinity(this);
        openAndFinishActivity(LanguageActivity::class.java, null)
    }

    override fun shareApp(intent: Intent) {
        startActivity(Intent.createChooser(intent, getMessage(R.string.share_app_link)));
    }

    fun setToolbarTitle(title: String) {
        viewDataBinding.toolbar.myToolbar.title = title

    }

    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
            finish()
        } else {
            showToast(getString(R.string.click_back_to_exit))
        }
        backPressedTime = System.currentTimeMillis()
    }

    override fun onDestroy() {
        try {
            SharedPreferencesHelper.instance?.putParcelable(
                SharedPreferencesKeys.BANNER_DATA,
                BannerResponse("")
            )

            SharedPreferencesHelper.instance?.putString(SharedPreferencesKeys.SUGGESTED_CROPS, "")


        } catch (e: Exception) {
            e.printStackTrace()
        }

        SharedPreferencesHelper.instance?.putBoolean(
           Constants.TARGET_PAGE_FROM_DEEP_LINK,
            false
        )
        SharedPreferencesHelper.instance?.putInteger(
           Constants.TARGET_PAGE_TAB,
            0
        )
        SharedPreferencesHelper.instance?.putString(Constants.TARGET_PAGE, "")
        SharedPreferencesHelper.instance?.putString(Constants.URI,"")
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        SharedPreferencesHelper.instance?.putString(Constants.TARGET_PAGE,"")
    }
}