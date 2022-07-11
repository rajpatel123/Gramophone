package agstack.gramophone.utils

import agstack.gramophone.utils.SharedPreferencesHelper.Companion.initializeInstance
import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GramAppApplication : Application() {

    companion object {
    }

    override fun onCreate() {
        super.onCreate()
        initializeInstance(this)
    }
}
