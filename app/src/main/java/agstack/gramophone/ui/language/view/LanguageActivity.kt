package agstack.gramophone.ui.language.view

import agstack.gramophone.BuildConfig
import agstack.gramophone.base.BaseActivity
import agstack.gramophone.databinding.ActivityHomeBinding
import agstack.gramophone.databinding.ActivityLanguageBinding
import agstack.gramophone.ui.home.navigator.HomeActivityNavigator
import agstack.gramophone.ui.home.viewmodel.HomeViewModel
import agstack.gramophone.ui.language.LanguageActivityNavigator
import agstack.gramophone.ui.language.adapter.LanguageAdapter
import agstack.gramophone.ui.language.model.LanguageData
import agstack.gramophone.ui.language.model.RegistrerDeviceRquestModel
import agstack.gramophone.ui.language.viewmodel.LanguageViewModel
import agstack.gramophone.ui.splash.view.SplashActivity
import agstack.gramophone.utils.Resource
import agstack.gramophone.utils.SharedPreferencesHelper
import agstack.gramophone.utils.SharedPreferencesKeys
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
class LanguageActivity : BaseActivity<ActivityLanguageBinding, LanguageActivityNavigator, LanguageViewModel>(),
    LanguageActivityNavigator {

    private lateinit var binding: ActivityLanguageBinding
    private val languageViewModel: LanguageViewModel by viewModels()


    val registrerDeviceRquestModel: RegistrerDeviceRquestModel
        get() {
            val android_id = Settings.Secure.getString(this.contentResolver,
                Settings.Secure.ANDROID_ID)



            val manufacturer = Build.MANUFACTURER
            val model = Build.MODEL
            val version = Build.VERSION.SDK_INT
            val versionName = BuildConfig.VERSION_NAME
            val versionCode = BuildConfig.VERSION_CODE
            val oneSignalToken = "testtoken"  // will be updated later
            val fcmToken = "dfd"+SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.FirebaseTokenKey)  // will be updated later


            var registrerDeviceRquestModel = RegistrerDeviceRquestModel(
                versionCode,
                versionName,
                android_id,
                model,
                oneSignalToken,
                version,
                fcmToken
            )


            return registrerDeviceRquestModel

        }
    companion object {
        fun start(activity: SplashActivity) {
            val intent = Intent(activity, LanguageActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUi()
        setupDeviceTokenObserver()

        getDeviceToken()
    }

    private fun getDeviceToken() {
        languageViewModel.getDeviceToken(registrerDeviceRquestModel)
    }

    private fun setupDeviceTokenObserver() {
      languageViewModel.registerDeviceModel.observe(this, Observer {
          when (it) {
              is Resource.Success -> { }
              is Resource.Error -> {
                  it.message?.let { message ->
                      Toast.makeText(this@LanguageActivity, message, Toast.LENGTH_LONG).show()
                  }
              }
              is Resource.Loading -> {
              }
          }
      })
    }


    private fun setupUi() {
        /* binding?.recyclerLanguage?.layoutManager = GridLayoutManager(this, 2)
         binding?.recyclerLanguage?.setHasFixedSize(true)
         binding?.recyclerLanguage?.adapter = LanguageAdapter()*/
        val languageList = ArrayList<LanguageData>()
        languageList.add(LanguageData("English", "English", true))
        languageList.add(LanguageData("हिंदी", "Hindi", false))
        languageList.add(LanguageData("मराठी", "Marathi", false))
        languageList.add(LanguageData("ગુજરાતી", "Gujrati", false))

        recycler_language?.setHasFixedSize(true)
        recycler_language?.adapter = LanguageAdapter(languageList)
    }
    override fun getLayoutID(): Int {
        TODO("Not yet implemented")
    }

    override fun getBindingVariable(): Int {
        TODO("Not yet implemented")
    }

    override fun getViewModel(): LanguageViewModel {
        TODO("Not yet implemented")
    }

}