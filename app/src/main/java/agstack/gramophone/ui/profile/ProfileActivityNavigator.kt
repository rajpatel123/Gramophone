package agstack.gramophone.ui.profile

import agstack.gramophone.base.BaseNavigator
import android.content.Intent

interface ProfileActivityNavigator:BaseNavigator {
    fun logout()
    fun shareApp(intent: Intent)
}