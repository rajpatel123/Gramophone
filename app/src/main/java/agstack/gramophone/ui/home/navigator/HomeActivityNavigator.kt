package agstack.gramophone.ui.home.navigator

import agstack.gramophone.base.BaseNavigator
import android.content.Intent

interface HomeActivityNavigator :BaseNavigator {

    fun logout()
    fun shareApp(intent: Intent)
    fun closeDrawer()
}