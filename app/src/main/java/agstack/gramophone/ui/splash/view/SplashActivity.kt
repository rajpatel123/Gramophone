package agstack.gramophone.ui.splash.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivitySplashBinding
import agstack.gramophone.ui.home.view.HomeActivity
import agstack.gramophone.ui.language.view.LanguageActivity
import agstack.gramophone.ui.splash.SplashNavigator
import agstack.gramophone.ui.splash.viewmodel.SplashViewModel
import agstack.gramophone.utils.Utils
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_splash.*
import org.json.JSONObject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivityWrapper<ActivitySplashBinding,SplashNavigator,SplashViewModel>(), SplashNavigator {
    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startApp()
    }

    private fun startApp() {
        splashViewModel.initSplash()
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_splash
    }

    override fun getBindingVariable(): Int {
       return BR.viewModel
    }

    override fun getViewModel(): SplashViewModel {
        return splashViewModel
    }

    override fun moveToLogIn() {
       openAndFinishActivity(LanguageActivity::class.java,null)

    }

    override fun moveTOHome() {
        openAndFinishActivity(HomeActivity::class.java,null)
    }
}