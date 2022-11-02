package agstack.gramophone.ui.home.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityHomeBinding
import agstack.gramophone.ui.cart.view.CartActivity
import agstack.gramophone.ui.home.navigator.HomeActivityNavigator
import agstack.gramophone.ui.home.view.fragments.community.CommunityFragment
import agstack.gramophone.ui.home.view.fragments.gramophone.MyGramophoneFragment
import agstack.gramophone.ui.home.view.fragments.market.MarketFragment
import agstack.gramophone.ui.home.view.fragments.trading.TradeFragment
import agstack.gramophone.ui.home.viewmodel.HomeViewModel
import agstack.gramophone.ui.language.view.LanguageActivity
import agstack.gramophone.ui.search.view.GlobalSearchActivity
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper.Companion.instance
import agstack.gramophone.utils.SharedPreferencesKeys
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity :
    BaseActivityWrapper<ActivityHomeBinding, HomeActivityNavigator, HomeViewModel>(),
    HomeActivityNavigator {

    var from: String = "home"
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var mFirebaseRemoteConfig: FirebaseRemoteConfig
    private lateinit var marketFragment: MarketFragment
    private lateinit var communityFragment: CommunityFragment
    private lateinit var profileFragment: MyGramophoneFragment
    private lateinit var tradeFragment: TradeFragment
    private lateinit var activeFragment: Fragment
    var drawer: DrawerLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
        setUpFirebaseConfig()

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

    override fun setImageNameMobile(name: String, mobile: String, profileImage: String,gramCash:String?) {
        viewDataBinding.navigationlayout.tvName.text = name
        viewDataBinding.navigationlayout.tvContact.text =
            getString(R.string.dialing_code).plus(mobile)
        viewDataBinding.navigationlayout.tvGc.text = gramCash
        profileFragment.refreshProfile()
        if (profileImage.isNotNullOrEmpty())
        Glide.with(this)
            .load(profileImage)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .fitCenter()
            .into(viewDataBinding.navigationlayout.ivProfile)
    }

    private fun setupUi() {
        marketFragment = MarketFragment()
        communityFragment = CommunityFragment()
        profileFragment = MyGramophoneFragment()
        tradeFragment = TradeFragment()
        activeFragment = marketFragment

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, tradeFragment, TradeFragment::class.java.simpleName)
            .hide(tradeFragment).commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, profileFragment, MyGramophoneFragment::class.java.simpleName)
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
                    viewDataBinding.llCreateAPost.visibility= GONE

                    return@setOnItemSelectedListener true
                }
                R.id.navigation_community -> {
                    viewDataBinding.llCreateAPost.visibility=VISIBLE
                    viewDataBinding.toolbar.myToolbar.title =
                        "  " + resources.getString(R.string.community)
                    updateMenuItemVisibility(showHomeItems = false, showCommunityItems = true)

                    supportFragmentManager.beginTransaction().hide(activeFragment)
                        .show(communityFragment).commit()
                    activeFragment = communityFragment
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_profile -> {
                    viewDataBinding.toolbar.myToolbar.title =
                        "  " + resources.getString(R.string.my_profile)

                    updateMenuItemVisibility(false, true)
                    supportFragmentManager.beginTransaction().hide(activeFragment)
                        .show(profileFragment).commit()
                    activeFragment = profileFragment
                    viewDataBinding.llCreateAPost.visibility= GONE

                    return@setOnItemSelectedListener true
                }
                R.id.navigation_trade -> {
                    viewDataBinding.toolbar.myToolbar.title =
                        "  " + resources.getString(R.string.vyapar)

                    updateMenuItemVisibility(false, false)
                    supportFragmentManager.beginTransaction().hide(activeFragment)
                        .show(tradeFragment).commit()
                    activeFragment = tradeFragment
                    viewDataBinding.llCreateAPost.visibility= GONE

                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    fun showCommunityFragment(from:String) {
        this.from = from
        viewDataBinding.navView.selectedItemId = R.id.navigation_community
        communityFragment.selectTab(from)
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

    override fun closeDrawer() {
        drawer?.close()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cart -> {
                openActivity(CartActivity::class.java)
            }
            R.id.item_search -> {
                openActivity(GlobalSearchActivity::class.java, Bundle().apply {
                    putBoolean("searchInCommunity", (activeFragment is CommunityFragment))
                })
            }
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        mViewModel?.getProfile()
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
        ActivityCompat.finishAffinity(this);
        openAndFinishActivity(LanguageActivity::class.java, null)
    }

    override fun shareApp(intent: Intent) {
        startActivity(Intent.createChooser(intent, getMessage(R.string.share_app_link)));
    }

    fun setToolbarTitle(title:String){
        viewDataBinding.toolbar.myToolbar.title = title

    }
}