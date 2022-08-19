package agstack.gramophone.ui.apptour

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.language.model.LoginBanner
import android.os.Bundle

interface AppTourNavigator : BaseNavigator{
    fun onHelpClick(bundle: String)
    fun onLanguageChangeClick()
    fun updateImages(currentPage: Int)
    fun setupViewPager(loginBannerList: List<LoginBanner>?)
}
