package agstack.gramophone.ui.referandearn.referralpoints

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ReferEarnActivityBinding
import agstack.gramophone.databinding.ReferralPointsActivityBinding
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReferralPointsActivity :
    BaseActivityWrapper<ReferralPointsActivityBinding, ReferralPointsNavigator, ReferralPointsViewModel>(),
    ReferralPointsNavigator {

    private val referralpointsViewModel: ReferralPointsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar(true, resources.getString(R.string.referral_points), R.drawable.ic_arrow_left)
    }
    override fun getLayoutID(): Int {
        return R.layout.referral_points_activity
    }

    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): ReferralPointsViewModel {
        return referralpointsViewModel
    }

}