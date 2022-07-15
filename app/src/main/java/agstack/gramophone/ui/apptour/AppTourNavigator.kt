package agstack.gramophone.ui.apptour

import agstack.gramophone.base.BaseNavigator

interface AppTourNavigator : BaseNavigator{
    fun onHelpClick(bundle: String)
    fun onLanguageChangeClick()
}
