package agstack.gramophone.ui.apptour

import agstack.gramophone.base.BaseNavigator
import android.os.Bundle

interface AppTourNavigator : BaseNavigator{
    fun onHelpClick(bundle: String)
    fun onLanguageChangeClick()
}
