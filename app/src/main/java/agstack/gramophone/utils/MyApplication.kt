package agstack.gramophone.utils

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    companion object {
    }

    override fun onCreate() {
        super.onCreate()

    }
}
