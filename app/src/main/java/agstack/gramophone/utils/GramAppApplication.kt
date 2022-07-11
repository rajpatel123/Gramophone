package agstack.gramophone.utils

import agstack.gramophone.utils.SharedPreferencesHelper.Companion.initializeInstance
import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GramAppApplication : Application() {

    companion object {
    }

    override fun onCreate() {
        super.onCreate()
        initializeInstance(this)
        val langIsoCode = LocaleManagerClass.getLangCodeFromPreferences(this)
        if (langIsoCode != null && !langIsoCode.equals("", ignoreCase = true)) {
            LocaleManagerClass.updateLocale(langIsoCode, resources)
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}
