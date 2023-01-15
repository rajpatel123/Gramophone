package agstack.gramophone.utils

import agstack.gramophone.BuildConfig
import agstack.gramophone.R
import agstack.gramophone.ui.profile.model.GpApiResponseProfileData
import agstack.gramophone.utils.SharedPreferencesHelper.Companion.initializeInstance
import android.app.Application
import android.text.TextUtils
import androidx.appcompat.app.AppCompatDelegate
import com.amnix.xtension.extensions.isNotNull
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.moengage.core.LogLevel
import com.moengage.core.MoECoreHelper
import com.moengage.core.MoEngage
import com.moengage.core.analytics.MoEAnalyticsHelper
import com.moengage.core.config.*
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GramAppApplication : Application() {

    private var moEngageAppId: String? = null
    lateinit var instance : GramAppApplication

    override fun onCreate() {
        super.onCreate()
        initializeInstance(this)
        val langIsoCode = LocaleManagerClass.getLangCodeFromPreferences(this)
        if (langIsoCode != null && !langIsoCode.equals("", ignoreCase = true)) {
            LocaleManagerClass.updateLocale(langIsoCode, resources)
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        initializeMoEngage()

        try {
            configureCrashReporting()
        } catch (e: Exception) {
        }
    }



    private fun configureCrashReporting() {
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
    }
    private fun initializeMoEngage() {

        // this is the instance of the application class and "moEngageAppId" is the APP ID from the dashboard.
        moEngageAppId = if (BuildConfig.DEBUG) {
            "62YMO56GSDKATEJQ1SF3B9OZ"
        } else {
            "62YMO56GSDKATEJQ1SF3B9OZ"
        }

        val moEngage = MoEngage.Builder(this, moEngageAppId!!)
            .configureNotificationMetaData(NotificationConfig(R.drawable.ic_gramophone_leaf,
                R.drawable.ic_gramophone_leaf,
                R.color.color_logo,
                isMultipleNotificationInDrawerEnabled = true,
                isBuildingBackStackEnabled = true,
                isLargeIconDisplayEnabled = true))
            .configureFcm(FcmConfig(false))
            .configureInApps(InAppConfig(null))
            .configureGeofence(GeofenceConfig(true))
            .configureLogs(LogConfig(LogLevel.VERBOSE, false))
            .build()
        MoEngage.initialiseDefaultInstance(moEngage)

       userInfoMoEngage()



    }


    fun userLogoutMoEngage() {
        MoECoreHelper.logoutUser(this)
    }

    private fun userInfoMoEngage() {
        val profileData =
            SharedPreferencesHelper.instance?.getParcelable(SharedPreferencesKeys.PROFILE_DATA,
                GpApiResponseProfileData::class.java)
        if (profileData.isNotNull()) {
            MoEAnalyticsHelper.setFirstName(this, profileData?.first_name!!)
            if (!TextUtils.isEmpty(profileData.last_name)){
                MoEAnalyticsHelper.setLastName(this, profileData.last_name!!)
            }
            MoEAnalyticsHelper.setUserName(this,
                SharedPreferencesHelper.instance?.getString(SharedPreferencesKeys.USERNAME)!!)
            MoEAnalyticsHelper.setLocation(this, 0.0, 0.0)
            MoEAnalyticsHelper.setMobileNumber(this, profileData.mobile_no!!)
        }
    }
}
