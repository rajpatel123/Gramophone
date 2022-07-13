package agstack.gramophone.ui.language.view

import agstack.gramophone.BR
import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityLanguageBinding
import agstack.gramophone.ui.apptour.view.AppTourActivity
import agstack.gramophone.ui.language.LanguageActivityNavigator
import agstack.gramophone.ui.language.adapter.LanguageAdapter
import agstack.gramophone.ui.language.model.DeviceDetails
import agstack.gramophone.ui.language.model.InitiateAppDataRequestModel
import agstack.gramophone.ui.language.model.LanguageData
import agstack.gramophone.ui.language.viewmodel.LanguageViewModel
import agstack.gramophone.ui.splash.view.SplashActivity
import agstack.gramophone.utils.ApiResponse
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_language.*

@AndroidEntryPoint
class LanguageActivity : BaseActivityWrapper<ActivityLanguageBinding, LanguageActivityNavigator, LanguageViewModel>(),
    LanguageActivityNavigator {

    private val languageViewModel: LanguageViewModel by viewModels()

    val initiateAppDataRequestModel: InitiateAppDataRequestModel
        get() {
            val android_id = Settings.Secure.getString(this.contentResolver,
                Settings.Secure.ANDROID_ID)

            var deviceDetails = DeviceDetails(
                BuildConfig.VERSION_CODE.toString(),
                BuildConfig.VERSION_NAME,
                android_id,
                Build.MODEL,
                Build.VERSION.SDK_INT.toString()
            )
            var registerDeviceRequestModel = InitiateAppDataRequestModel(deviceDetails,getLanguage(),)



            return registerDeviceRequestModel

        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
        setupDeviceTokenObserver()

        getDeviceToken()
    }

    private fun getDeviceToken() {
        languageViewModel.initiateAppData(initiateAppDataRequestModel)
    }

    private fun setupDeviceTokenObserver() {
      languageViewModel.registerDeviceModel.observe(this, Observer {
          when (it) {
              is ApiResponse.Success -> { }
              is ApiResponse.Error -> {
                  it.message?.let { message ->
                      Toast.makeText(this@LanguageActivity, message, Toast.LENGTH_LONG).show()
                  }
              }
              is ApiResponse.Loading -> {
              }
          }
      })

    }


    private fun setupUi() {
        val languageList = ArrayList<LanguageData>()
        languageList.add(LanguageData("English", "English", true))
        languageList.add(LanguageData("हिंदी", "Hindi", false))
        languageList.add(LanguageData("मराठी", "Marathi", false))
        languageList.add(LanguageData("ગુજરાતી", "Gujrati", false))

        recycler_language?.setHasFixedSize(true)
        recycler_language?.adapter = LanguageAdapter(languageList)

    }
    override fun getLayoutID(): Int {
        return R.layout.activity_language
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): LanguageViewModel {
        return languageViewModel
    }

    override fun moveToNext() {
        openAndFinishActivity(AppTourActivity::class.java,null)
    }

}