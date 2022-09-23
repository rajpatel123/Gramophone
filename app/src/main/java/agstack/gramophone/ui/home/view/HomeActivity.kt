package agstack.gramophone.ui.home.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityHomeBinding
import agstack.gramophone.ui.cart.view.CartActivity
import agstack.gramophone.ui.home.navigator.HomeActivityNavigator
import agstack.gramophone.ui.home.view.fragments.community.CommunityFragment
import agstack.gramophone.ui.home.view.fragments.market.MarketFragment
import agstack.gramophone.ui.home.view.fragments.profile.ProfileFragment
import agstack.gramophone.ui.home.view.fragments.trading.TradeFragment
import agstack.gramophone.ui.home.viewmodel.HomeViewModel
import agstack.gramophone.ui.language.view.LanguageActivity
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper.Companion.instance
import agstack.gramophone.utils.SharedPreferencesKeys
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.forEach
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
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

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var mFirebaseRemoteConfig: FirebaseRemoteConfig
    private lateinit var marketFragment: MarketFragment
    private lateinit var communityFragment: CommunityFragment
    private lateinit var profileFragment: ProfileFragment
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

    override fun setImageNameMobile(name: String, mobile: String, profileImage: String) {
        viewDataBinding.navigationlayout.tvName.text = name
        viewDataBinding.navigationlayout.tvContact.text = mobile
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
        profileFragment = ProfileFragment()
        tradeFragment = TradeFragment()
        activeFragment = marketFragment

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, tradeFragment, TradeFragment::class.java.simpleName)
            .hide(tradeFragment).commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, profileFragment, ProfileFragment::class.java.simpleName)
            .hide(profileFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.fragment_container,
            communityFragment,
            CommunityFragment::class.java.simpleName).hide(communityFragment).commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, marketFragment, MarketFragment::class.java.simpleName)
            .commit()

        setUpNavigationDrawer()
        viewDataBinding.navView.itemIconTintList = null

        viewDataBinding.navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    updateMenuItemVisibility(true)
                    supportFragmentManager.beginTransaction().hide(activeFragment)
                        .show(marketFragment).commit()
                    activeFragment = marketFragment
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_community -> {
                    updateMenuItemVisibility(false)
                    supportFragmentManager.beginTransaction().hide(activeFragment)
                        .show(communityFragment).commit()
                    activeFragment = communityFragment
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_profile -> {
                    updateMenuItemVisibility(false)
                    supportFragmentManager.beginTransaction().hide(activeFragment)
                        .show(profileFragment).commit()
                    activeFragment = profileFragment
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_trade -> {
                    updateMenuItemVisibility(false)
                    supportFragmentManager.beginTransaction().hide(activeFragment)
                        .show(tradeFragment).commit()
                    activeFragment = tradeFragment
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
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

        viewDataBinding.toolbar.myToolbar.inflateMenu(R.menu.menu_home_activity)
        viewDataBinding.toolbar.myToolbar.setOnMenuItemClickListener { menuItem ->
            onOptionsItemSelected(menuItem)
        }

        viewDataBinding.toolbar.myToolbar.setNavigationOnClickListener {
            drawer?.open()
        }
    }

    private fun updateMenuItemVisibility(showItems: Boolean) {
        viewDataBinding.toolbar.myToolbar.menu.forEach {
            if (showItems) {
                it.isVisible = true
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
}