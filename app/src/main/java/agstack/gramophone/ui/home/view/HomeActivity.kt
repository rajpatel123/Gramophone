package agstack.gramophone.ui.home.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityHomeBinding
import agstack.gramophone.ui.cart.view.CartActivity
import agstack.gramophone.ui.home.navigator.HomeActivityNavigator
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
import androidx.core.view.forEach
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
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

    private fun setupUi() {
        setUpNavigationDrawer()
        viewDataBinding.navView.itemIconTintList = null
        val navController = findNavController(R.id.nav_host)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home, R.id.navigation_community, R.id.navigation_profile, R.id.navigation_trade))
        setupActionBarWithNavController(navController, appBarConfiguration)*/
        viewDataBinding.navView.setupWithNavController(navController)

        /*if (navigationItem.position != 3)
            updateMenuItemVisiblity(navigationItem.position == 0)*/
    }

    private fun setUpNavigationDrawer() {
        setUpToolBar(true, resources.getString(R.string.app_name), R.drawable.ic_cart_menu)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        title = "Gram"

        viewDataBinding.toolbar.myToolbar.navigationIcon =
            resources.getDrawable(R.drawable.ic_logo_toolbar_logo)
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

    private fun updateMenuItemVisiblity(showItems: Boolean) {
        viewDataBinding.toolbar.myToolbar.menu.forEach {
            it.setVisible(showItems)


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
        // If you want to change active navigation item programmatically

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