package agstack.gramophone.ui.home.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityHomeBinding
import agstack.gramophone.menu.BottomNavigationView
import agstack.gramophone.menu.OnNavigationItemChangeListener
import agstack.gramophone.ui.cart.view.CartActivity
import agstack.gramophone.ui.home.navigator.HomeActivityNavigator
import agstack.gramophone.ui.home.viewmodel.HomeViewModel
import agstack.gramophone.ui.language.view.LanguageActivity
import agstack.gramophone.ui.order.view.OrderListActivity
import agstack.gramophone.ui.profile.view.ProfileActivity
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper.Companion.instance
import agstack.gramophone.utils.SharedPreferencesKeys
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.forEach
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_home.*

@AndroidEntryPoint
class HomeActivity :
    BaseActivityWrapper<ActivityHomeBinding, HomeActivityNavigator, HomeViewModel>(),
    HomeActivityNavigator, View.OnClickListener {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var mFirebaseRemoteConfig: FirebaseRemoteConfig
    var currentFragmentPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
        setUpFirebaseConfig()

        bottomNav.setActiveNavigationIndex(currentFragmentPosition)

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

    private fun setupUi() {
        setUpNavigationDrawer()

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host
        ) as NavHostFragment
        navController = navHostFragment.navController

        /* If you want to change active navigation item programmatically
               bottomMenu.setActiveNavigationIndex(2)
          */
        bottomNav.setOnNavigationItemChangedListener(object :
            OnNavigationItemChangeListener {
            override fun onNavigationItemChanged(navigationItem: BottomNavigationView.NavigationItem) {
                when (navigationItem.position) {
                    0 -> {
                        currentFragmentPosition = 0
                        navController.navigate(R.id.marketFragment)
                    }
                    1 -> {
                        currentFragmentPosition = 1
                        navController.navigate(R.id.tradeFragment2)
                    }
                    2 -> {
                        currentFragmentPosition = 2
                        navController.navigate(R.id.communityFragment3)
                    }
                    3 -> {
                        openActivity(ProfileActivity::class.java)
                    }
                }
                if (navigationItem.position != 3)
                    updateMenuItemVisiblity(navigationItem.position == 0)

            }
        })
    }

    private fun setUpNavigationDrawer() {
        setUpToolBar(true, resources.getString(R.string.app_name), R.drawable.ic_cart_menu)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setTitle("Gram")





        viewDataBinding.toolbar.myToolbar.navigationIcon =
            ResourcesCompat.getDrawable(getResources(), R.drawable.ic_burger_menu, null)
        viewDataBinding.toolbar.myToolbar.title = "  "+resources.getString(R.string.app_name)
        viewDataBinding.toolbar.myToolbar.logo =
            ResourcesCompat.getDrawable(getResources(), R.drawable.ic_gramophone_leaf, null)
        //Logo,title
        val actionbar = supportActionBar
        actionbar?.let {
            actionbar.setDisplayHomeAsUpEnabled(true)
        }

        val drawer = viewDataBinding.drawerLayout as DrawerLayout

        val toggle = ActionBarDrawerToggle(
            this,
            drawer,
            findViewById<Toolbar>(R.id.myToolbar),
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()

        viewDataBinding.toolbar.myToolbar.inflateMenu(R.menu.menu_home_activity)
        viewDataBinding.toolbar.myToolbar.setOnMenuItemClickListener { menuItem ->
            onOptionsItemSelected(menuItem)
        }

        viewDataBinding.toolbar.myToolbar.setNavigationOnClickListener {
            drawer.open()
        }
    }

    private fun updateMenuItemVisiblity(showItems: Boolean) {
        viewDataBinding.toolbar.myToolbar.menu.forEach {
            it.setVisible(showItems)


        }
    }

    override fun onClick(view: View?) {
        /* when (view?.id) {
             R.id.ivCart -> {
                 openActivity(CartActivity::class.java, null)
             }
             R.id.ivFavourite -> {
                 openActivity(OrderListActivity::class.java, null)
             }
         }*/
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cart -> {
                openActivity(CartActivity::class.java)
            }

            R.id.item_favorite -> {
                openActivity(OrderListActivity::class.java, null)
            }

        }
        return true
    }



    override fun onResume() {
        super.onResume()


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