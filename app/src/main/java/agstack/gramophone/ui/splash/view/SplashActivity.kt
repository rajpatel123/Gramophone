package agstack.gramophone.ui.splash.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivitySplashBinding
import agstack.gramophone.ui.home.view.HomeActivity
import agstack.gramophone.ui.language.view.LanguageActivity
import agstack.gramophone.ui.splash.SplashNavigator
import agstack.gramophone.ui.splash.viewmodel.SplashViewModel
import agstack.gramophone.utils.Constants
import agstack.gramophone.utils.LocaleManagerClass
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivityWrapper<ActivitySplashBinding,SplashNavigator,SplashViewModel>(), SplashNavigator {
    private val splashViewModel: SplashViewModel by viewModels()

    private lateinit var referrerClient: InstallReferrerClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startApp()
    }

    private fun startApp() {
        if (SharedPreferencesHelper.instance?.getBoolean(Constants.UTM_SOURCE_UPDATED) == true){
            splashViewModel.initSplash(resources)
        }else{
            referrerClient = InstallReferrerClient.newBuilder(this).build()
            referrerClient.startConnection(object : InstallReferrerStateListener {

                override fun onInstallReferrerSetupFinished(responseCode: Int) {
                    when (responseCode) {
                        InstallReferrerClient.InstallReferrerResponse.OK -> {
                            val response: ReferrerDetails = referrerClient.installReferrer
                            val referrerUrl: String = response.installReferrer
                            val referrerClickTime: Long = response.referrerClickTimestampSeconds
                            val appInstallTime: Long = response.installBeginTimestampSeconds
                            val instantExperienceLaunched: Boolean = response.googlePlayInstantParam

                            Log.d("Raj",referrerUrl)
                            Log.d("Raj","".plus(referrerClickTime))
                            Log.d("Raj","".plus(appInstallTime))
                            Log.d("Raj","".plus(instantExperienceLaunched))

                            SharedPreferencesHelper.instance?.putString(SharedPreferencesKeys.UTM_SOURCE,referrerUrl)
                            referrerClient.endConnection()//end connection to avoid leaks and smooth transition in app
                            SharedPreferencesHelper.instance?.putBoolean(Constants.UTM_SOURCE_UPDATED,true)

                            splashViewModel.initSplash(resources)
                        }
                        InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                            // API not available on the current Play Store app.
                        }
                        InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                            // Connection couldn't be established.
                        }
                    }
                }

                override fun onInstallReferrerServiceDisconnected() {
                    // Try to restart the connection on the next request to
                    // Google Play by calling the startConnection() method.
                }
            })

        }

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