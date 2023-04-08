package agstack.gramophone.ui.home.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityHomeBinding
import agstack.gramophone.databinding.ActivityNoInternetConnectionBinding
import agstack.gramophone.ui.home.navigator.HomeActivityNavigator
import agstack.gramophone.ui.home.viewmodel.HomeViewModel
import agstack.gramophone.ui.splash.viewmodel.SplashViewModel
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LostConnection :
    BaseActivityWrapper<ActivityNoInternetConnectionBinding, HomeActivityNavigator, HomeViewModel>(),
    HomeActivityNavigator, View.OnClickListener {
    private val homeViewModel: HomeViewModel by viewModels()
    override fun getLayoutID(): Int {
        return R.layout.activity_no_internet_connection
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): HomeViewModel {
       return homeViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun logout() {
        TODO("Not yet implemented")
    }

    override fun shareApp(intent: Intent) {
        TODO("Not yet implemented")
    }

    override fun closeDrawer() {
        TODO("Not yet implemented")
    }

    override fun setImageNameMobile(
        name: String,
        mobile: String,
        profileImage: String,
        gramCash: String?
    ) {
        TODO("Not yet implemented")
    }

    override fun openNotificationSetting() {
        TODO("Not yet implemented")
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}
