package agstack.gramophone.ui.home.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityHomeBinding
import agstack.gramophone.menu.BottomNavigationView
import agstack.gramophone.menu.OnNavigationItemChangeListener
import agstack.gramophone.ui.home.navigator.HomeActivityNavigator
import agstack.gramophone.ui.home.view.fragments.community.CommunityFragment
import agstack.gramophone.ui.home.view.fragments.market.MarketFragment
import agstack.gramophone.ui.home.view.fragments.trading.TradeFragment
import agstack.gramophone.ui.home.viewmodel.HomeViewModel
import agstack.gramophone.ui.profile.view.ProfileActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_home.*

@AndroidEntryPoint
class HomeActivity :
    BaseActivityWrapper<ActivityHomeBinding, HomeActivityNavigator, HomeViewModel>(),
    HomeActivityNavigator {
    private lateinit var binding: ActivityHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUi()
    }

    private fun setupUi() {
        /* val navHostFragment = supportFragmentManager.findFragmentById(
             R.id.nav_host_container
         ) as NavHostFragment
         navController = navHostFragment.navController

         bottom_nav.setOnNavigationItemChangedListener(object :
             OnNavigationItemChangeListener {
             override fun onNavigationItemChanged(navigationItem: BottomNavigationView.NavigationItem) {
                 navController.navigateUp()
                 *//*when (navigationItem.position) {
                    0 -> {
                        navController.navigate(R.id.marketFragment2)
                    }
                    1 -> {
                        navController.navigate(R.id.tradeFragment)
                    }
                    2 -> {
                        navController.navigate(R.id.communityFragment)
                    }
                    3 -> {
                        ProfileActivity.start(this@HomeActivity)
                    }
                }*//*
            }
        })
        *//* If you want to change active navigation item programmatically
             bottomMenu.setActiveNavigationIndex(2)
        */
        replaceFragment(MarketFragment(), MarketFragment::class.java.simpleName)
        bottom_nav.setOnNavigationItemChangedListener(object :
            OnNavigationItemChangeListener {
            override fun onNavigationItemChanged(navigationItem: BottomNavigationView.NavigationItem) {
                when (navigationItem.position) {
                    0 -> {
                        replaceFragment(MarketFragment(), MarketFragment::class.java.simpleName)
                    }
                    1 -> {
                        replaceFragment(TradeFragment(), TradeFragment::class.java.simpleName)
                    }
                    2 -> {
                        replaceFragment(CommunityFragment(), CommunityFragment::class.java.simpleName)
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