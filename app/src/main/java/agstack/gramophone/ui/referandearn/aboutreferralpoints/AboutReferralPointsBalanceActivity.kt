package agstack.gramophone.ui.referandearn.aboutreferralpoints

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.AboutReferralPointsActivityBinding
import agstack.gramophone.ui.referandearn.model.ReferralPointsItem
import agstack.gramophone.utils.Constants
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
        val bundle = getBundle()
        bundle?.let {
            if (bundle.getParcelableArrayList<ReferralPointsItem>(Constants.ReferralPointBalanceData) != null) {
                mViewModel?.ReferralPointsBalanceDataFromBundle?.set(bundle.getParcelable(Constants.ReferralPointBalanceData))
               mViewModel?.setAdapter()
            }
        }
    }

    fun getBundle(): Bundle? {
        return intent.extras
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