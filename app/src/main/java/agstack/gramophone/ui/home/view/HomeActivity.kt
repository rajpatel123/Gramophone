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
import agstack.gramophone.ui.order.view.OrderListActivity
import agstack.gramophone.ui.profile.view.ProfileActivity
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper.Companion.instance
import agstack.gramophone.utils.SharedPreferencesKeys
import android.os.Bundle
import android.view.View
import android.util.Log
import androidx.activity.viewModels
import androidx.navigation.NavController
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.toolbar_home.*

@AndroidEntryPoint
class HomeActivity :
    BaseActivityWrapper<ActivityHomeBinding, HomeActivityNavigator, HomeViewModel>(),
    HomeActivityNavigator, View.OnClickListener {
    private lateinit var binding: ActivityHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var mFirebaseRemoteConfig: FirebaseRemoteConfig
    var currentFragmentPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
        setUpFirebaseConfig()


    }

    private fun setUpFirebaseConfig() {
        FirebaseApp.initializeApp(this)
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        var configSettings = FirebaseRemoteConfigSettings.Builder()
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
            }else {
                Log.d("Remote Config Failed","Failure of Remote Config")
                }
            }

            .addOnFailureListener { exception ->Log.d("remoteConfig","remoteConfig exception: $exception") }
            .addOnCanceledListener {Log.d("remoteConfig","remoteConfig initAssetList: cancelled ") }

        }



    override fun onResume() {
        super.onResume()
        // If you want to change active navigation item programmatically
        bottom_nav.setActiveNavigationIndex(currentFragmentPosition)
    }

    private fun setupUi() {
        iv_cart.setOnClickListener(this)
        iv_favourite.setOnClickListener(this)
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host
        ) as NavHostFragment
        navController = navHostFragment.navController

        bottom_nav.setOnNavigationItemChangedListener(object :
            OnNavigationItemChangeListener {
            override fun onNavigationItemChanged(navigationItem: BottomNavigationView.NavigationItem) {
                navController.navigateUp()
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
                        currentFragmentPosition = 0
                        ProfileActivity.start(this@HomeActivity)
                    }
                }
            }
        })
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.iv_cart -> {
                openActivity(CartActivity::class.java, null)
            }
            R.id.iv_favourite -> {
                openActivity(OrderListActivity::class.java, null)
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
}