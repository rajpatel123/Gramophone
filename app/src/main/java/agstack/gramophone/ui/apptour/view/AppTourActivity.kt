package agstack.gramophone.ui.apptour.view

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ActivityApptourBinding
import agstack.gramophone.ui.apptour.AppTourNavigator
import agstack.gramophone.ui.apptour.adapter.DotIndicatorPager2Adapter
import agstack.gramophone.ui.apptour.viewmodel.AppTourViewModel
import agstack.gramophone.ui.dialog.LanguageBottomSheetFragment
import agstack.gramophone.ui.language.model.LoginBanner
import agstack.gramophone.ui.login.view.LoginActivity
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.get
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_apptour.*


@AndroidEntryPoint
class AppTourActivity :
    BaseActivityWrapper<ActivityApptourBinding, AppTourNavigator, AppTourViewModel>(),
    AppTourNavigator,
    LanguageBottomSheetFragment.LanguageUpdateListener {
    private lateinit var loginBanners: List<LoginBanner>
    private val appTourViewModel: AppTourViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appTourViewModel.setupViewPager()
    }

    override fun getLayoutID(): Int {
        return R.layout.activity_apptour
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): AppTourViewModel {
        return appTourViewModel
    }

    override fun onError(message: String?) {
        Toast.makeText(this@AppTourActivity, message, Toast.LENGTH_LONG).show()

    }

    override fun onSuccess(message: String?) {
        Toast.makeText(this@AppTourActivity, message, Toast.LENGTH_LONG).show()
    }

    override fun onLoading() {
    }

    override fun onHelpClick(number: String) {
        appTourViewModel.onHelpClick()
    }

    override fun onLanguageChangeClick() {
        val bottomSheet = LanguageBottomSheetFragment()
        bottomSheet.setLanguageListener(this)
        bottomSheet.show(
            getSupportFragmentManager(),
            getMessage(R.string.bottomsheet_tag)
        )
    }

    override fun updateImages(currentPage: Int) {
        view_pager.setCurrentItem(currentPage, true)
    }

    override fun setupViewPager(loginBanners: List<LoginBanner>?) {
        this.loginBanners = loginBanners!!
        val adapter = DotIndicatorPager2Adapter(loginBanners)
        view_pager.adapter = adapter

        val zoomOutPageTransformer = ZoomOutPageTransformer()
        view_pager.setPageTransformer { page, position ->
            zoomOutPageTransformer.transformPage(page, position)
        }

        dots_indicator.attachTo(view_pager)


        appTourViewModel.startScroller()
    }



    override fun onLanguageUpdate() {
        appTourViewModel.updateLanguage()
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

    }

}