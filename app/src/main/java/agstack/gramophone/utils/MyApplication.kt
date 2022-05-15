package agstack.gramophone.utils

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex

class MyApplication : Application() {

    companion object {
        private const val ONESIGNAL_APP_ID = "4b51d526-b5e1-433a-87d6-f07c2490e0e9"
    }

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

    }
}
