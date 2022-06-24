package agstack.gramophone.ui.home.view

import agstack.gramophone.R
import agstack.gramophone.base.BaseActivity
import agstack.gramophone.databinding.ActivityHomeBinding
import agstack.gramophone.menu.BottomNavigationView
import agstack.gramophone.menu.OnNavigationItemChangeListener
import agstack.gramophone.ui.home.navigator.HomeActivityNavigator
import agstack.gramophone.ui.home.viewmodel.HomeViewModel
import agstack.gramophone.ui.profile.view.ProfileActivity
import agstack.gramophone.ui.profileselection.view.ProfileSelectionActivity
import agstack.gramophone.ui.verifyotp.view.VerifyOtpActivity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding,HomeActivityNavigator,HomeViewModel>(),HomeActivityNavigator {

    private lateinit var binding: ActivityHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUi()
        setupObservers()
    }



    private fun setupUi() {
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_container
        ) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNav.setOnNavigationItemChangedListener(object : OnNavigationItemChangeListener {
            override fun onNavigationItemChanged(navigationItem: BottomNavigationView.NavigationItem) {

                when (navigationItem.position) {
                    0-> {
                        navController.navigate(R.id.marketFragment)
                    }
                    1-> {
                        navController.navigate(R.id.tradeFragment2)
                    }
                    2-> {
                        navController.navigate(R.id.communityFragment3)
                    }
                    3-> {
                        ProfileActivity.start(this@HomeActivity)
                    }
                }
            }
        })
        /* If you want to change active navigation item programmatically
         * bottomMenu.setActiveNavigationIndex(2)
         */
    }

    private fun setupObservers() {

    }

    override fun getLayoutID(): Int {
        TODO("Not yet implemented")
    }

    override fun getBindingVariable(): Int {
        TODO("Not yet implemented")
    }

    override fun getViewModel(): HomeViewModel {
        TODO("Not yet implemented")
    }
}