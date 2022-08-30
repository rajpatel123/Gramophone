package agstack.gramophone.ui.referandearn.aboutreferralpoints

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.AboutReferralPointsActivityBinding
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AboutReferralPointsBalanceActivity :
    BaseActivityWrapper<AboutReferralPointsActivityBinding, AboutReferralPointsBalanceNavigator, AboutReferralPointsBalanceViewModel>(),
    AboutReferralPointsBalanceNavigator {

    private val aboutreferralpointsViewModel: AboutReferralPointsBalanceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar(true, resources.getString(R.string.about_referral_point_balance), R.drawable.ic_arrow_left)
    }
    override fun getLayoutID(): Int {
        return R.layout.about_referral_points_activity
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): AboutReferralPointsBalanceViewModel {
        return aboutreferralpointsViewModel
    }

}