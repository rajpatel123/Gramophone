package agstack.gramophone.ui.home.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityHomeBinding
import agstack.gramophone.menu.BottomNavigationView
import agstack.gramophone.menu.OnNavigationItemChangeListener
import agstack.gramophone.ui.home.navigator.HomeActivityNavigator
import agstack.gramophone.ui.home.product.activity.ProductDetailsActivity
import agstack.gramophone.ui.home.view.fragments.community.CommunityFragment
import agstack.gramophone.ui.home.view.fragments.market.MarketFragment
import agstack.gramophone.ui.home.view.fragments.market.model.ProductData
import agstack.gramophone.ui.home.view.fragments.trading.TradeFragment
import agstack.gramophone.ui.home.viewmodel.HomeViewModel
import agstack.gramophone.ui.profile.view.ProfileActivity
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.SharedPreferencesHelper.Companion.instance
import agstack.gramophone.utils.SharedPreferencesKeys
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.navigation.NavController
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_home.*

@AndroidEntryPoint
class HomeActivity :
    BaseActivityWrapper<ActivityHomeBinding, HomeActivityNavigator, HomeViewModel>(),
    HomeActivityNavigator {
    private lateinit var binding: ActivityHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var mFirebaseRemoteConfig: FirebaseRemoteConfig

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



    private fun setupUi() {

        /* If you want to change active navigation item programmatically
               bottomMenu.setActiveNavigationIndex(2)
          */
        replaceFragment(MarketFragment(), MarketFragment::class.java.simpleName)
        bottom_nav.setOnNavigationItemChangedListener(object :
            OnNavigationItemChangeListener {
            override fun onNavigationItemChanged(navigationItem: BottomNavigationView.NavigationItem) {
                when (navigationItem.position) {
                    0 -> {
                       replaceFragment(MarketFragment(), MarketFragment::class.java.simpleName)
                       /* openActivity(ProductDetailsActivity::class.java,Bundle().apply {
                            putParcelable("product", ProductData(700322))
                        })*/
                    }
                    1 -> {
                        replaceFragment(TradeFragment(), TradeFragment::class.java.simpleName)
                    }
                    2 -> {
                        replaceFragment(
                            CommunityFragment(),
                            CommunityFragment::class.java.simpleName
                        )
                    }
                    3 -> {
                        ProfileActivity.start(this@HomeActivity)
                    }
                }

            }
        })
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