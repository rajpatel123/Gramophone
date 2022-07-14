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
import agstack.gramophone.ui.language.model.languagelist.Language
import agstack.gramophone.ui.language.viewmodel.LanguageViewModel
import agstack.gramophone.utils.ApiResponse
import android.content.res.Resources
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
        getLanguageList()
    }

    private fun getLanguageList() {
        languageViewModel.getLanguageList()
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
        rvLanguage?.setHasFixedSize(true)
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

    override fun getResource(): Resources {
        return resources
    }

    override fun initiateApp() {
        languageViewModel.initiateAppData(initiateAppDataRequestModel)
    }

    override fun onError(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess(message: String?) {
    }

    override fun onLoading() {
        TODO("Not yet implemented")
    }

    override fun updateLanguageList(languageAdapter: LanguageAdapter,onLanguageClicked: (Language) -> Unit) {
        languageAdapter.selectedLanguage = onLanguageClicked
        rvLanguage.adapter=languageAdapter

    }

}