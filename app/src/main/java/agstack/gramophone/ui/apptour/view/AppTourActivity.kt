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
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.view.get
import com.amnix.xtension.extensions.childs
import com.amnix.xtension.extensions.setPaddingStart
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_apptour.*
import kotlinx.android.synthetic.main.activity_apptour.llIndicator
import kotlinx.android.synthetic.main.page_indicator.*


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

    override fun getLayoutID(): Int = R.layout.activity_apptour

    override fun getBindingVariable(): Int = BR.viewModel

    override fun getViewModel(): AppTourViewModel = appTourViewModel

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
        appTourViewModel.updateIndicator(currentPage,llIndicator)
    }

    override fun setupViewPager(loginBanners: List<LoginBanner>?) {
        this.loginBanners = loginBanners!!
        appTourViewModel.setPageIndicators(loginBanners)
        val adapter = DotIndicatorPager2Adapter(loginBanners)
        view_pager.adapter = adapter

        val zoomOutPageTransformer = ZoomOutPageTransformer()
        view_pager.setPageTransformer { page, position ->
            zoomOutPageTransformer.transformPage(page, position)
        }

        appTourViewModel.updateIndicator(0,llIndicator)

        appTourViewModel.startScroller()
    }

    override fun addIndicatorView() {
        val pageIndicator =
            LayoutInflater.from(this).inflate(R.layout.page_indicator, null)
        pageIndicator.setPaddingStart(10)
        val layoutParams = LinearLayout.LayoutParams(130, 30)
        layoutParams.setMargins(14, 0, 14, 0)
        llIndicator.addView(pageIndicator,layoutParams)
    }


    override fun onLanguageUpdate() {
        appTourViewModel.updateLanguage()
    }


}