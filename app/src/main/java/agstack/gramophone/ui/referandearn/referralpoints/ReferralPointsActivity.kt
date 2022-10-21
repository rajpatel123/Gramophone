package agstack.gramophone.ui.referandearn.referralpoints

import agstack.gramophone.BR
import agstack.gramophone.R
import agstack.gramophone.base.BaseActivityWrapper
import agstack.gramophone.databinding.ReferralPointsActivityBinding
import agstack.gramophone.utils.Constants
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import agstack.gramophone.ui.referandearn.model.GpApiResponseData
import agstack.gramophone.utils.IntentKeys
import agstack.gramophone.utils.ShareSheetPresenter
import agstack.gramophone.utils.Utility
import android.net.Uri

@AndroidEntryPoint
class ReferralPointsActivity :
    BaseActivityWrapper<ReferralPointsActivityBinding, ReferralPointsNavigator, ReferralPointsViewModel>(),
    ReferralPointsNavigator, ShareSheetPresenter.GenericUriHandler {

    private val referralpointsViewModel: ReferralPointsViewModel by viewModels()
    var URIString: String? = null
    private var shareSheetPresenter: ShareSheetPresenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpToolBar(true, resources.getString(R.string.referral_points), R.drawable.ic_cross)
        val bundle = getBundle()
        bundle?.let {
            if (bundle.getParcelable<GpApiResponseData>(Constants.GramCashResponse) != null) {
                mViewModel?.gramCashResponseDataFromBundle?.set(bundle.getParcelable(Constants.GramCashResponse))
                mViewModel?.setMyReferralsListAdapter()
            }

            if (bundle.getString(Constants.SHAREIMAGEURIStRING) != null) {
                URIString = bundle.getString(Constants.SHAREIMAGEURIStRING)
            }
        }


        shareSheetPresenter = this?.let { ShareSheetPresenter(this, it) }
        shareSheetPresenter!!.shareDynamicLink()
    }

    override fun processGenericUri(genericUri: Uri) {
        //
    }


    override fun openShareIntent() {

        var shareMessage = resources.getString(R.string.welcome_msg)
        mViewModel?.gramCashResponseDataFromBundle?.get()?.share_message?.let {
            shareMessage = mViewModel?.gramCashResponseDataFromBundle?.get()?.share_message!!
        }


        var QRCodeURI: Uri? = Uri.parse(URIString)
        shareSheetPresenter?.shareDeepLinkWithExtraTextWithOption(
            shareMessage,
            getString(R.string.home_share_subject),
            QRCodeURI,
            IntentKeys.OtherShareKey
        )
    }


    fun getBundle(): Bundle? {
        return intent.extras
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

    override fun setMyReferralsAdapter(myReferralsAdapter: MyReferralsAdapter) {
        viewDataBinding.rvMyreferrals.adapter = myReferralsAdapter
    }

}