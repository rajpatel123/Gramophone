package agstack.gramophone.ui.apptour

import agstack.gramophone.base.BaseNavigator
import agstack.gramophone.ui.language.model.LoginBanner
import android.os.Bundle
import android.view.View

interface AppTourNavigator : BaseNavigator {
    fun onLanguageChangeClick()
    fun updateImages(currentPage: Int)
    fun setupViewPager(loginBannerList: List<LoginBanner>?)
    fun addIndicatorView()
    fun updateImage(currentPage: Int)
    fun sendMoEngageEvent(isLoginClicked: Boolean)
}
